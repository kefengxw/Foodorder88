package com.foodorder.order.model.firebase

import com.foodorder.order.model.data.Resource
import com.foodorder.order.model.data.Resource.Companion.errorData
import com.foodorder.order.model.data.Resource.Companion.loadingData
import com.foodorder.order.model.data.Resource.Companion.successData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject

class FirebaseAuthRepository(/*val mEx: AppExecutors, val mFbAuth: FirebaseAuth*/) {

    val mFbAuth: FirebaseAuth = FirebaseAuth.getInstance()

    //待完成dagger之后添加这个功能
    //val mSharedPre = SecuritySharedPreferencesFactory.getInstanceSSharedPre()
    //val username = mSharedPre.getString("username", "")
    //val password = mSharedPre.getString("password", "")

    /*  static FirebaseAuth
        getInstance(FirebaseApp firebaseApp)
        Returns an instance of this class corresponding to the given FirebaseApp instance.*/

    fun handleRegisterByEmail(email: String, password: String): Flowable<Resource<AuthResult>> {

        val result = BehaviorSubject.create<Resource<AuthResult>>()

        initResult(result)
        mFbAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(OnCompleteListener {
                handleFbListener(it, result)
            })

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    fun handleLoginByEmail(email: String, password: String): Flowable<Resource<AuthResult>> {

        val result = BehaviorSubject.create<Resource<AuthResult>>()//这里是发射数据

        //待完成dagger之后添加这个功能，本地完成验证即可，而不是真正的登录，特殊场景，另外一个owner登录呢，必须先退出，退出再重新登录即可；
        //if (mFbAuth.currentUser != null ){//already login to the firebase
        //进行本地检查

        //} else {// first time to login
        initResult(result)
        mFbAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(OnCompleteListener {
                handleFbListener(it, result)
            })
        //}
        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    private fun initResult(it: BehaviorSubject<Resource<AuthResult>>) {
        it.onNext(loadingData(null as? AuthResult))
    }

    private fun handleFbListener(it: Task<AuthResult>, result: BehaviorSubject<Resource<AuthResult>>) {
        if (it.isSuccessful) {
            result.onNext(successData(it.result!!))
        } else {
            try {
                when (it.exception) {
                    is FirebaseAuthUserCollisionException -> {
                        val exc = it.exception as FirebaseAuthUserCollisionException
                        //val error = FirebaseError()
                        if (exc.errorCode == "ERROR_EMAIL_ALREADY_IN_USE") {
                            //还可以嵌套在次查询的流程
                            println("HELLO:1")
                        }
                    }
                }

                result.onNext(errorData(it.result!!))
            } catch (e: Exception) {
                println("HELLO:2")//好像不需要做什么事情，仅仅是截获这个异常而已，终止异常往下传播
            }
/*            finally {

            }*/
        }
        result.onComplete()
    }

    fun signOut() {
        mFbAuth.signOut()
    }

//    mAuth.addAuthStateListener(mAuthListener);
//    mAuth.removeAuthStateListener(mAuthListener);
//    FirebaseAuthActionCodeException,
//    FirebaseAuthEmailException,
//    FirebaseAuthInvalidCredentialsException,
//    FirebaseAuthInvalidUserException,
//    FirebaseAuthRecentLoginRequiredException,
//    FirebaseAuthUserCollisionException,
//    FirebaseAuthWebException
//    FirebaseAuthWeakPasswordException
}