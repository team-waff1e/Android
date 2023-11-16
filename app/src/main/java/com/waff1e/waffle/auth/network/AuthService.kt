package com.waff1e.waffle.auth.network

import com.waff1e.waffle.auth.dto.CheckEmailRequest
import com.waff1e.waffle.auth.dto.CheckNickNameRequest
import com.waff1e.waffle.auth.dto.LoginRequest
import com.waff1e.waffle.dto.DefaultResponse
import com.waff1e.waffle.auth.dto.SignupRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    suspend fun requestLogin(@Body loginRequest: LoginRequest): Response<DefaultResponse>

    @POST("auth/logout")
    suspend fun requestLogout(): Response<DefaultResponse>

    @POST("auth/signup")
    suspend fun requestSignup(@Body signupRequest: SignupRequest): Response<DefaultResponse>

    @POST("auth/signup/email")
    suspend fun checkEmail(@Body email: CheckEmailRequest): Response<DefaultResponse>

    @POST("auth/signup/nickname")
    suspend fun checkNickname(@Body nickname: CheckNickNameRequest): Response<DefaultResponse>
}