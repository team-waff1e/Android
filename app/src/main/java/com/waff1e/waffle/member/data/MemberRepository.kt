package com.waff1e.waffle.member.data

import com.waff1e.waffle.dto.DefaultResponse
import com.waff1e.waffle.member.dto.Member
import com.waff1e.waffle.member.dto.UpdateProfileRequest
import com.waff1e.waffle.member.network.MemberService
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Part
import javax.inject.Inject

interface MemberRepository {
    suspend fun getMyProfile(): Response<Member>

    suspend fun getProfileById(id: Long): Response<Member>

    suspend fun checkPwd(pwd: String): Response<DefaultResponse>

    suspend fun deleteMyProfile(pwd: String): Response<Member>

    suspend fun updateMyProfile(updateProfile: UpdateProfileRequest): Response<DefaultResponse>

    suspend fun updateMyProfileImage(image: MultipartBody.Part): Response<String>
}

class DefaultMemberRepository @Inject constructor(
    private val memberService: MemberService
): MemberRepository {
    override suspend fun getMyProfile(): Response<Member> {
        return memberService.getMyProfile()
    }

    override suspend fun getProfileById(id: Long): Response<Member> {
        return memberService.getProfileById(id)
    }

    override suspend fun checkPwd(pwd: String): Response<DefaultResponse> {
        return memberService.checkPwd(pwd)
    }

    override suspend fun deleteMyProfile(pwd: String): Response<Member> {
        return memberService.deleteMyProfile(pwd)
    }

    override suspend fun updateMyProfile(updateProfile: UpdateProfileRequest): Response<DefaultResponse> {
        return memberService.updateMyProfile(updateProfile)
    }

    override suspend fun updateMyProfileImage(image: MultipartBody.Part): Response<String> {
        return memberService.updateMyProfileImage(image)
    }
}