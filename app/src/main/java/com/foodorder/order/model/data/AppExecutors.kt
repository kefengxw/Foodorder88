package com.foodorder.order.model.data

import android.os.Handler
import android.os.Looper
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors(private val mDiskIO: Executor, val mNetworkIO: Executor, val mMainThread: Executor) {

    companion object {

        private var mInstanceEx: AppExecutors? = null

        fun getInstanceEx(): AppExecutors {
            if (mInstanceEx == null) {
                mInstanceEx = AppExecutors(
                    Executors.newSingleThreadExecutor(),
                    Executors.newFixedThreadPool(1),
                    MainThreadExecutor()
                )
            }
            return mInstanceEx!!
        }

        fun destroyInstanceEx() {
            mInstanceEx = null
        }
    }

    private class MainThreadExecutor : Executor {
        private val mMainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mMainThreadHandler.post(command)
        }
    }

    fun asRxSchedulerDiskIO(): Scheduler {
        return Schedulers.from(mDiskIO)
    }

    fun asRxSchedulerNetwork(): Scheduler {
        return Schedulers.from(mNetworkIO)
    }

    fun asRxSchedulerMainThread(): Scheduler {
        return Schedulers.from(mMainThread)
    }

    fun runOnDiskIO(it: Runnable) {
        mDiskIO.execute(it)
    }

    fun runOnNetwork(it: Runnable) {
        mNetworkIO.execute(it)
    }

    fun runOnMainThread(it: Runnable) {
        mMainThread.execute(it)
    }
}