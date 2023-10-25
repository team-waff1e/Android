package com.waff1e.waffle.auth.data

import com.waff1e.waffle.auth.dto.CheckEmailRequest
import com.waff1e.waffle.auth.dto.CheckNickNameRequest
import com.waff1e.waffle.auth.dto.LoginRequest
import com.waff1e.waffle.dto.DefaultResponse
import com.waff1e.waffle.auth.dto.SignupRequest
import com.waff1e.waffle.auth.network.AuthService
import retrofit2.Response
import javax.inject.Inject

// TODO. 추후에 기능에 따라 Repository 분리 예정
interface AuthRepository {
     suspend fun login(loginRequest: LoginRequest): Response<DefaultResponse>

     suspend fun signup(signupRequest: SignupRequest): Response<DefaultResponse>

     suspend fun checkEmailStream(email: CheckEmailRequest): Response<DefaultResponse>

     suspend fun checkNickNameStream(nickname: CheckNickNameRequest): Response<DefaultResponse>
}

class DefaultAuthRepository @Inject constructor(
     private val authService: AuthService,
) : AuthRepository {
     override suspend fun login(loginRequest: LoginRequest): Response<DefaultResponse> {
          return authService.requestLogin(loginRequest)
     }


     override suspend fun signup(signupRequest: SignupRequest): Response<DefaultResponse> {
          return authService.requestSignup(signupRequest)
     }

     override suspend fun checkEmailStream(email: CheckEmailRequest): Response<DefaultResponse> {
          return authService.checkEmail(email)
     }

     override suspend fun checkNickNameStream(nickname: CheckNickNameRequest): Response<DefaultResponse> {
          return authService.checkNickname(nickname)
     }
}