package com.waff1e.waffle.member.ui.profile

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waff1e.waffle.di.LIMIT
import com.waff1e.waffle.dto.DefaultResponse
import com.waff1e.waffle.dto.check
import com.waff1e.waffle.member.data.MemberRepository
import com.waff1e.waffle.member.dto.Member
import com.waff1e.waffle.ui.navigation.NavigationDestination
import com.waff1e.waffle.utils.removeWaffle
import com.waff1e.waffle.waffle.data.WaffleRepository
import com.waff1e.waffle.waffle.dto.WaffleListSuccessResponse
import com.waff1e.waffle.waffle.ui.waffles.WaffleListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val memberRepository: MemberRepository,
    private val waffleRepository: WaffleRepository
) : ViewModel() {
    private val memberString: String? = savedStateHandle[NavigationDestination.Profile.MEMBER_ID]
    private val memberId: Long? = memberString?.toLong()

    var profile by mutableStateOf(ProfileUiState())
    var waffleListUiState by mutableStateOf(WaffleListUiState())
    private val idx: MutableState<Long?> = mutableStateOf(null)

    init {
        viewModelScope.launch {
            Log.d("로그", "$memberId")
            if (memberId == null) getMyProfile() else getProfileByMemberId()
            getWaffleListByMemberId(true)
        }
    }

    private suspend fun getMyProfile() {
        val responseResult: Response<Member> = memberRepository.getMyProfile()

        profile = if (responseResult.isSuccessful) {
            profile.copy(member = responseResult.body()!!)
        } else {
            val body = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )

            profile.copy(errorCode = body.errorCode)
        }
    }

    private suspend fun getProfileByMemberId() {
        val responseResult: Response<Member> = memberRepository.getProfileById(memberId!!)

        profile = if (responseResult.isSuccessful) {
            profile.copy(member = responseResult.body()!!)
        } else {
            val body = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )

            profile.copy(errorCode = body.errorCode)
        }
    }

    suspend fun getWaffleListByMemberId(isUpdate: Boolean) {
        val responseResult: Response<WaffleListSuccessResponse>? = if (isUpdate) {
            waffleRepository.requestWaffleListByMemberId(memberId, LIMIT, true, null)
        } else if (idx.value != null) {
            waffleRepository.requestWaffleListByMemberId(memberId, LIMIT, false, idx.value)
        } else {
            null
        }

        if (responseResult != null && responseResult.isSuccessful) {
            waffleListUiState = if (isUpdate) {
                waffleListUiState.copy(waffleList = responseResult.body()!!.contents)
            } else {
                val newSet = waffleListUiState.waffleList.toMutableSet()
                newSet.addAll(responseResult.body()!!.contents)
                val newList = newSet.sortedByDescending { it.updatedAt }
                waffleListUiState.copy(waffleList = newList)
            }

            idx.value = responseResult.body()!!.lastIdx
        } else if (responseResult != null) {
            val body = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )
            waffleListUiState = waffleListUiState.copy(errorCode = body.errorCode)
        }
    }


    suspend fun removeWaffle(id: Long) {
        waffleListUiState = waffleListUiState.copy(waffleList = waffleListUiState.waffleList.removeWaffle(id))

        val responseResult = waffleRepository.deleteWaffle(id).check()

        if (!responseResult.isSuccess) {
            waffleListUiState = waffleListUiState.copy(errorCode = responseResult.body!!.errorCode)
        }
    }

    // TODO. 프로필 이미지 업데이트 기능 수정 필요
    suspend fun updateMyProfileImage() {
        val file = File("1234")
//        val requestFile = file.asRequestBody()
        val requestFile = file.toString().toRequestBody("image/*".toMediaType())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        memberRepository.updateMyProfileImage(body)
    }

    suspend fun requestWaffleLike(id: Long) {
        // TODO. 포스트맨으로만 테스트하면 에러남
//        val idx = waffleListUiState.waffleList.indexOfFirst { it.id == id }
//        val responseResult = if (waffleListUiState.waffleList[idx].liked) {
//            waffleRepository.likeWaffle(id)
//        } else {
//            waffleRepository.unlikeWaffle(id)
//        }
//
//        if (responseResult.isSuccessful) {
//            val waffle = responseResult.body()!!
//            waffleListUiState.waffleList[idx] = waffle
//        } else {
//            val body = Json.decodeFromString<WaffleListFailResponse>(
//                responseResult.errorBody()?.string()!!
//            )
//            waffleListUiState = waffleListUiState.copy(errorCode = body.errorCode)
//        }
    }
}