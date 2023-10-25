package com.waff1e.waffle.auth.data

import com.waff1e.waffle.auth.dto.CheckEmail
import com.waff1e.waffle.auth.dto.CheckNickName
import com.waff1e.waffle.dto.DefaultResponse
import com.waff1e.waffle.auth.dto.Signup
import com.waff1e.waffle.auth.network.AuthService
import kotlinx.serialization.json.JsonElement
import retrofit2.Response
import javax.inject.Inject

// TODO. 추후에 기능에 따라 Repository 분리 예정
interface AuthRepository {
     suspend fun login(jsonLogin: JsonElement): Response<DefaultResponse>

     suspend fun signup(signup: Signup): Response<DefaultResponse>

     suspend fun checkEmailStream(email: CheckEmail): Response<DefaultResponse>

     suspend fun checkNickNameStream(nickname: CheckNickName): Response<DefaultResponse>
}

class DefaultAuthRepository @Inject constructor(
     private val authService: AuthService,
) : AuthRepository {
     override suspend fun login(jsonLogin: JsonElement): Response<DefaultResponse> {
          return authService.requestLogin(jsonLogin)
     }

     override suspend fun signup(signup: Signup): Response<DefaultResponse> {
          return authService.requestSignup(signup)
     }

     override suspend fun checkEmailStream(email: CheckEmail): Response<DefaultResponse> {
          return authService.checkEmail(email)
     }

     override suspend fun checkNickNameStream(nickname: CheckNickName): Response<DefaultResponse> {
          return authService.checkNickname(nickname)
     }
}