package com.example.oldstreets.di

import com.example.oldstreets.data.datasource.CommonsApiDataSource
import com.example.oldstreets.data.datasource.PastVuPhotoDataSource
import com.example.oldstreets.data.remote.dto.commons.CommonsPhotoResponse
import com.example.oldstreets.domain.repository.PhotoDataSource
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Module
abstract class PhotoDataSourceModule {

    @Binds
    @IntoSet
    abstract fun bindPastVuDataSource(pastVu: PastVuPhotoDataSource): PhotoDataSource

    @Binds
    @IntoSet
    abstract fun bindCommonsDataSource(commons: CommonsApiDataSource): PhotoDataSource
}