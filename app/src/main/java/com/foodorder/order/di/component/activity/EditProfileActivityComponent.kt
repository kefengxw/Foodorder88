package com.foodorder.order.di.component.activity

import com.foodorder.order.di.module.activity.EditProfileActivityModule
import com.foodorder.order.di.scope.ActivityScope
import com.foodorder.order.view.activity.EditProfileActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [EditProfileActivityModule::class])
interface EditProfileActivityComponent {

    fun inject(activity: EditProfileActivity)

    @Subcomponent.Builder
    interface Builder {

        fun editProfileActivityModule(activityModule: EditProfileActivityModule): Builder
        fun build(): EditProfileActivityComponent        //only need this for sub component, that's all
    }
}