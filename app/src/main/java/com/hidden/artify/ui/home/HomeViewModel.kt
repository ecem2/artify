package com.hidden.artify.ui.home

import com.hidden.artify.R
import com.hidden.artify.core.viewmodel.BaseViewModel
import com.hidden.artify.data.model.ArtStyleModel
import com.hidden.artify.data.model.GenerateModel
import com.hidden.artify.data.model.SizeModel
import com.hidden.artify.data.preferences.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val preferences: Preferences
) : BaseViewModel() {

    val itemList: ArrayList<SizeModel> = ArrayList()
    val artList: ArrayList<ArtStyleModel> = ArrayList()
    val imageList: ArrayList<GenerateModel> = ArrayList()

    init {
        getSizeList()
        getArtList()
        getAnimationList()
    }

    private fun getArtList() {
        artList.add(
            ArtStyleModel(
                name = "Hyper realistic",
                filter = R.drawable.img_digital_art,
                isSelected = true
            )
        )
        artList.add(
            ArtStyleModel(
                name = "Photographic",
                filter = R.drawable.img_realistic,
                isSelected = false
            )
        )
        artList.add(
            ArtStyleModel(
                name = "3D Rendering",
                filter = R.mipmap.img_futuristic,
                isSelected = false
            )
        )
        artList.add(
            ArtStyleModel(
                name = "Neon",
                filter = R.drawable.img_scolastic,
                isSelected = false
            )
        )
        artList.add(
            ArtStyleModel(
                name = "Futuristic",
                filter = R.mipmap.img_futuristic,
                isSelected = false
            )
        )
        artList.add(
            ArtStyleModel(
                name = "Cyberpunk",
                filter = R.drawable.img_scolastic,
                isSelected = false
            )
        )
        artList.add(
            ArtStyleModel(
                name = "Colorful",
                filter = R.mipmap.img_futuristic,
                isSelected = false
            )
        )
        artList.add(
            ArtStyleModel(
                name = "Anime",
                filter = R.drawable.img_scolastic,
                isSelected = false
            )
        )
        artList.add(
            ArtStyleModel(
                name = "Cartoon",
                filter = R.drawable.img_scolastic,
                isSelected = false
            )
        )
        artList.add(
            ArtStyleModel(
                name = "Pop Art",
                filter = R.drawable.img_digital_art,
                isSelected = false
            )
        )
        artList.add(
            ArtStyleModel(
                name = "Oil Painting",
                filter = R.drawable.img_digital_art, // todo
                isSelected = false
            )
        )

        artList.add(
            ArtStyleModel(
                name = "Mosaic",
                filter = R.drawable.img_digital_art, // todo
                isSelected = false
            )
        )
    }

    private fun getSizeList() {
        itemList.add(SizeModel(icon = "", size = "1024x1024", isSelected = true))
        itemList.add(SizeModel(icon = "", size = "1024x1792", isSelected = false))
        itemList.add(SizeModel(icon = "", size = "1792x1024", isSelected = false))
    }

    private fun getAnimationList() {
        imageList.add(GenerateModel(image = "", animation = R.raw.loading))
        imageList.add(GenerateModel(image = "", animation = R.raw.loading))
        imageList.add(GenerateModel(image = "", animation = R.raw.loading))
        imageList.add(GenerateModel(image = "", animation = R.raw.loading))
    }
}