package com.waff1e.waffle.member.network

import com.waff1e.waffle.dto.DefaultResponse
import com.waff1e.waffle.member.dto.Member
import com.waff1e.waffle.member.dto.NicknameRequest
import com.waff1e.waffle.member.dto.PasswordRequest
import com.waff1e.waffle.member.dto.UpdateProfileRequest
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface MemberService {
    @GET("members")
    suspend fun getMyProfile(): Response<Member>

    @GET("members/{memberId}")
    suspend fun getProfileById(@Path("memberId") id: Long): Response<Member>

    @HTTP(method = "DELETE", path = "members", hasBody = true)
    suspend fun deleteMyProfile(@Body pwd: PasswordRequest): Response<DefaultResponse>

    @PATCH("members/profile-image")
    @Multipart
    suspend fun updateMyProfileImage(@Part image: MultipartBody.Part): Response<String>

    @POST("members/checkpwd")
    suspend fun checkPassword(@Body passwordRequest: PasswordRequest): Response<DefaultResponse>

    // TODO. 임시 URL
    @PATCH("members/pwd")
    suspend fun updatePassword(@Body passwordRequest: PasswordRequest): Response<DefaultResponse>

    @PATCH("members/nickname")
    suspend fun updateNickname(@Body nicknameRequest: NicknameRequest): Response<DefaultResponse>
}