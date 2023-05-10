package com.iotric.fcm_notification_sender

import android.app.Application
import android.content.Context

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        private lateinit var context: Application
        fun getAppContext(): Context {
            return context
        }
    }
}