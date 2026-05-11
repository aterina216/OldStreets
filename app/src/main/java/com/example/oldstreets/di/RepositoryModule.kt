package com.example.oldstreets.di

import com.example.oldstreets.data.remote.api.PastVuApi
import com.example.oldstreets.data.repository.AdressRepositoryImpl
import com.example.oldstreets.data.repository.PhotoRepositoryImpl
import com.example.oldstreets.domain.repository.AddressRepository
import com.example.oldstreets.domain.repository.PhotoRepository
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideRepository(repository: AdressRepositoryImpl): AddressRepository

}