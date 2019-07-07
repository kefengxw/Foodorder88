package com.foodorder.order.di.component.activity

import com.foodorder.order.di.module.activity.UploadOverviewActivityModule
import com.foodorder.order.di.scope.ActivityScope
import com.foodorder.order.view.activity.UploadOverviewActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [UploadOverviewActivityModule::class])
interface UploadOverviewActivityComponent {

    fun inject(activity: UploadOverviewActivity)

    @Subcomponent.Builder
    interface Builder {

        fun uploadOverviewActivityModule(activityModule: UploadOverviewActivityModule): Builder
        fun build(): UploadOverviewActivityComponent        //only need this for sub component, that's all
    }
}