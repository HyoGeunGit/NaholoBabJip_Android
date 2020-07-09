package com.shimhg02.solorestorant.utils.Service

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService

@Suppress("DEPRECATION")
class FCMTokenService : FirebaseMessagingService() {

    fun onTokenRefresh() {
        val token = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Token Test : $token")
        FirebaseMessaging.getInstance().subscribeToTopic(FRIENDLY_ENGAGE_TOPIC);
    }

    companion object {
        private const val TAG = "MyFirebaseIIDService"
        const val FRIENDLY_ENGAGE_TOPIC = "friendly_engage"
    }
}