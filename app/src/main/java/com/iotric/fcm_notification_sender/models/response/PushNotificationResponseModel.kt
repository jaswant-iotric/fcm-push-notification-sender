package com.iotric.fcm_notification_sender.models.response

import com.google.gson.annotations.SerializedName

data class PushNotificationResponseModel(

	@field:SerializedName("message_id")
	val messageId: Long? = null
)
