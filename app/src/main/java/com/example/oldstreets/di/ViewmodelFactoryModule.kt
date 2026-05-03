package com.example.oldstreets.di

import com.example.oldstreets.domain.repository.AddressRepository
import com.example.oldstreets.domain.repository.PhotoRepository
import com.example.oldstreets.ui.viewmodel.MainViewmodelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewmodelFactoryModule {

    @Provides
    @Singleton
    fun provideViewmodelFactory(repo: AddressRepository, photoRepository: PhotoRepository): MainViewmodelFactory {
        return MainViewmodelFactory(repo, photoRepository)
    }
}