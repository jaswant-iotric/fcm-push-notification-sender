package com.iotric.fcm_notification_sender.database

import android.util.Log
import io.paperdb.Paper

object CommonData {
    fun readData(key: String?): Any? {
        return Paper.book().read(key!!, null)
    }

    fun writeData(key: String?, value: Any?) {
        if (value == null) {
            Log.d("Database->", "Can't write null values for $key")
            return
        }
        Paper.book().write(key!!, value)
    }

    fun destroy() {
        Paper.book().destroy()
    }

    fun destroyKey(key: String?) {
        Paper.book().delete(key!!)
    }
}