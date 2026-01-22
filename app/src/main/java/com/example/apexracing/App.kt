package com.example.apexracing

import android.app.Application
import com.example.apexracing.api.ApiHandler

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ApiHandler.init()
    }
}