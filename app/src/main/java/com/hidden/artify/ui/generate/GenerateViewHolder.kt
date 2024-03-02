package com.hidden.artify.ui.generate

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hidden.artify.core.adapters.ArtBaseViewHolder
import com.hidden.artify.data.model.GenerateModel
import com.hidden.artify.databinding.ItemGenerateBinding
import com.hidden.artify.extensions.executeAfter

class GenerateViewHolder(
    parent: ViewGroup, inflater: LayoutInflater
) : ArtBaseViewHolder<ItemGenerateBinding>(
    binding = ItemGenerateBinding.inflate(inflater, parent, false)
) {

    fun bind(
        item: GenerateModel,
        onItemClicked: ((item: GenerateModel, position: Int) -> Unit)? = null
    ) {
        binding.executeAfter {
            this.item = item
            root.setOnClickListener {
                onItemClicked?.invoke(item, adapterPosition)
            }
        }
    }
}