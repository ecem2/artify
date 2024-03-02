package com.hidden.artify.core.activities

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.hidden.artify.core.common.ViewBindingUtil
import com.hidden.artify.core.viewmodel.BaseViewModel
import com.hidden.artify.databinding.ActivityBaseBinding

abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> :
    ArtBaseVmDbActivity<VM, DB>() {

    private lateinit var baseViewBinding: ViewDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setResourceViewBinding(): View {
        baseViewBinding =
            ViewBindingUtil.inflate<ActivityBaseBinding>(layoutInflater)
        viewBinding = ViewBindingUtil.inflate(
            layoutInflater,
            (baseViewBinding as ActivityBaseBinding).baseContentFrame,
            true,
            viewDataBindingClass()
        )
        viewBinding.lifecycleOwner = this
        return baseViewBinding.root
    }
}