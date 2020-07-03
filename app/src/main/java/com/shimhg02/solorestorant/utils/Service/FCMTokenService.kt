package com.shimhg02.solorestorant.utils.Service

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService

class FCMTokenService : FirebaseMessagingService() {
    /**
     * The Application's current Instance ID token is no longer valid and thus a new one must be requested.
     */
    fun onTokenRefresh() {
        val token = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Token : $token")
        //FirebaseMessaging.getInstance().subscribeToTopic(FRIENDLY_ENGAGE_TOPIC);
    }

    companion object {
        private const val TAG = "MyFirebaseIIDService"
        private const val FRIENDLY_ENGAGE_TOPIC = "friendly_engage"
    }
}