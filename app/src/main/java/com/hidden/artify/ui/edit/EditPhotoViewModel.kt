package com.hidden.artify.ui.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hidden.artify.core.common.Resource
import com.hidden.artify.core.viewmodel.BaseViewModel
import com.hidden.artify.data.model.EditResponse
import com.hidden.artify.data.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditPhotoViewModel @Inject constructor(
    private val repository: ImageRepository
) : BaseViewModel() {

    private val _editedImageResponse = MutableLiveData<Resource<EditResponse>>()
    val editedImageResponse: LiveData<Resource<EditResponse>> = _editedImageResponse

    fun uploadImage(file: File, mask: File, prompt: String, n: Int, size: String) = viewModelScope.launch {
        _editedImageResponse.value = Resource.loading(null)
        viewModelScope.launch {
            val response = repository.loadImage(file, mask, prompt, n, size)
            _editedImageResponse.postValue(response)
        }
    }
}