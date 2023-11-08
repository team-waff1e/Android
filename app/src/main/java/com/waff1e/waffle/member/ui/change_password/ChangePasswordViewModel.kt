package com.waff1e.waffle.member.ui.change_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waff1e.waffle.member.data.MemberRepository
import com.waff1e.waffle.member.dto.Member
import com.waff1e.waffle.member.ui.UpdateProfileUiState
import com.waff1e.waffle.member.ui.profile.ProfileUiState
import com.waff1e.waffle.waffle.dto.WaffleListFailResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
) : ViewModel() {
    var updateProfileUiState by mutableStateOf(UpdateProfileUiState())

    init {
        viewModelScope.launch {
            getMyProfile()
        }
    }

    private suspend fun getMyProfile() {
        val responseResult: Response<Member> = memberRepository.getMyProfile()
        var myProfile by mutableStateOf(ProfileUiState())

        myProfile = if (responseResult.isSuccessful) {
            myProfile.copy(member = responseResult.body()!!)
        } else {
            val body = Json.decodeFromString<WaffleListFailResponse>(
                responseResult.errorBody()?.string()!!
            )

            myProfile.copy(errorCode = body.errorCode)
        }

        updateProfileUiState = updateProfileUiState.copy(
            nickname = myProfile.member?.nickname ?: ""
        )
    }

    suspend fun requestUpdateProfile() {

    }
}
