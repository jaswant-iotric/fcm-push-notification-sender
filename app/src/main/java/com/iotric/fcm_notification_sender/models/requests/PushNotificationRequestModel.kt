package com.iotric.fcm_notification_sender.models

import com.google.gson.annotations.SerializedName

data class PushNotificationRequestModel(

	@field:SerializedName("notification")
	val notification: Notification? = null,

	@field:SerializedName("android")
	val android: Android? = null,

	@field:SerializedName("to")
	val to: String? = null
)

data class Notification(

	@field:SerializedName("body")
	val body: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)

data class Android(
	@field:SerializedName("priority")
	val priority: String? = null,
)
