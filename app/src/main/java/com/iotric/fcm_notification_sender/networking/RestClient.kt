package com.iotric.fcm_notification_sender.networking

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.GsonBuilder
import com.iotric.fcm_notification_sender.MyApplication.Companion.getAppContext
import com.iotric.fcm_notification_sender.constants.FCM_BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RestClient {

    private var connectivityManager: ConnectivityManager? = null
    var retrofitInstanceForFCM: Retrofit? = null
    private const val TIME_OUT = 5

    @JvmStatic
    val apiInterface: ApiInterface
        get() {
            if (connectivityManager == null) {
                connectivityManager = getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            }
            if (retrofitInstanceForFCM == null) {
                retrofitInstanceForFCM = Retrofit.Builder()
                    .baseUrl(FCM_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
                    .client(httpClientWithoutSSL(needsInterceptor = true).build())
                    .build()
            }
            return retrofitInstanceForFCM!!.create(ApiInterface::class.java)
        }

    private fun httpClientWithoutSSL(needsInterceptor: Boolean = true): OkHttpClient.Builder {
        val httpClient = OkHttpClient.Builder()
        httpClient.readTimeout(TIME_OUT.toLong(), TimeUnit.MINUTES)
        httpClient.writeTimeout(TIME_OUT.toLong(), TimeUnit.MINUTES)
        httpClient.connectTimeout(TIME_OUT.toLong(), TimeUnit.MINUTES)

        return httpClient
    }
}