package com.foodorder.order.model.firebase

import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import com.foodorder.order.app.HomeApplication
import com.foodorder.order.model.data.FireBaseFolderType.FoodFolder
import com.foodorder.order.model.data.FireBaseFolderType.UserFolder
import com.foodorder.order.model.data.InternalStatusConfiguration.getLoginUserId
import com.foodorder.order.model.firebase.FirebaseResult.Companion.errorFbData
import com.foodorder.order.model.firebase.FirebaseResult.Companion.loadingFbData
import com.foodorder.order.model.firebase.FirebaseResult.Companion.successFbData
import com.foodorder.order.view.adapter.OverviewFoodItem
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject
import org.joda.time.DateTimeUtils

class FirebaseCloudDbRepository(/*val mEx: AppExecutors, val mFbAuth: FirebaseAuth*/) {

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

    private fun initResult(it: BehaviorSubject<FirebaseResult>) {
        it.onNext(loadingFbData())
    }

    private fun getDocumentRef(folder: FireBaseFolder): CollectionReference {
        return when (folder.ordinal) {
            UserFolder.ordinal -> mDocUser
            FoodFolder.ordinal -> mDocFood
            else -> mDocFood//mDocIngr
        }
    }

    private fun getDocumentName(caseKey: DataUnitCase): String {
        return when (caseKey.folder.ordinal) {
            UserFolder.ordinal -> "user"
            FoodFolder.ordinal -> caseKey.uniqueId
            else -> ""
        }
    }

    private fun getUniqueIdString(): String {
        return DateTimeUtils.currentTimeMillis().toString()//unique name for this data at firebase
    }

    fun foodTaskIsOngoing(): Boolean {
        return ((mUploadTaskSt != null) && (mUploadTaskSt!!.isInProgress)) || (mUploadTaskDb != null) && (mUploadTaskDb!!.isComplete)
    }

    fun <RemoteT> updateToUser(localData: DataUnitFb<RemoteT>): Flowable<FirebaseResult> {
        return addOrUpdateData(localData)
    }

    fun <RemoteT> addOrUpdateToFood(localData: DataUnitFb<RemoteT>): Flowable<FirebaseResult> {
        return addOrUpdateData(localData)
    }

    fun deleteFromFood(docRef: DocumentReference, item: OverviewFoodItem) {
        //delete picture first, and then update the database
        docRef.delete()//delete database

        val stoRef = mStorageRef.child(item.remoteImage.imageRemotePath)
        stoRef.delete()
    }

    private fun <RemoteT> addOrUpdateData(
        localData: DataUnitFb<RemoteT>
    ): Flowable<FirebaseResult> {

        val result = BehaviorSubject.create<FirebaseResult>()
        initResult(result)

        val caseKey = localData.caseKey
        val remote: DataUnitRemoteFb<RemoteT> = localData.remote

        if ((caseKey.imageLocalAddr == "") && (caseKey.imageRemoteAddr != "")) {
            //case 1: use remote image, only update the database
            handleUpdateWithoutImage(caseKey, remote, result)
        } else if ((caseKey.imageLocalAddr != "") && (caseKey.imageRemoteAddr == "")) {
            //case 1: use local image, directly
            if (caseKey.uniqueId == "") {//first time to upload User Profiile
                caseKey.uniqueId = getUniqueIdString()//unique name for this data at firebase
            }
            handleUpdateWithImage(caseKey, remote, result)
        } else if ((caseKey.imageLocalAddr != "") && (caseKey.imageRemoteAddr != "")) {
            //case 1: use local image, with old name
            val uniqueId = caseKey.uniqueId
            handleUpdateWithImage(caseKey, remote, result)
        } /*else if ((case.imageLocalAddr == "") && (case.imageRemoteAddr == "")) {
           can not happen here
        }*/

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    private fun <RemoteT> handleUpdateWithImage(
        caseKey: DataUnitCase,
        remote: DataUnitRemoteFb<RemoteT>,
        result: BehaviorSubject<FirebaseResult>
    ) {
        val uri: Uri = caseKey.imageLocalAddr.toUri()
        val imageWholeName = caseKey.folder.value + "/" + caseKey.uniqueId + "." + getFileExtension(uri)
        val mStorageUser = mStorageRef.child(imageWholeName)

        //update on Storage
        mUploadTaskSt = mStorageUser.putFile(uri)
            .addOnSuccessListener {
                handleUpdateWithImageSuccess(caseKey, mStorageUser, remote, imageWholeName, result)
            }
            .addOnFailureListener {
                handleUpdateError(it, result)
            }
    }

    private fun <RemoteT> handleUpdateWithoutImage(
        caseKey: DataUnitCase,
        remote: DataUnitRemoteFb<RemoteT>,
        result: BehaviorSubject<FirebaseResult>
    ) {
        updateOnDatabase(caseKey, remote, result)
    }

    private fun <RemoteT> handleUpdateWithImageSuccess(
        caseKey: DataUnitCase,
        storageUser: StorageReference,
        remote: DataUnitRemoteFb<RemoteT>,
        imageWholeName: String,
        result: BehaviorSubject<FirebaseResult>
    ) {

        val addrTask = mUploadTaskSt!!.continueWithTask(
            Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
                if (it.isSuccessful) {
                    //do nothing here
                } else {
                    handleUpdateError(it.exception!!, result)
                }
                return@Continuation storageUser.downloadUrl
            }).addOnCompleteListener() {
            if (it.isSuccessful) {

                val downloadRri = it.result

                remote.remoteImage.imageRemoteAddr = downloadRri.toString()//update the download link
                remote.remoteImage.imageRemotePath = imageWholeName

                updateOnDatabase(caseKey, remote, result)
            } else {
                handleUpdateError(it.exception!!, result)
            }
        }
    }

    private fun handleUpdateError(
        it: Exception,
        result: BehaviorSubject<FirebaseResult>
    ) {
        result.onNext(errorFbData(it.message))
        endThisFoodTask(result)
    }

    private fun <RemoteT> updateOnDatabase(
        caseKey: DataUnitCase,
        remote: DataUnitRemoteFb<RemoteT>,
        result: BehaviorSubject<FirebaseResult>
    ) {
        //upload information to database
        remote.uniqueId = caseKey.uniqueId
        val name = getDocumentName(caseKey)
        println("the name is :{$name}")
        val docRef = getDocumentRef(caseKey.folder).document(name)

        mUploadTaskDb = docRef.set(remote as Any)
            .addOnFailureListener {
                handleUpdateError(it, result)
            }
            .addOnSuccessListener {
                result.onNext(successFbData())
                endThisFoodTask(result)
            }
    }

    private fun endThisFoodTask(result: BehaviorSubject<FirebaseResult>) {
        result.onComplete()
        mUploadTaskSt = null
        mUploadTaskDb = null
    }

    private fun getFileExtension(uri: Uri): String {
        //后续需要修改为Dagger
        val app: HomeApplication = HomeApplication.mHomeApp
        val cResolver = app.contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cResolver.getType(uri))!!
    }

    fun getFoodQueryOrderByKey(): Query {
        val x = mDocFood.orderBy("uniqueId", Query.Direction.ASCENDING)
        return x
    }

    fun getFoodQueryWhereEqualTo(it: String): Query {
        return mDocFood.whereEqualTo("remoteInfo.category", it).orderBy("uniqueId")
    }

    //待优化，后续可以拆分为2个firebase的文件，一个负责更新，一个负责查询(大部分情况下是查询操作)；

}