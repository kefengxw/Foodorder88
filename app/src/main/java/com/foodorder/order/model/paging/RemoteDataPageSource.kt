package com.foodorder.order.model.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.foodorder.order.model.data.AppExecutors
import com.foodorder.order.model.data.ConvertToDisplayData
import com.foodorder.order.model.data.InternalDataConfiguration.AUTH_TOKEN
import com.foodorder.order.model.data.NetworkState
import com.foodorder.order.model.data.NetworkState.Companion.errorNwData
import com.foodorder.order.model.data.NetworkState.Companion.loadingNwData
import com.foodorder.order.model.data.NetworkState.Companion.successNwData
import com.foodorder.order.model.remote.RemoteBean
import com.foodorder.order.model.remote.RemoteDataInfoService
import com.foodorder.order.model.repository.DisplayData
import com.foodorder.order.model.repository.DisplayItem
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import retrofit2.Response

class RemoteDataPageSource(val mEx: AppExecutors, val mService: RemoteDataInfoService, val mName: String) :
    PageKeyedDataSource<String, DisplayItem>() {

    val mNetworkState = MutableLiveData<NetworkState>()
    var mTotal = 0
    var mOffset = 1//start index of each time
    var mLimit = 10//end index of each time

    fun getPagedListRemoteState(): LiveData<NetworkState> {
        return mNetworkState
    }

    fun initResult(list: MutableList<DisplayItem>) {
        mNetworkState.postValue(loadingNwData())
        list.clear()
    }

    fun handleSuccessResult(it: DisplayData) {
        mNetworkState.postValue(successNwData())
    }

    fun handleErrorResult(msg: String?) {
        mNetworkState.postValue(errorNwData(msg))
    }

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, DisplayItem>
    ) {

        var mResultInitList: MutableList<DisplayItem> = ArrayList<DisplayItem>()

        initResult(mResultInitList)
        val mRemote = mService.getRemoteInfo(
            AUTH_TOKEN, mName, "artist", "SE", mLimit, (mOffset - 1)
        )

        mRemote.subscribe(object : SingleObserver<Response<RemoteBean>> {
            override fun onSubscribe(d: Disposable) {}
            override fun onSuccess(response: Response<RemoteBean>) {
                if (response.isSuccessful) {
                    val it = ConvertToDisplayData.convert(response.body()!!)
                    handleSuccessResult(it)
                    if (it.mItem.isNullOrEmpty()) return
                    mResultInitList = it.mItem!! as MutableList<DisplayItem>
                    //callback.onResult(mResultList, 0, it.mTotal, null, it.mNext)
                    callback.onResult(mResultInitList, 0, it.mTotal, null, it.mNext)

                    mTotal = it.mTotal
                    mOffset += mLimit
                } else {
                    handleErrorResult(response.message())
                    callback.onResult(mResultInitList, 0, 0, null, null)
                }
            }

            override fun onError(e: Throwable) {
                handleErrorResult(e.message)
                callback.onResult(mResultInitList, 0, 0, null, null)
            }
        })
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, DisplayItem>) {

        var mResultAfterList: MutableList<DisplayItem> = ArrayList<DisplayItem>()

        initResult(mResultAfterList)
        val mRemote = mService.getRemoteInfo(
            AUTH_TOKEN, mName, "artist", "SE", mLimit, (mOffset - 1)
        )

        mRemote.subscribe(object : SingleObserver<Response<RemoteBean>> {
            override fun onSubscribe(d: Disposable) {}
            override fun onSuccess(response: Response<RemoteBean>) {
                if (response.isSuccessful) {
                    val it = ConvertToDisplayData.convert(response.body()!!)
                    handleSuccessResult(it)
                    if (it.mItem.isNullOrEmpty()) return
                    mResultAfterList = it.mItem!! as MutableList<DisplayItem>

                    val next = if (it.mNext == "") null else it.mNext
                    callback.onResult(mResultAfterList, next)

                    mOffset += mLimit
                } else {
                    handleErrorResult(response.message())
                    callback.onResult(mResultAfterList, null)
                }
            }

            override fun onError(e: Throwable) {
                handleErrorResult(e.message)
                callback.onResult(mResultAfterList, null)
            }
        })
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, DisplayItem>) {
        //just skip
    }
}