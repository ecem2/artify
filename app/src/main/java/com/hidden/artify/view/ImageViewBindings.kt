package com.hidden.artify.view

import androidx.databinding.BindingAdapter
import com.hidden.artify.core.common.ImageManager
import com.makeramen.roundedimageview.RoundedImageView

@BindingAdapter("imageUrl")
fun RoundedImageView.setImageUrl(url: Int) {
    ImageManager.setImageUrl(url, this)
}