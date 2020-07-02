package com.shimhg02.solorestorant.Test.ImageEditor.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


abstract class BaseFragment : Fragment() {
    protected abstract val layoutId: Int
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        require(layoutId != 0) { "Invalid layout id" }
        return inflater.inflate(layoutId, container, false)
    }
}