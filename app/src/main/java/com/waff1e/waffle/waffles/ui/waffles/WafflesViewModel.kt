package com.waff1e.waffle.waffles.ui.waffles

import androidx.lifecycle.ViewModel
import com.waff1e.waffle.waffles.data.WafflesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WafflesViewModel @Inject constructor(
    private val wafflesRepository: WafflesRepository
) : ViewModel() {

}