package com.foodorder.order.di.component.activity

import com.foodorder.order.di.module.activity.UploadEditActivityModule
import com.foodorder.order.di.scope.ActivityScope
import com.foodorder.order.view.activity.UploadEditActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [UploadEditActivityModule::class])
interface UploadEditActivityComponent {

    fun inject(activity: UploadEditActivity)

    @Subcomponent.Builder
    interface Builder {

        fun uploadEditActivityModule(activityModule: UploadEditActivityModule): Builder
        fun build(): UploadEditActivityComponent        //only need this for sub component, that's all
    }
}