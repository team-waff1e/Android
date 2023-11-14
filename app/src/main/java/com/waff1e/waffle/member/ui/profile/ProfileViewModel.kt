package com.waff1e.waffle.member.ui.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waff1e.waffle.di.LIMIT
import com.waff1e.waffle.dto.DefaultResponse
import com.waff1e.waffle.member.data.MemberRepository
import com.waff1e.waffle.member.dto.Member
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
    private val memberRepository: MemberRepository,
    private val waffleRepository: WaffleRepository
) : ViewModel() {
    var myProfile by mutableStateOf(ProfileUiState())
    var myWaffleListUiState by mutableStateOf(WaffleListUiState())
    private val idx: MutableState<Long?> = mutableStateOf(null)

    init {
        viewModelScope.launch {
            getMyProfile()
            getMyWaffleList(true)
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

    suspend fun getMyWaffleList(isUpdate: Boolean) {
//        val responseResult: Response<WaffleListSuccessResponse>? = if (isUpdate) {
//            waffleRepository.requestWaffleList(LIMIT, true, null)
//        } else if (idx.value != null) {
//            waffleRepository.requestWaffleList(LIMIT, false, idx.value)
//        } else {
//            null
//        }

        val responseResult: Response<WaffleListSuccessResponse>? = if (isUpdate) {
            waffleRepository.requestWaffleListByMemberId(null, LIMIT, true, null)
        } else if (idx.value != null) {
            waffleRepository.requestWaffleListByMemberId(null, LIMIT, false, idx.value)
        } else {
            null
        }

        if (responseResult != null && responseResult.isSuccessful) {
            myWaffleListUiState = if (isUpdate) {
                myWaffleListUiState.copy(waffleList = responseResult.body()!!.contents)
            } else {
                // TODO. 무한 스크롤 시연을 위해 아래 코드 주석 처리, 실제로는 MutableSet 사용 필요
//                val newSet = waffleListUiState.value.waffleList.toMutableSet()
//                newSet.addAll(responseResult.body()!!.list)
//                val newList = newSet.sortedByDescending { it.updatedAt }
                val newList = myWaffleListUiState.waffleList.toMutableList()
                newList.addAll(responseResult.body()!!.contents)
                newList.sortedByDescending { it.updatedAt }
                myWaffleListUiState.copy(waffleList = newList)
            }

            idx.value = responseResult.body()!!.lastIdx
        } else if (responseResult != null) {
            val body = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )
            myWaffleListUiState = myWaffleListUiState.copy(errorCode = body.errorCode)
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
//        val responseResult = waffleRepository.likeWaffle(id)
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