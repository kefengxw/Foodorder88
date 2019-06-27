package com.foodorder.order.model.firebase

import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import com.foodorder.order.app.HomeApplication
import com.foodorder.order.model.data.FireBaseFolderType
import com.foodorder.order.model.data.FireBaseFolderType.*
import com.foodorder.order.model.data.InternalStatusConfiguration.getLoginUserId
import com.foodorder.order.model.firebase.FirebaseResult.Companion.errorFbData
import com.foodorder.order.model.firebase.FirebaseResult.Companion.loadingFbData
import com.foodorder.order.model.firebase.FirebaseResult.Companion.successFbData
import com.foodorder.order.view.adapter.OverviewItem
import com.foodorder.order.view.componet.UpdateLocalUserDataUnit
import com.foodorder.order.view.componet.UploadLocalFoodDataUnit
import com.foodorder.order.view.componet.UploadRemoteFoodDataUnit
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

    val mFbCloudDatabase: FirebaseFirestore = FirebaseFirestore.getInstance()
    val mFbCloudStorage: FirebaseStorage = FirebaseStorage.getInstance()

    val mCoName: String = getLoginUserId()

    val mDbCoRef = mFbCloudDatabase.collection(mCoName)
    private val mDocUser = mDbCoRef.document("user").collection("user")
    private val mDocFood = mDbCoRef.document("food").collection("food")
    private val mDocIngr = mDbCoRef.document("ingredient").collection("ingredient")
    //val mDocFoodIngr = mDbCoRef.document("food_ingre")
    //val mDocSubmit = mDbCoRef.document("submit")

    val mStorageRef = mFbCloudStorage.getReference(mCoName)

    var mUploadTaskSt: StorageTask<UploadTask.TaskSnapshot>? = null
    var mUploadTaskDb: Task<Void>? = null

    /*  static FirebaseAuth
        getInstance(FirebaseApp firebaseApp)
        Returns an instance of this class corresponding to the given FirebaseApp instance.*/

    init {
        //document.addOnSuccessListener
        //noteRef.set(note).addOnSuccessListener 添加数据
        //noteRef.get().addOnSuccessListener 手动load
        //noteRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() 实时同步firebase的最新数据
        //notebookRef.add(note);
        //Note note = documentSnapshot.toObject(Note.class);
    }

    fun deleteFood(docRef: DocumentReference, item: OverviewItem) {
        //delete picture first, and then update the database
        docRef.delete()//delete datdabase

        val stoRef = mStorageRef.child(item.imagePath)
        stoRef.delete()
    }

    fun taskIsOngoing(): Boolean {
        return ((mUploadTaskSt != null) && (mUploadTaskSt!!.isInProgress)) || (mUploadTaskDb != null) && (mUploadTaskDb!!.isComplete)
    }

    fun uploadToUser(localFood: UploadLocalFoodDataUnit) {
        addToStorage("user", UserFolder, localFood)
    }

    fun updateToFood(localFood: UploadLocalFoodDataUnit): Flowable<FirebaseResult> {
        return addToStorage("food", FoodFolder, localFood)
    }

    fun uploadToIngredient(localFood: UploadLocalFoodDataUnit) {
        addToStorage("ingredient", IngredientFolder, localFood)
    }

//    fun updateUser(localFood: UpdateLocalUserDataUnit): Flowable<FirebaseResult> {
//        return addToStorage("food", FoodFolder, localFood)
//    }

    private fun deleteFromStorage() {

    }

    private fun initResult(it: BehaviorSubject<FirebaseResult>) {
        it.onNext(loadingFbData())
    }

    private fun addToStorage(
        folderType: String,
        type: FireBaseFolderType,
        localFood: UploadLocalFoodDataUnit
    ): Flowable<FirebaseResult> {

        val result = BehaviorSubject.create<FirebaseResult>()//这里是发射数据
        initResult(result)

        val remoteFood: UploadRemoteFoodDataUnit = localFood.uploadRemoteFood

        if (remoteFood.uniqueId == "") {
            //new, from "add new button"
            val uniqueId = DateTimeUtils.currentTimeMillis().toString()//unique name for this upload or data(food)
            handleNewUpload(folderType, type, uniqueId, localFood, remoteFood, result)
        } else {
            //update, two cases: 1. uri already update to localFood by user, 2. uri is still the firebase address
            if (remoteFood.imageRemoteAddr == "") {
                //case 1: uri is update to localFood files, and should upload localFood file to firebase
                handleNewUpload(folderType, type, remoteFood.uniqueId, localFood, remoteFood, result)
            } else {
                //case 2: still the firebase uri, no need to upload the file to firebase, only update the database, that's all
                handleUpdateUpload(type, remoteFood, result)
            }
        }

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    private fun handleNewUpload(
        folderType: String,
        type: FireBaseFolderType,
        uniqueId: String,
        localFood: UploadLocalFoodDataUnit,
        remoteFood: UploadRemoteFoodDataUnit,
        result: BehaviorSubject<FirebaseResult>
    ) {
        val uri: Uri = localFood.imageLocalAddr.toUri()
        val imageWholeName = folderType + "/" + uniqueId + "." + getFileExtension(uri)
        val mStorageUser = mStorageRef.child(imageWholeName)

        remoteFood.uniqueId = uniqueId
        mUploadTaskSt = mStorageUser.putFile(uri)
            .addOnSuccessListener {
                handleUploadSuccess(type, mStorageUser, remoteFood, imageWholeName, result)
            }
            .addOnFailureListener {
                handleUploadError(it, result)
            }
    }

    private fun handleUpdateUpload(
        type: FireBaseFolderType,
        remoteFood: UploadRemoteFoodDataUnit,
        result: BehaviorSubject<FirebaseResult>
    ) {
        uploadToDatabase(type, remoteFood, result)
    }

    private fun handleUploadSuccess(
        type: FireBaseFolderType,
        storageUser: StorageReference,
        remoteFood: UploadRemoteFoodDataUnit,
        imageWholeName: String,
        result: BehaviorSubject<FirebaseResult>
    ) {

        val addrTask = mUploadTaskSt!!.continueWithTask(
            Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
                if (it.isSuccessful) {
                    //do nothing here
                } else {
                    handleUploadError(it.exception!!, result)
                }
                return@Continuation storageUser.downloadUrl
            }).addOnCompleteListener() {
            if (it.isSuccessful) {

                val downloadRri = it.result
                remoteFood.imageRemoteAddr = downloadRri.toString()//update the download link
                remoteFood.imageRemotePath = imageWholeName

                uploadToDatabase(type, remoteFood, result)
            } else {
                handleUploadError(it.exception!!, result)
            }
        }
    }

    private fun handleUploadError(
        it: Exception,
        result: BehaviorSubject<FirebaseResult>
    ) {
        result.onNext(errorFbData(it.message))
        endThisTask(result)
    }

    private fun uploadToDatabase(
        type: FireBaseFolderType,
        remoteFood: UploadRemoteFoodDataUnit,
        result: BehaviorSubject<FirebaseResult>
    ) {
        //upload information to database
        val name = remoteFood.uniqueId
        val docRef = getDocumentRef(type).document(name)

        mUploadTaskDb = docRef.set(remoteFood)
            .addOnFailureListener {
                handleUploadError(it, result)
            }
            .addOnSuccessListener {
                result.onNext(successFbData())
                endThisTask(result)
            }
    }

    private fun endThisTask(result: BehaviorSubject<FirebaseResult>) {
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

    private fun getDocumentRef(type: FireBaseFolderType): CollectionReference {
        return when (type) {
            UserFolder -> mDocUser
            FoodFolder -> mDocFood
            else -> mDocIngr
        }
    }

    fun getQueryOrderByKey(): Query {
        return mDocFood.orderBy("uniqueId", Query.Direction.ASCENDING)
    }

    fun getQueryWhereEqualTo(it: String): Query {
        return mDocFood.whereEqualTo("category", it).orderBy("uniqueId")
    }
}