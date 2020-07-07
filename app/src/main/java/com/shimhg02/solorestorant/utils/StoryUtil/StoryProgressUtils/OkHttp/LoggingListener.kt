package com.shimhg02.solorestorant.utils.StoryUtil.StoryProgressUtils.OkHttp

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.target.ViewTarget
import com.shimhg02.solorestorant.utils.StoryUtil.StoryProgressUtils.OkHttp.NoOpRequestListener.Companion.get
import com.shimhg02.solorestorant.utils.StoryUtil.StoryProgressUtils.Target.WrappingTarget
import java.util.*


class LoggingListener<A, B> @JvmOverloads constructor(
    private val level: Int,
    private val name: String,
    delegate: RequestListener<A, B>? = null
) : RequestListener<A, B> {
    private val delegate: RequestListener<A, B>

    @JvmOverloads
    constructor(name: String = "") : this(Log.VERBOSE, name) {
    }

    constructor(delegate: RequestListener<A, B>?) : this(Log.VERBOSE, "", delegate) {}

    override fun onException(
        e: Exception,
        model: A,
        target: Target<B>,
        isFirstResource: Boolean
    ): Boolean {
        Log.println(
            level, "GLIDE", String.format(
                Locale.ROOT,
                "%s.onException(%s, %s, %s, %s)\n%s",
                name,
                e,
                model,
                strip(
                    target
                ),
                isFirst(isFirstResource),
                Log.getStackTraceString(e)
            )
        )
        return delegate.onException(e, model, target, isFirstResource)
    }

    override fun onResourceReady(
        resource: B,
        model: A,
        target: Target<B>,
        isFromMemoryCache: Boolean,
        isFirstResource: Boolean
    ): Boolean {
        val resourceString =
            strip(
                getResourceDescription(resource)
            )
        val targetString =
            strip(
                getTargetDescription(target)
            )
        Log.println(
            level, "GLIDE", String.format(
                Locale.ROOT,
                "%s.onResourceReady(%s, %s, %s, %s, %s)",
                name,
                resourceString,
                model,
                targetString,
                isMem(isFromMemoryCache),
                isFirst(isFirstResource)
            )
        )
        return delegate.onResourceReady(resource, model, target, isFromMemoryCache, isFirstResource)
    }

    private fun isMem(isFromMemoryCache: Boolean): String {
        return if (isFromMemoryCache) "sync" else "async"
    }

    private fun isFirst(isFirstResource: Boolean): String {
        return if (isFirstResource) "first" else "not first"
    }

    private fun getTargetDescription(target: Target<*>): String {
        val result: String
        result = if (target is WrappingTarget<*>) {
            val wrapped =
                target.wrappedTarget
            String.format(
                Locale.ROOT,
                "%s in %s",
                getTargetDescription(wrapped),
                target
            )
        } else if (target is ViewTarget<*, *>) {
            val v = target.view
            val p = v.layoutParams
            String.format(
                Locale.ROOT,
                "%s(params=%dx%d->size=%dx%d)",
                target,
                p.width,
                p.height,
                v.width,
                v.height
            )
        } else {
            target.toString()
        }
        return result
    }

    private fun getResourceDescription(resource: B): String {
        val result: String
        result = if (resource is Bitmap) {
            val bm = resource as Bitmap
            String.format(
                Locale.ROOT,
                "%s(%dx%d@%s)", resource, bm.width, bm.height, bm.config
            )
        } else if (resource is BitmapDrawable) {
            val bm = (resource as BitmapDrawable).bitmap
            String.format(
                Locale.ROOT,
                "%s(%dx%d@%s)", resource, bm.width, bm.height, bm.config
            )
        } else if (resource is GlideBitmapDrawable) {
            val bm = (resource as GlideBitmapDrawable).bitmap
            String.format(
                Locale.ROOT,
                "%s(%dx%d@%s)", resource, bm.width, bm.height, bm.config
            )
        } else if (resource is Drawable) {
            val d =
                resource as Drawable
            String.format(
                Locale.ROOT,
                "%s(%dx%d)", resource, d.intrinsicWidth, d.intrinsicHeight
            )
        } else {
            resource.toString()
        }
        return result
    }

    companion object {
        private fun strip(text: Any): String {
            return text.toString().replace("(com|android|net|org)(\\.[a-z]+)+\\.".toRegex(), "")
        }
    }

    init {
        this.delegate = (delegate ?: get())
    }
}