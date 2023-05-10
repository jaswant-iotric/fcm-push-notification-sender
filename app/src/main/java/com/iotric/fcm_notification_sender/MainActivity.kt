package com.iotric.fcm_notification_sender

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.iotric.fcm_notification_sender.models.Android
import com.iotric.fcm_notification_sender.models.Notification
import com.iotric.fcm_notification_sender.models.PushNotificationRequestModel
import com.iotric.fcm_notification_sender.models.response.PushNotificationResponseModel
import com.iotric.fcm_notification_sender.networking.InternetConnection
import com.iotric.fcm_notification_sender.networking.RestClient
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class NotificationType {
    TOPIC,
    DEVICE
}

class MainActivity : AppCompatActivity() {

    private var notificationType = NotificationType.TOPIC

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews() {
        rbSendToTopic.isChecked = true

        rgTypeOfNotification.setOnCheckedChangeListener { radioGroup, i ->
            when(i) {
                R.id.rbSendToTopic -> {
                    notificationType = NotificationType.TOPIC
                    etTopicOrDeviceToken.hint = "Topic Name"
                }
                R.id.rbSendToDevice -> {
                    notificationType = NotificationType.DEVICE
                    etTopicOrDeviceToken.hint = "Device Token"
                }
            }
        }

        btnSend.setOnClickListener {
            if(areFieldsValid()) {
                sendPushNotificationToTopic(etTopicOrDeviceToken.text.toString())
            }
        }
    }

    private fun areFieldsValid(): Boolean {



        if(etServerKey.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please enter server key", Toast.LENGTH_LONG).show()
            return false
        }

        if(etNotificationTitle.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please enter notification title", Toast.LENGTH_LONG).show()
            return false
        }

        if(etNotificationMessage.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please enter notification message", Toast.LENGTH_LONG).show()
            return false
        }

        if(etTopicOrDeviceToken.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please enter ${if(notificationType == NotificationType.TOPIC) "Topic Name" else "Device Token"}", Toast.LENGTH_LONG).show()
            return false
        }
        if(!InternetConnection.checkConnection(this)) {
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    private fun sendPushNotificationToTopic(topicOrDeviceTokenName: String) {
        val serverKey = "key=${etServerKey.text?.toString()?.replace("key=", "")}"
        val to = if(notificationType == NotificationType.TOPIC) "/topics/$topicOrDeviceTokenName" else "$topicOrDeviceTokenName"
        val android = Android(priority = "high")
        val notification = Notification(body = etNotificationMessage.text.toString(), title = etNotificationTitle.text.toString())
        val pushNotificationRequestModel = PushNotificationRequestModel(
            to = to,
            notification = notification,
            android = android
        )

        RestClient.apiInterface.sendPush(
            authorization = serverKey,
            pushNotificationRequestModel = pushNotificationRequestModel)
            .enqueue(object : Callback<PushNotificationResponseModel?> {
                override fun onResponse(
                    call: Call<PushNotificationResponseModel?>,
                    response: Response<PushNotificationResponseModel?>
                ) {
                    if(response.body()?.messageId != null) {
                        Toast.makeText(this@MainActivity, "Notifications Sent!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@MainActivity, "Failed to send, please check your inputs!", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<PushNotificationResponseModel?>, t: Throwable) {
                    Log.d("FCM--->", "Failed " + t.message)
                }

            })
    }
}