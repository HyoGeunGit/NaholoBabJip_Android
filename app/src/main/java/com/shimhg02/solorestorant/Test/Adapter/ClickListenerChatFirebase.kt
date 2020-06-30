package com.shimhg02.solorestorant.Test.Adapter

import android.view.View

interface ClickListenerChatFirebase {
    /**
     * Quando houver click na imagem do chat
     * @param view
     * @param position
     */
    fun clickImageChat(
        view: View?,
        position: Int,
        nameUser: String?,
        urlPhotoUser: String?,
        urlPhotoClick: String?
    )

    /**
     * Quando houver click na imagem de mapa
     * @param view
     * @param position
     */
    fun clickImageMapChat(
        view: View?,
        position: Int,
        latitude: String?,
        longitude: String?
    )
}