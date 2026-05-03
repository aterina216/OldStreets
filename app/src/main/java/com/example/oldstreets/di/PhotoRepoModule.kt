package com.example.oldstreets.di

import com.example.oldstreets.data.remote.api.PastVuApi
import com.example.oldstreets.data.repository.PhotoRepositoryImpl
import com.example.oldstreets.domain.repository.PhotoRepository
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PhotoRepoModule {

    @Provides
    @Singleton
    fun providePhotoRepository(api: PastVuApi, moshi: Moshi): PhotoRepository {
        return PhotoRepositoryImpl(api, moshi)
    }
}