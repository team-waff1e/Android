package com.waff1e.waffle.member.data

import com.waff1e.waffle.dto.DefaultResponse
import com.waff1e.waffle.member.dto.PasswordRequest
import com.waff1e.waffle.member.dto.Member
import com.waff1e.waffle.member.dto.NicknameRequest
import com.waff1e.waffle.member.dto.UpdateProfileRequest
import com.waff1e.waffle.member.network.MemberService
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

interface MemberRepository {
    suspend fun getMyProfile(): Response<Member>

    suspend fun getProfileById(id: Long): Response<Member>

    suspend fun deleteMyProfile(pwd: PasswordRequest): Response<DefaultResponse>

    suspend fun updateMyProfileImage(image: MultipartBody.Part): Response<String>

    suspend fun checkPassword(passwordRequest: PasswordRequest): Response<DefaultResponse>

    suspend fun updatePassword(passwordRequest: PasswordRequest): Response<DefaultResponse>

    suspend fun updateNickname(nicknameRequest: NicknameRequest): Response<DefaultResponse>
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
    override suspend fun deleteMyProfile(pwd: PasswordRequest): Response<DefaultResponse> {
        return memberService.deleteMyProfile(pwd)
    }

    override suspend fun updateMyProfileImage(image: MultipartBody.Part): Response<String> {
        return memberService.updateMyProfileImage(image)
    }

    override suspend fun checkPassword(passwordRequest: PasswordRequest): Response<DefaultResponse> {
        return memberService.checkPassword(passwordRequest)
    }

    override suspend fun updatePassword(passwordRequest: PasswordRequest): Response<DefaultResponse> {
        return memberService.updatePassword(passwordRequest)
    }

    override suspend fun updateNickname(nicknameRequest: NicknameRequest): Response<DefaultResponse> {
        return memberService.updateNickname(nicknameRequest)
    }
}