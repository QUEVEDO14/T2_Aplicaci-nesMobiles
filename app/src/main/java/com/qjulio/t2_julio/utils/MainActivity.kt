package com.qjulio.t2_julio.utils

import android.app.Application
import com.google.firebase.FirebaseApp

class MainActivity : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
    }

}