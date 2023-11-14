package com.waff1e.waffle.member.ui.profile_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waff1e.waffle.dto.DefaultResponse
import com.waff1e.waffle.member.data.MemberRepository
import com.waff1e.waffle.member.dto.Member
import com.waff1e.waffle.member.ui.profile.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
) : ViewModel() {
    var myProfile by mutableStateOf(ProfileUiState())

    init {
        viewModelScope.launch {
            getMyProfile()
        }
    }

    private suspend fun getMyProfile() {
        val responseResult: Response<Member> = memberRepository.getMyProfile()

        myProfile = if (responseResult.isSuccessful) {
            myProfile.copy(member = responseResult.body()!!)
        } else {
            val body = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )

            myProfile.copy(errorCode = body.errorCode)
        }
    }
}