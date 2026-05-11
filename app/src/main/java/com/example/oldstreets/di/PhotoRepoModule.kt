package com.example.oldstreets.di

import com.example.oldstreets.data.remote.api.PastVuApi
import com.example.oldstreets.data.repository.PhotoRepositoryImpl
import com.example.oldstreets.domain.repository.PhotoDataSource
import com.example.oldstreets.domain.repository.PhotoRepository
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PhotoRepoModule {

    @Provides
    @Singleton
    fun providePhotoRepository(photoSource: Set <@JvmSuppressWildcards PhotoDataSource> ): PhotoRepository {
        return PhotoRepositoryImpl(photoSource)
    }

}