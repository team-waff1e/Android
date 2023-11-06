package com.waff1e.waffle.member.network

import com.waff1e.waffle.dto.DefaultResponse
import com.waff1e.waffle.member.dto.Member
import com.waff1e.waffle.member.dto.UpdateProfileRequest
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part

interface MemberService {
    @GET("members")
    suspend fun getMyProfile(): Response<Member>

    @GET("members/{memberId}")
    suspend fun getProfileById(id: Long): Response<Member>

    @POST("members/checkpwd")
    suspend fun checkPwd(@Body pwd: String): Response<DefaultResponse>

    @DELETE("members")
    suspend fun deleteMyProfile(@Body pwd: String): Response<Member>

    @PATCH("members")
    suspend fun updateMyProfile(@Body updateProfile: UpdateProfileRequest): Response<DefaultResponse>

    @PATCH("members/profile-image")
    @Multipart
    suspend fun updateMyProfileImage(@Part image: MultipartBody.Part): Response<String>
}