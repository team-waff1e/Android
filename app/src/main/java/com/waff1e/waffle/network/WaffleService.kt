package com.waff1e.waffle.network

import com.waff1e.waffle.dto.Login
import com.waff1e.waffle.dto.LoginResponse
import com.waff1e.waffle.dto.Signup
import retrofit2.Call
import retrofit2.http.POST

interface WaffleService {
    @POST("auth/login")
    suspend fun requestLogin(login: Login) : Call<LoginResponse>

    @POST("auth/signup")
    suspend fun requestSignup(signup: Signup)
}