package com.example.oldstreets.di

import com.example.oldstreets.MainActivity
import com.example.oldstreets.OldStreetApp
import dagger.Component
import javax.inject.Singleton

@Component(modules = [NetworkModule::class, RepositoryModule::class, ViewmodelFactoryModule::class,
    PhotoRepoModule::class, PhotoDataSourceModule::class])
@Singleton
interface AppComponent {
    fun inject(app: OldStreetApp)
    fun inject(activity: MainActivity)
}