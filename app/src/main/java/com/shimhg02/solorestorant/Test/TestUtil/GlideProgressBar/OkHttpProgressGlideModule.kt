package com.shimhg02.solorestorant.Test.TestUtil.GlideProgressBar

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.GlideModule
import okhttp3.*
import okio.*
import java.io.IOException
import java.io.InputStream
import java.util.*

/**
 * Created by rahuljanagouda on 30/09/17.
 */
class OkHttpProgressGlideModule : GlideModule {
    override fun applyOptions(
        context: Context,
        builder: GlideBuilder
    ) {
    }

    override fun registerComponents(
        context: Context,
        glide: Glide
    ) {
        val client = OkHttpClient.Builder()
            .addNetworkInterceptor(
                createInterceptor(
                    DispatchingProgressListener()
                )
            )
            .build()
        glide.register(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(client)
        )
    }

    interface UIProgressListener {
        fun onProgress(bytesRead: Long, expectedLength: Long)

        /**
         * Control how often the listener needs an update. 0% and 100% will always be dispatched.
         * @return in percentage (0.2 = call [.onProgress] around every 0.2 percent of progress)
         */
        val granualityPercentage: Float
    }

    private interface ResponseProgressListener {
        fun update(
            url: HttpUrl,
            bytesRead: Long,
            contentLength: Long
        )
    }

    private class DispatchingProgressListener internal constructor() : ResponseProgressListener {
        private val handler: Handler
        override fun update(
            url: HttpUrl,
            bytesRead: Long,
            contentLength: Long
        ) {
            //System.out.printf("%s: %d/%d = %.2f%%%n", url, bytesRead, contentLength, (100f * bytesRead) / contentLength);
            val key = url.toString()
            val listener =
                LISTENERS[key] ?: return
            if (contentLength <= bytesRead) {
                forget(key)
            }
            if (needsDispatch(key, bytesRead, contentLength, listener.granualityPercentage)) {
                handler.post { listener.onProgress(bytesRead, contentLength) }
            }
        }

        private fun needsDispatch(
            key: String,
            current: Long,
            total: Long,
            granularity: Float
        ): Boolean {
            if (granularity == 0f || current == 0L || total == current) {
                return true
            }
            val percent = 100f * current / total
            val currentProgress = (percent / granularity).toLong()
            val lastProgress =
                PROGRESSES[key]
            return if (lastProgress == null || currentProgress != lastProgress) {
                PROGRESSES[key] = currentProgress
                true
            } else {
                false
            }
        }

        companion object {
            private val LISTENERS: MutableMap<String, UIProgressListener> =
                HashMap()
            private val PROGRESSES: MutableMap<String, Long> =
                HashMap()

            fun forget(url: String) {
                LISTENERS.remove(url)
                PROGRESSES.remove(url)
            }

            fun expect(url: String, listener: UIProgressListener) {
                LISTENERS[url] = listener
            }
        }

        init {
            handler = Handler(Looper.getMainLooper())
        }
    }

    private class OkHttpProgressResponseBody internal constructor(
        private val url: HttpUrl, private val responseBody: ResponseBody?,
        private val progressListener: ResponseProgressListener
    ) : ResponseBody() {
        private var bufferedSource: BufferedSource? = null
        override fun contentType(): MediaType? {
            return responseBody!!.contentType()
        }

        override fun contentLength(): Long {
            return responseBody!!.contentLength()
        }

        override fun source(): BufferedSource {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody!!.source()))
            }
            return bufferedSource!!
        }

        private fun source(source: Source): Source {
            return object : ForwardingSource(source) {
                var totalBytesRead = 0L

                @Throws(IOException::class)
                override fun read(sink: Buffer, byteCount: Long): Long {
                    val bytesRead = super.read(sink, byteCount)
                    val fullLength = responseBody!!.contentLength()
                    if (bytesRead == -1L) { // this source is exhausted
                        totalBytesRead = fullLength
                    } else {
                        totalBytesRead += bytesRead
                    }
                    progressListener.update(url, totalBytesRead, fullLength)
                    return bytesRead
                }
            }
        }

    }

    companion object {
        private fun createInterceptor(listener: ResponseProgressListener): Interceptor {
            return Interceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(request)
                response.newBuilder()
                    .body(OkHttpProgressResponseBody(request.url(), response.body(), listener))
                    .build()
            }
        }

        @JvmStatic
        fun forget(url: String) {
            DispatchingProgressListener.forget(url)
        }

        @JvmStatic
        fun expect(url: String, listener: UIProgressListener) {
            DispatchingProgressListener.expect(url, listener)
        }
    }
}