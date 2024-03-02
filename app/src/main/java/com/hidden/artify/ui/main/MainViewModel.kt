package com.hidden.artify.ui.main

import com.hidden.artify.core.viewmodel.BaseViewModel
import com.hidden.artify.data.preferences.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val preferences: Preferences
) : BaseViewModel() {
}