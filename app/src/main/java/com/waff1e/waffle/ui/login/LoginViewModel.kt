package com.waff1e.waffle.ui.login

import androidx.lifecycle.ViewModel
import com.waff1e.waffle.data.WaffleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val waffleRepository: WaffleRepository
) : ViewModel() {

}