package com.shimhg02.solorestorant.utils.StoryUtil.StoryProgressUtils.BitMapTransformer

import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation


class DelayBitmapTransformation(private val delay: Int) :
    BitmapTransformation(null as BitmapPool?) {
    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        w: Int,
        h: Int
    ): Bitmap {
        try {
            Thread.sleep(delay.toLong())
        } catch (e: InterruptedException) {
            Log.i("DELAY", "Sleeping for " + delay + "ms was interrupted.", e)
        }
        return toTransform
    }

    override fun getId(): String {
        return ""
    }

}