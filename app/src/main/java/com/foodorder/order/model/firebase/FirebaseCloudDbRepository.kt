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
import com.foodorder.order.view.componet.UpdateRemoteUserDataUnit
import com.foodorder.order.view.componet.UploadLocalFoodDataUnit
import com.foodorder.order.view.componet.UploadRemoteFoodDataUnit
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
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
    private val mDocUser = mDbCoRef//.document("user").collection("user")
    private val mDocFood = mDbCoRef.document("food").collection("food")
    private val mDocIngr = mDbCoRef.document("ingredient").collection("ingredient")
    //val mDocFoodIngr = mDbCoRef.document("food_ingre")
    //val mDocSubmit = mDbCoRef.document("submit")

    private val mStorageRef = mFbCloudStorage.getReference(mCoName)

    private var mUploadTaskSt: StorageTask<UploadTask.TaskSnapshot>? = null
    private var mUploadTaskDb: Task<Void>? = null
    private var mUpdateUserTaskDb: Task<Void>? = null

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

    private fun initResult(it: BehaviorSubject<FirebaseResult>) {
        it.onNext(loadingFbData())
    }

    private fun getUserDocumentRef(): DocumentReference {
        return mDocUser.document("user")
    }

    private fun getFoodDocumentRef(type: FireBaseFolderType): CollectionReference {
        return when (type) {
            UserFolder -> mDocUser
            FoodFolder -> mDocFood
            else -> mDocIngr
        }
    }

    private fun getUniqueIdString(): String {
        return DateTimeUtils.currentTimeMillis().toString()//unique name for this upload or data(food)
    }

    /**************************************************Update Food*****************************************************/
    fun foodTaskIsOngoing(): Boolean {
        return ((mUploadTaskSt != null) && (mUploadTaskSt!!.isInProgress)) || (mUploadTaskDb != null) && (mUploadTaskDb!!.isComplete)
    }

    fun uploadToUser(localFood: UploadLocalFoodDataUnit) {
        addOrUpdateFoodToStorage("user", UserFolder, localFood)
    }

    fun updateToFood(localFood: UploadLocalFoodDataUnit): Flowable<FirebaseResult> {
        return addOrUpdateFoodToStorage("food", FoodFolder, localFood)
    }

    fun uploadToIngredient(localFood: UploadLocalFoodDataUnit) {
        addOrUpdateFoodToStorage("ingredient", IngredientFolder, localFood)
    }

    fun deleteFood(docRef: DocumentReference, item: OverviewItem) {
        //delete picture first, and then update the database
        docRef.delete()//delete datdabase

        val stoRef = mStorageRef.child(item.imagePath)
        stoRef.delete()
    }

    private fun addOrUpdateFoodToStorage(
        folderType: String,
        type: FireBaseFolderType,
        localFood: UploadLocalFoodDataUnit
    ): Flowable<FirebaseResult> {

        val result = BehaviorSubject.create<FirebaseResult>()//这里是发射数据
        initResult(result)

        val remoteFood: UploadRemoteFoodDataUnit = localFood.uploadRemoteFood

        if (remoteFood.uniqueId == "") {
            //new, from "add new button"
            val uniqueId = getUniqueIdString()//unique name for this upload or data(food)
            handleNewUploadFood(folderType, type, uniqueId, localFood, remoteFood, result)
        } else {
            //update, two cases: 1. uri already update to localFood by user, 2. uri is still the firebase address
            if (remoteFood.imageRemoteAddr == "") {
                //case 1: uri is update to localFood files, and should upload localFood file to firebase
                handleNewUploadFood(folderType, type, remoteFood.uniqueId, localFood, remoteFood, result)
            } else {
                //case 2: still the firebase uri, no need to upload the file to firebase, only update the database, that's all
                handleUpdateUploadFood(type, remoteFood, result)
            }
        }

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    private fun handleNewUploadFood(
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
                handleUploadFoodSuccess(type, mStorageUser, remoteFood, imageWholeName, result)
            }
            .addOnFailureListener {
                handleUploadFoodError(it, result)
            }
    }

    private fun handleUpdateUploadFood(
        type: FireBaseFolderType,
        remoteFood: UploadRemoteFoodDataUnit,
        result: BehaviorSubject<FirebaseResult>
    ) {
        uploadFoodToDatabase(type, remoteFood, result)
    }

    private fun handleUploadFoodSuccess(
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
                    handleUploadFoodError(it.exception!!, result)
                }
                return@Continuation storageUser.downloadUrl
            }).addOnCompleteListener() {
            if (it.isSuccessful) {

                val downloadRri = it.result
                remoteFood.imageRemoteAddr = downloadRri.toString()//update the download link
                remoteFood.imageRemotePath = imageWholeName

                uploadFoodToDatabase(type, remoteFood, result)
            } else {
                handleUploadFoodError(it.exception!!, result)
            }
        }
    }

    private fun handleUploadFoodError(
        it: Exception,
        result: BehaviorSubject<FirebaseResult>
    ) {
        result.onNext(errorFbData(it.message))
        endThisFoodTask(result)
    }

    private fun uploadFoodToDatabase(
        type: FireBaseFolderType,
        remoteFood: UploadRemoteFoodDataUnit,
        result: BehaviorSubject<FirebaseResult>
    ) {
        //upload information to database
        val name = remoteFood.uniqueId
        val docRef = getFoodDocumentRef(type).document(name)

        mUploadTaskDb = docRef.set(remoteFood)
            .addOnFailureListener {
                handleUploadFoodError(it, result)
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
        return mDocFood.orderBy("uniqueId", Query.Direction.ASCENDING)
    }

    fun getFoodQueryWhereEqualTo(it: String): Query {
        return mDocFood.whereEqualTo("category", it).orderBy("uniqueId")
    }

    /*********************************************Update User Profile**************************************************/
    fun updateUser(localUser: UpdateLocalUserDataUnit): Flowable<FirebaseResult> {
        return addOrUpdateUser(localUser)
    }

    private fun addOrUpdateUser(
        localUser: UpdateLocalUserDataUnit
    ): Flowable<FirebaseResult> {

        val result = BehaviorSubject.create<FirebaseResult>()//here to omit data
        initResult(result)

        val remote: UpdateRemoteUserDataUnit = localUser.updateRemoteUser
        if ((localUser.imageLocalAddr != "") && (remote.imageRemoteAddr == "")) {
            //first time to update user --- upload logo first time直接上传图片
            val uniqueId = getUniqueIdString()
            handleUpdateUserWithImage(uniqueId, localUser, remote, result)
        } else if ((localUser.imageLocalAddr != "") && (remote.imageRemoteAddr != "")) {
            //will replace the logo with new image使用新图片和旧名字
            handleUpdateUserWithImage(remote.uniqueId, localUser, remote, result)
        } else if ((localUser.imageLocalAddr == "") && (remote.imageRemoteAddr != "")) {
            //still use the remote image, no need to change
            handleUpdateUser(remote, result)
        }
        //else if ((localUser.imageLocalAddr == "") && (remote.imageRemoteAddr == "")) //first time to update user, can not happen


        //update, two cases: 1. uri already update to localFood by user, 2. uri is still the firebase address
        if (remote.imageRemoteAddr == "") {
            //case 1: uri is update to localFood files, and should upload localFood file to firebase
            handleUpdateUserWithImage(remote.uniqueId, localFood, remote, result)
        } else {
            //case 2: still the firebase uri, no need to upload the file to firebase, only update the database, that's all
            handleUpdateUser(remote, result)
        }

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    private fun handleUpdateUserWithImage(
        uniqueId: String,
        localUser: UpdateLocalUserDataUnit,
        remoteUser: UpdateRemoteUserDataUnit,
        result: BehaviorSubject<FirebaseResult>
    ) {
        val uri: Uri = localUser.imageLocalAddr.toUri()
        val imageWholeName = "user" + "/" + uniqueId + "." + getFileExtension(uri)
        val mStorageUser = mStorageRef.child(imageWholeName)

        if (remoteUser.uniqueId != "") {
            remoteUser.uniqueId = uniqueId
        }

        mUploadTaskSt = mStorageUser.putFile(uri)
            .addOnSuccessListener {
                handleUserSuccess(mStorageUser, remoteUser, imageWholeName, result)
            }
            .addOnFailureListener {
                handleUploadFoodError(it, result)
            }
    }

    private fun handleUpdateUser(
        remoteUser: UpdateRemoteUserDataUnit,
        result: BehaviorSubject<FirebaseResult>
    ) {
        updateUserToDatabase(remoteUser, result)
    }

    private fun updateUserToDatabase(
        remoteUser: UpdateRemoteUserDataUnit,
        result: BehaviorSubject<FirebaseResult>
    ) {
        //upload information to database
        val docRef = getUserDocumentRef()

        mUpdateUserTaskDb = docRef.set(remoteUser, SetOptions.merge())
            .addOnFailureListener {
                handleUploadFoodError(it, result)
            }
            .addOnSuccessListener {
                result.onNext(successFbData())
                endThisUserTask(result)
            }
    }

    private fun endThisUserTask(result: BehaviorSubject<FirebaseResult>) {
        result.onComplete()
        mUploadTaskSt = null
        mUpdateUserTaskDb = null
    }
}