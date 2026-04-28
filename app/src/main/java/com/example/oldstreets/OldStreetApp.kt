package com.example.oldstreets

import android.app.Application
import com.example.oldstreets.di.AppComponent
import com.example.oldstreets.di.DaggerAppComponent

class OldStreetApp: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }
}