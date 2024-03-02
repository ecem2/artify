package com.hidden.artify.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RequestModel(
    val prompt: String,
    val count: Int,
    val size: String
) : Parcelable