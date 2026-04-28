package com.example.oldstreets.di

import com.example.oldstreets.data.repository.AdressRepositoryImpl
import com.example.oldstreets.domain.repository.AddressRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideRepository(repository: AdressRepositoryImpl): AddressRepository
}