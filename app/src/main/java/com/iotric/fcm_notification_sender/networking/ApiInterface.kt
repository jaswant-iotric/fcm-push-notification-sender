package com.iotric.fcm_notification_sender.networking

import com.iotric.fcm_notification_sender.constants.SEND_PUSH
import com.iotric.fcm_notification_sender.models.PushNotificationRequestModel
import com.iotric.fcm_notification_sender.models.response.PushNotificationResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

public interface ApiInterface {
    @POST(SEND_PUSH)
    fun sendPush(
        @Header("Authorization") authorization: String,
        @Body pushNotificationRequestModel: PushNotificationRequestModel?
    ): Call<PushNotificationResponseModel?>
}