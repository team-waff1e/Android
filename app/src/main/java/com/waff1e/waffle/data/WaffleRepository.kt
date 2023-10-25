package com.waff1e.waffle.data

import com.waff1e.waffle.dto.CheckEmail
import com.waff1e.waffle.dto.CheckNickName
import com.waff1e.waffle.dto.DefaultResponse
import com.waff1e.waffle.dto.Signup
import com.waff1e.waffle.network.WaffleService
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import retrofit2.Response
import javax.inject.Inject

// TODO. 추후에 기능에 따라 Repository 분리 예정
interface WaffleRepository {
     suspend fun login(jsonLogin: JsonElement): Response<DefaultResponse>

     suspend fun signup(signup: Signup): Response<DefaultResponse>

     suspend fun checkEmailStream(email: CheckEmail): Response<DefaultResponse>

     suspend fun checkNickNameStream(nickname: CheckNickName): Response<DefaultResponse>
}

class DefaultWaffleRepository @Inject constructor(
     private val waffleService: WaffleService,
) : WaffleRepository {
     override suspend fun login(jsonLogin: JsonElement): Response<DefaultResponse> {
          return waffleService.requestLogin(jsonLogin)
     }

     override suspend fun signup(signup: Signup): Response<DefaultResponse> {
          return waffleService.requestSignup(signup)
     }

     override suspend fun checkEmailStream(email: CheckEmail): Response<DefaultResponse> {
          return waffleService.checkEmail(email)
     }

     override suspend fun checkNickNameStream(nickname: CheckNickName): Response<DefaultResponse> {
          return waffleService.checkNickname(nickname)
     }
}