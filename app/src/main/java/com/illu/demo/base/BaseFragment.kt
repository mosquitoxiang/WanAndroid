package com.illu.demo.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hjq.bar.OnTitleBarListener

abstract class BaseFragment : Fragment(), OnTitleBarListener{

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    abstract fun getLayoutId(): Int
    abstract fun initView()
    open fun initData() {}

    override fun onLeftClick(v: View?) {

    }

    override fun onTitleClick(v: View?) {

    }

    override fun onRightClick(v: View?) {

    }
}