package com.shimhg02.solorestorant.Test.TestUtil

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.shimhg02.solorestorant.R
import java.util.*

class StoryStatusView : LinearLayout {
    private val progressBars: MutableList<ProgressBar> =
        ArrayList()
    private val animators: MutableList<ObjectAnimator> =
        ArrayList()
    private var storiesCount = -1
    private var current = 0
    private var userInteractionListener: UserInteractionListener? = null
    var isReverse = false
    var isComplete = false

    interface UserInteractionListener {
        fun onNext()
        fun onPrev()
        fun onComplete()
    }

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    private fun bindViews() {
        removeAllViews()
        for (i in 0 until storiesCount) {
            val p = createProgressBar()
            p.max = MAX_PROGRESS
            progressBars.add(p)
            addView(p)
            if (i + 1 < storiesCount) {
                addView(createSpace())
            }
        }
    }

    private fun createProgressBar(): ProgressBar {
        val p =
            ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal)
        p.layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1F)
        p.progressDrawable = ContextCompat.getDrawable(context, R.drawable.progress_bg)
        return p
    }

    private fun createSpace(): View {
        val v = View(context)
        v.layoutParams = LayoutParams(
            SPACE_BETWEEN_PROGRESS_BARS,
            LayoutParams.WRAP_CONTENT
        )
        return v
    }

    fun setStoriesCount(storiesCount: Int) {
        this.storiesCount = storiesCount
        bindViews()
    }

    fun setUserInteractionListener(userInteractionListener: UserInteractionListener?) {
        this.userInteractionListener = userInteractionListener
    }

    fun skip() {
        if (isComplete) return
        val p = progressBars[current]
        p.progress = p.max
        animators[current].cancel()
    }

    fun pause() {
        if (isComplete) return
        val p = progressBars[current]
        p.progress = p.progress
        animators[current].pause()
    }

    fun resume() {
        if (isComplete) return
        val p = progressBars[current]
        p.progress = p.progress
        animators[current].resume()
    }

    fun reverse() {
        if (isComplete) return
        var p = progressBars[current]
        p.progress = 0
        isReverse = true
        animators[current].cancel()
        if (0 <= current - 1) {
            p = progressBars[current - 1]
            p.progress = 0
            animators[--current].start()
        } else {
            animators[current].start()
        }
    }

    fun setStoryDuration(duration: Long) {
        animators.clear()
        for (i in progressBars.indices) {
            animators.add(createAnimator(i, duration))
        }
    }

    fun setStoriesCountWithDurations(durations: LongArray) {
        storiesCount = durations.size
        bindViews()
        animators.clear()
        for (i in progressBars.indices) {
            animators.add(createAnimator(i, durations[i]))
        }
    }

    fun playStories() {
        animators[0].start()
    }

    /**
     * Need to call when Activity or Fragment destroy
     */
    fun destroy() {
        for (a in animators) {
            a.removeAllListeners()
            a.cancel()
        }
    }

    private fun createAnimator(index: Int, duration: Long): ObjectAnimator {
        val animation = ObjectAnimator.ofInt(
            progressBars[index],
            "progress",
            MAX_PROGRESS
        )
        animation.interpolator = LinearInterpolator()
        animation.duration = duration
        animation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                current = index
            }

            override fun onAnimationEnd(animation: Animator) {
                if (isReverse) {
                    isReverse = false
                    if (userInteractionListener != null) userInteractionListener!!.onPrev()
                    return
                }
                val next = current + 1
                if (next <= animators.size - 1) {
                    if (userInteractionListener != null) userInteractionListener!!.onNext()
                    animators[next].start()
                } else {
                    isComplete = true
                    if (userInteractionListener != null) userInteractionListener!!.onComplete()
                }
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        return animation
    }

    companion object {
        private const val MAX_PROGRESS = 100
        private const val SPACE_BETWEEN_PROGRESS_BARS = 5
    }
}