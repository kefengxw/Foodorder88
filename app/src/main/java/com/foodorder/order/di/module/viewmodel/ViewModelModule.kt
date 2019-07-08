package com.foodorder.order.di.module.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.foodorder.order.viewmodel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory

    //@Binds 和 @Provider的作用相差不大，区别在于@Provider需要写明具体的实现，而@Binds只是告诉Dagger2谁是谁实现的
    //當物件的建立方式只是用new呼叫constructor的時候，可以用@Binds來取代@Provides
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SubmitViewModel::class)
    abstract fun bindSubmitViewModel(viewModel: SubmitViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InitialViewModel::class)
    abstract fun bindInitialViewModel(viewModel: InitialViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AboutViewModel::class)
    abstract fun bindAboutViewModel(viewModel: AboutViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditProfileViewModel::class)
    abstract fun bindEditProfileViewModel(viewModel: EditProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecyListDataViewModel::class)
    abstract fun bindRecyListDataViewModel(viewModel: RecyListDataViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindRegisterViewModel(viewModel: RegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingViewModel::class)
    abstract fun bindSettingViewModel(viewModel: SettingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UploadOverviewModel::class)
    abstract fun bindUploadOverviewModel(viewModel: UploadOverviewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UploadEditViewModel::class)
    abstract fun bindUploadEditViewModel(viewModel: UploadEditViewModel): ViewModel
}