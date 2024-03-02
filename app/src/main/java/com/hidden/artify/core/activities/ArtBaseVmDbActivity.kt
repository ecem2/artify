package com.hidden.artify.core.activities

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.hidden.artify.core.common.ViewBindingUtil
import com.hidden.artify.core.viewmodel.ArtBaseViewModel
import com.hidden.artify.databinding.ActivityArtBaseBinding

abstract class ArtBaseVmDbActivity<VM : ArtBaseViewModel, DB : ViewDataBinding> :
    ArtBaseVmActivity<VM>() {

    protected lateinit var viewBinding: DB

    abstract fun viewDataBindingClass(): Class<DB>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = setResourceViewBinding()
        setContentView(view)
        viewBinding.lifecycleOwner = this
        onInitDataBinding()
    }

    open fun setResourceViewBinding(): View {
        val baseViewBinding = ViewBindingUtil.inflate<ActivityArtBaseBinding>(layoutInflater)
        viewBinding = ViewBindingUtil.inflate(
            layoutInflater,
            baseViewBinding.baseArtContentFrame,
            true,
            viewDataBindingClass()
        )
        return baseViewBinding.root
    }

    abstract fun onInitDataBinding()
}