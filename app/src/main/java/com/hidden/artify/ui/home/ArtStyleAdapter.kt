package com.hidden.artify.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hidden.artify.R
import com.hidden.artify.core.recyclerview.ArtListAdapter
import com.hidden.artify.data.model.ArtStyleModel
import com.hidden.artify.data.model.SizeModel

class ArtStyleAdapter : ArtListAdapter<ArtStyleModel>(
    itemsSame = { old, new -> old == new },
    contentsSame = { old, new -> old == new }
) {

    private var selectedPosition: Int = 0
    private var itemSelectedListener: OnArtItemSelectedListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return ArtStyleViewHolder(parent, inflater)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ArtStyleViewHolder) {
            val item = getItem(position)
            holder.bind(item, position == selectedPosition)
            holder.itemView.setOnClickListener {
                if (selectedPosition != position) {
                    val previouslySelectedPosition = selectedPosition
                    selectedPosition = position
                    updateArtItemsSelection(item)
                    notifyItemChanged(previouslySelectedPosition)
                    notifyItemChanged(position)
                    itemSelectedListener?.onArtItemSelected(item)
                } else {
                    return@setOnClickListener
                }

                //item.isSelected = !item.isSelected
                holder.binding.apply {
                    if (item.isSelected) {
                        tvItemName.setTextColor(
                            ContextCompat.getColor(
                                tvItemName.context,
                                R.color.white
                            )
                        )
                        llSelectedBg.visibility = View.VISIBLE
                    } else {
                        tvItemName.setTextColor(
                            ContextCompat.getColor(
                                tvItemName.context,
                                R.color.white
                            )
                        )
                        llSelectedBg.visibility = View.GONE
                    }
                }
                notifyItemChanged(position)
            }
        }
    }

    private fun updateArtItemsSelection(clickedItem: ArtStyleModel) {
        for (i in 0 until itemCount) {
            val item = getItem(i)
            item.isSelected = item == clickedItem
        }
    }

    fun setOnArtItemSelectedListener(listener: OnArtItemSelectedListener) {
        this.itemSelectedListener = listener
    }

    interface OnArtItemSelectedListener {
        fun onArtItemSelected(item: ArtStyleModel)
    }
}