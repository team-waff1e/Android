package com.waff1e.waffle.waffle.ui.waffles

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waff1e.waffle.auth.data.AuthRepository
import com.waff1e.waffle.di.LIMIT
import com.waff1e.waffle.di.LoginUser
import com.waff1e.waffle.di.LoginUserPreferenceModule
import com.waff1e.waffle.dto.DefaultResponse
import com.waff1e.waffle.dto.check
import com.waff1e.waffle.member.data.MemberRepository
import com.waff1e.waffle.member.dto.Member
import com.waff1e.waffle.member.ui.profile.ProfileUiState
import com.waff1e.waffle.utils.removeWaffle
import com.waff1e.waffle.waffle.data.WaffleRepository
import com.waff1e.waffle.waffle.dto.WaffleListSuccessResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class WaffleListViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val waffleRepository: WaffleRepository,
    private val memberRepository: MemberRepository,
    private val loginUserPreference: LoginUserPreferenceModule,
) : ViewModel() {
    var myProfile by mutableStateOf(ProfileUiState())
    var waffleListUiState by mutableStateOf(WaffleListUiState())
    private val idx: MutableState<Long?> = mutableStateOf(null)
    private var isLast = false

    init {
        viewModelScope.launch {
            getMyProfile()
            getWaffleList(true)
        }
    }

    private suspend fun getMyProfile() {
        val responseResult: Response<Member> = memberRepository.getMyProfile()

        if (responseResult.isSuccessful) {
            myProfile = myProfile.copy(member = responseResult.body()!!)

            LoginUser.email = myProfile.member?.email ?: ""
            LoginUser.nickname = myProfile.member?.nickname ?: ""
            LoginUser.profileUrl = myProfile.member?.profileUrl ?: ""
        } else {
            val body = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )

            myProfile = myProfile.copy(errorCode = body.errorCode)
        }
    }

    suspend fun getWaffleList(isUpdate: Boolean) {
        val responseResult: Response<WaffleListSuccessResponse>? = if (isUpdate) {
            waffleRepository.requestWaffleList(LIMIT, true, null)
        } else if (idx.value != null && !isLast) {
            waffleRepository.requestWaffleList(LIMIT, false, idx.value)
        } else {
            null
        }

        if (responseResult != null && responseResult.isSuccessful) {
            waffleListUiState = if (isUpdate) {
                waffleListUiState.copy(waffleList = responseResult.body()!!.contents)
            } else {
                val newSet = waffleListUiState.waffleList.toMutableSet()
                newSet.addAll(responseResult.body()!!.contents)
                val newList = newSet.sortedByDescending { it.updatedAt }.toMutableList()
                waffleListUiState.copy(waffleList = newList)
            }

            idx.value = responseResult.body()!!.lastIdx
            isLast = responseResult.body()!!.last
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

    suspend fun requestWaffleLike(id: Long) {
        // TODO. 포스트맨으로만 테스트하면 에러남
        val idx = waffleListUiState.waffleList.indexOfFirst { it.id == id }

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

    suspend fun logout() {
        loginUserPreference.removeJSESSIONID()
        authRepository.logout()
    }
}