package com.waff1e.waffle.network

import com.waff1e.waffle.dto.CheckEmail
import com.waff1e.waffle.dto.CheckNickName
import com.waff1e.waffle.dto.DefaultResponse
import com.waff1e.waffle.dto.Signup
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface WaffleService {
    @POST("auth/login")
    suspend fun requestLogin(@Body jsonLogin: JsonElement): Response<DefaultResponse>

    @POST("auth/signup")
    suspend fun requestSignup(@Body signup: Signup): Response<DefaultResponse>

    @POST("auth/signup/email")
    suspend fun checkEmail(@Body email: CheckEmail): Response<DefaultResponse>

    @POST("auth/signup/nickname")
    suspend fun checkNickname(@Body nickname: CheckNickName): Response<DefaultResponse>
}