package com.shimhg02.solorestorant.ui.Activity.ImageViewer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.shimhg02.solorestorant.R
import kotlinx.android.synthetic.main.activity_image_viewer.*


class ImageViewrActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)
        Glide.with(this).load(intent.getStringExtra("imageView")).into(image_view)
        out_btn.setOnClickListener {
            finish()
        }
    }
}