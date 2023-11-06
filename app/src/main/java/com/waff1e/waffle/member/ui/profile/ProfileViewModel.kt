package com.waff1e.waffle.member.ui.profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waff1e.waffle.dto.ResponseResult
import com.waff1e.waffle.member.data.MemberRepository
import com.waff1e.waffle.member.dto.Member
import com.waff1e.waffle.member.dto.UpdateProfileRequest
import com.waff1e.waffle.waffle.dto.WaffleListFailResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val memberRepository: MemberRepository
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
            val body = Json.decodeFromString<WaffleListFailResponse>(
                responseResult.errorBody()?.string()!!
            )

            myProfile.copy(errorCode = body.errorCode)
        }

        Log.d("로그", "ProfileViewModel - getMyProfile() 호출됨 - ${myProfile.member}")
    }
    
    // TODO. 프로필 이미지 업데이트 기능 수정 필요
    suspend fun updateMyProfileImage() {
        val file = File("1234")
//        val requestFile = file.asRequestBody()
        val requestFile = file.toString().toRequestBody("image/*".toMediaType())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        memberRepository.updateMyProfileImage(body)
    }
}