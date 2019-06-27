package com.foodorder.order.model.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.foodorder.order.model.data.AppExecutors
import com.foodorder.order.model.data.InternalDataConfiguration.getToken
import com.foodorder.order.model.data.Resource
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import retrofit2.Response

//database, network, UI, 3 threads
abstract class NetworkBoundResource<ResultType, RequestType> @MainThread constructor(private val mEx: AppExecutors) {

    private val mResult = PublishSubject.create<Resource<ResultType>>()

    init {
        //all logical control here, local first, uploadRemoteFood first, or fetch every time
        loadData()
    }

    private fun loadData() {
        @Suppress("UNCHECKED_CAST")
        mResult.onNext(Resource.loadingData(null as ResultType))
        if (shouldGetRemoteToken()) {
            fetchRemoteToken()
        } else {
            mResult.onNext(Resource.successData(getToken() as ResultType))
        }
    }

    private fun fetchRemoteToken() {
        createTokenCall()
            .subscribeOn(mEx.asRxSchedulerNetwork())
            .observeOn(mEx.asRxSchedulerMainThread())
            .subscribe(object : SingleObserver<Response<RequestType>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(response: Response<RequestType>) {
                    if (response.isSuccessful) {
                        convertTokenCallResult(processTokenResponse(response)!!)
                        mResult.onNext(Resource.successData(getToken() as ResultType))
                    } else {
                        onFetchTokenFailed()
                        mResult.onNext(
                            Resource.errorData(
                                null as ResultType,
                                response.message()
                            )
                        )
                    }
                }

                override fun onError(e: Throwable) {
                    onFetchTokenFailed()
                    mResult.onNext(
                        Resource.errorData(
                            null as ResultType,
                            e.message
                        )
                    )
                }
            })
    }

    // Returns a LiveData object that represents the resource that's implemented in the base class.
    //.startWith(Resource.loading(null));
    fun asFlowable(): Flowable<Resource<ResultType>> {
        return mResult.toFlowable(BackpressureStrategy.BUFFER)
    }

    @WorkerThread
    private fun processTokenResponse(response: Response<RequestType>): RequestType? {
        return response.body()//return response.getBody();
    }

    @WorkerThread
    protected abstract fun convertTokenCallResult(data: RequestType)

    @MainThread
    protected abstract fun shouldGetRemoteToken(): Boolean

    @MainThread
    protected abstract fun createTokenCall(): Single<Response<RequestType>>

    @MainThread
    protected open fun onFetchTokenFailed() {/*Log*/
    }
}