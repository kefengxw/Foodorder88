package com.foodorder.order.model.firebase

import com.foodorder.order.model.data.FireBaseFolderType.FoodFolder
import com.foodorder.order.model.data.FireBaseFolderType.UserFolder
import com.foodorder.order.model.data.InternalStatusConfiguration.getLoginUserId
import com.foodorder.order.model.firebase.FirebaseUserResult.Companion.errorFbUserData
import com.foodorder.order.model.firebase.FirebaseUserResult.Companion.loadingFbUserData
import com.foodorder.order.model.firebase.FirebaseUserResult.Companion.successFbUserData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject
import org.joda.time.DateTimeUtils

class FirebaseCloudQueryRepository(/*val mEx: AppExecutors, val mFbAuth: FirebaseAuth*/) {

    private val mFbCloudDatabase: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val mFbCloudStorage: FirebaseStorage = FirebaseStorage.getInstance()

    private val mCoName: String = getLoginUserId()

    private val mDbCoRef = mFbCloudDatabase.collection(mCoName)

    private val mDocUser = mDbCoRef
    private val mDocFood = mDbCoRef.document("food").collection("food")
    //private val mDocIngr = mDbCoRef.document("ingredient").collection("ingredient")

    private val mStorageRef = mFbCloudStorage.getReference(mCoName)

    private var mUploadTaskSt: StorageTask<UploadTask.TaskSnapshot>? = null
    private var mUploadTaskDb: Task<Void>? = null
    private var mUpdateUserTaskDb: Task<Void>? = null

    init {

    }

    private fun initResult(it: BehaviorSubject<FirebaseUserResult>) {
        it.onNext(loadingFbUserData())
    }

    private fun getDocumentRef(folder: FireBaseFolder): CollectionReference {
        return when (folder.ordinal) {
            UserFolder.ordinal -> mDocUser
            FoodFolder.ordinal -> mDocFood
            else -> mDocFood//mDocIngr
        }
    }

    private fun getDocumentName(caseKey: DataUnitQueryCase): String {
        return when (caseKey.folder.ordinal) {
            UserFolder.ordinal -> "user"
            FoodFolder.ordinal -> caseKey.uniqueId
            else -> ""
        }
    }

    fun getUserProfile(): Flowable<FirebaseUserResult> {

        val result = BehaviorSubject.create<FirebaseUserResult>()
        initResult(result)

        val docRef = mDocUser.document("user")
        val x= docRef.get(/*Source.SERVER*/)
            .addOnSuccessListener {
                val user = it.toObject(GetUserDataUnitRemoteFb::class.java)
                //here user could be null or not-null, be careful, if it is null, than it is the first time
                result.onNext(successFbUserData(user))
                endThisUserTask(result)
            }
            .addOnFailureListener {
                handleUpdateError(it, result)
            }

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    private fun getUniqueIdString(): String {
        return DateTimeUtils.currentTimeMillis().toString()//unique name for this data at firebase
    }

    fun foodTaskIsOngoing(): Boolean {
        return ((mUploadTaskSt != null) && (mUploadTaskSt!!.isInProgress)) || (mUploadTaskDb != null) && (mUploadTaskDb!!.isComplete)
    }


    private fun endThisUserTask(result: BehaviorSubject<FirebaseUserResult>) {
        result.onComplete()
        mUploadTaskSt = null
        mUploadTaskDb = null
    }

    fun getFoodQueryOrderByKey(): Query {
        val x = mDocFood.orderBy("uniqueId", Query.Direction.ASCENDING)
        return x
    }

    fun getFoodQueryWhereEqualTo(it: String): Query {
        return mDocFood.whereEqualTo("remoteInfo.category", it).orderBy("uniqueId")
    }

    private fun handleUpdateError(
        it: Exception,
        result: BehaviorSubject<FirebaseUserResult>
    ) {
        result.onNext(errorFbUserData(it.message))
        endThisUserTask(result)
    }
}