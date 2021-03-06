package com.shimhg02.solorestorant.utils.StoryUtil.StoryProgressUtils.OkHttp

import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class NoOpRequestListener<A, B> private constructor() : RequestListener<A, B> {
    override fun onException(
        e: Exception,
        a: A,
        target: Target<B>,
        b: Boolean
    ): Boolean {
        return false
    }

    override fun onResourceReady(
        b: B,
        a: A,
        target: Target<B>,
        b2: Boolean,
        b1: Boolean
    ): Boolean {
        return false
    }

    companion object {
        private val INSTANCE: RequestListener<*, *> =
            NoOpRequestListener<Any?, Any?>()

        fun <A, B> get(): RequestListener<A, B> {
            return INSTANCE as RequestListener<A, B>
        }
    }
}