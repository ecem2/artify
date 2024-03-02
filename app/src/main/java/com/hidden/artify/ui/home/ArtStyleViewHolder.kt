package com.hidden.artify.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.hidden.artify.R
import com.hidden.artify.core.adapters.ArtBaseViewHolder
import com.hidden.artify.data.model.ArtStyleModel
import com.hidden.artify.databinding.ItemArtStyleBinding
import com.hidden.artify.extensions.executeAfter

class ArtStyleViewHolder(
    parent: ViewGroup, inflater: LayoutInflater
) : ArtBaseViewHolder<ItemArtStyleBinding>(
    binding = ItemArtStyleBinding.inflate(inflater, parent, false)
) {

    fun bind(item: ArtStyleModel, isSelected: Boolean) {
        binding.executeAfter {
            this.item = item
            if (isSelected) {
                tvItemName.setTextColor(ContextCompat.getColor(tvItemName.context, R.color.white))
                llSelectedBg.visibility = View.VISIBLE
            } else {
                tvItemName.setTextColor(ContextCompat.getColor(tvItemName.context, R.color.white))
                llSelectedBg.visibility = View.GONE
            }
        }
    }
}