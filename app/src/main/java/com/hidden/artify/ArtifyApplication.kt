package com.hidden.artify

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ArtifyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

    }

    companion object {
        var hasSubscription : Boolean = false
    }
}