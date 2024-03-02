package com.hidden.artify.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.hidden.artify.R
import com.hidden.artify.core.adapters.ArtBaseViewHolder
import com.hidden.artify.data.model.SizeModel
import com.hidden.artify.databinding.ItemSizeBinding
import com.hidden.artify.extensions.executeAfter

class SizeViewHolder(
    parent: ViewGroup, inflater: LayoutInflater
) : ArtBaseViewHolder<ItemSizeBinding>(
    binding = ItemSizeBinding.inflate(inflater, parent, false)
) {
    fun bind(item: SizeModel, isSelected: Boolean) {
        binding.executeAfter {
            this.item = item
            if (isSelected) {
                clSize.setBackgroundResource(R.drawable.bg_size)
                tvItemText.setTextColor(ContextCompat.getColor(tvItemText.context, R.color.white))
            } else {
                clSize.setBackgroundResource(R.drawable.bg_selected_size)
                tvItemText.setTextColor(ContextCompat.getColor(tvItemText.context, R.color.purple))
            }
        }
    }
}