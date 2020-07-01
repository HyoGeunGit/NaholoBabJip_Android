package com.shimhg02.solorestorant.utils.Bases



import android.animation.ValueAnimator
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

/**
 * @description 베이스 액티비티
 */


abstract class BaseActivity : AppCompatActivity() {
    var instance: BaseActivity? = null
    var mToolbarHeight = 0
    var mAnimDuration = 0
    private var mVaActionBar: ValueAnimator? = null
    protected abstract var viewId: Int

    protected abstract fun onCreate()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        setContentView(viewId)
        onCreate()

    }


    fun disableToggle() {
        this.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
    fun enableToggle() {
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    fun setToolbarTitle(titleStr: String) {
        this.supportActionBar?.title = titleStr
    }
    fun setToolbarTitle(titleId: Int) {
        this.supportActionBar?.title = getString(titleId)
    }
    fun setToolbarIcon(iconRes: Int) {
        this.supportActionBar?.setHomeAsUpIndicator(iconRes)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}