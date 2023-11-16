package com.waff1e.waffle.member.ui.edit_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waff1e.waffle.auth.data.AuthRepository
import com.waff1e.waffle.di.LoginUserPreferenceModule
import com.waff1e.waffle.dto.ResponseResult
import com.waff1e.waffle.dto.check
import com.waff1e.waffle.member.data.MemberRepository
import com.waff1e.waffle.member.dto.PasswordRequest
import com.waff1e.waffle.member.network.MemberService
import com.waff1e.waffle.member.ui.change_password.toUpdatePasswordRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val memberRepository: MemberRepository,
    private val loginUserPreference: LoginUserPreferenceModule,
) : ViewModel() {
    var pwd by mutableStateOf("")

    fun updatePwd(newInput: String) {
        pwd = newInput
    }

    suspend fun requestDeleteMyProfile(): ResponseResult {
        val passwordRequest = PasswordRequest(pwd)
        val checkPasswordResponseResult = memberRepository.checkPassword(passwordRequest).check()

        return if (!checkPasswordResponseResult.isSuccess) {
            checkPasswordResponseResult
        } else {
            memberRepository.deleteMyProfile(passwordRequest).check()
        }
    }

    suspend fun logout() {
        loginUserPreference.removeJSESSIONID()
        authRepository.logout()
    }
}