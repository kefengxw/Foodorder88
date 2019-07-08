package com.foodorder.order.di.module.viewmodel

import androidx.annotation.NonNull
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ViewModelProviderFactory @Inject constructor(
    var mVmMaps: Map<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @NonNull
    override fun <T : ViewModel?> create(@NonNull modelClass: Class<T>): T {
        var creator: Provider<out ViewModel>? = mVmMaps[modelClass]
        if (creator == null) {
            for ((key, value) in mVmMaps) {
                if (modelClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        }

        if (creator == null) throw java.lang.IllegalArgumentException("Unknown ViewModel Class $modelClass")

        try {
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}