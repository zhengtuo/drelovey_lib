package com.drelovey.common.base.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Message
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.common.base.activity.BaseActivityDelegate
import com.drelovey.common.base.viewmodel.BaseViewModel
import com.drelovey.common.data.model.Resource
import com.drelovey.common.utils.observe
import com.drelovey.common.utils.viewModelsByVM
import com.skydoves.bindables.BindingActivity


abstract class BaseActivity<T : ViewDataBinding, VM : BaseViewModel> constructor(
    @LayoutRes private val contentLayoutId: Int,
) : BindingActivity<T>(contentLayoutId) {

    @Suppress("UNCHECKED_CAST")
    val mViewModel: VM by viewModelsByVM()


    protected lateinit var mContext: Context
    protected lateinit var mActivity: Activity
    private val mActivityDelegate by lazy {
        BaseActivityDelegate(
            mActivity
        )
    }

    protected var isFirstVisible = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = applicationContext
        mActivity = this
        initialization()
        setupListener()
        observeViewModel()
        initSingleEvent()
        initData()
    }
    //初始化方法
    abstract fun initialization()

    /**
     * 事情监听器
     */
    protected open fun setupListener() {

    }

    protected open fun observeViewModel() {
        observe(mViewModel.dataLiveData, ::handleData)
    }

    open fun handleData(resource: Resource<*>) {

    }

    /**
     * 初始化公共消息事件
     */
    protected fun initSingleEvent() {
        registerFinishEvent()
    }

    /**
     * 初始化数据
     */
    protected fun initData() {
        mViewModel.initData()
    }

    /**
     * 注册单个消息事件，消息对象:[Message]
     */
    protected open fun registerSingleLiveEvent(observer: Observer<Message?>) {
        mViewModel.getSingleLiveEvent().observe(this, observer)
    }

    open fun loadData() {

    }

    /**
     * 关闭当前activity事件
     */
    protected open fun registerFinishEvent() {
        mViewModel.getFinishEvent().observe(this, Observer { finish() })
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        if (!isFirstVisible) {
            loadData()
        }
        isFirstVisible = false
    }

    override fun onDestroy() {
        mActivityDelegate.onDestroy()
        super.onDestroy()
    }

}