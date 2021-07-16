package com.drelovey.realize.ui.learn.view

import com.alibaba.android.arouter.facade.annotation.Route
import com.drelovey.common.base.activity.BaseActivity
import com.drelovey.realize.R
import com.drelovey.provider.router.RouterPath
import com.drelovey.realize.databinding.ActivityMeasureLayoutBinding
import com.drelovey.realize.ui.learn.model.NoViewModel
import dagger.hilt.android.AndroidEntryPoint

@Suppress("RedundantOverride")
/**
 * 测量布局流程
 */
@Route(path = RouterPath.PATH_MEASURE_LAYOUT)
@AndroidEntryPoint
class MeasureLayoutActivity :
    BaseActivity<ActivityMeasureLayoutBinding, NoViewModel>(R.layout.activity_measure_layout) {

    override fun initialization() {

    }
}