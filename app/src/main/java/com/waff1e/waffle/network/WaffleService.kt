package com.waff1e.waffle.network

import com.waff1e.waffle.dto.Login
import com.waff1e.waffle.dto.LoginResponse
import com.waff1e.waffle.dto.Response
import com.waff1e.waffle.dto.Signup
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.POST


interface WaffleService {
    @POST("auth/login")
    suspend fun requestLogin(@Body login: Login): LoginResponse

    @POST("auth/signup")
    suspend fun requestSignup(@Body signup: Signup)

    @POST("auth/signup/email")
    suspend fun checkEmail(@Body email: String): Flow<Response>

    @POST("auth/signup/nickname")
    suspend fun checkNickname(@Body nickname: String): Flow<Response>
}