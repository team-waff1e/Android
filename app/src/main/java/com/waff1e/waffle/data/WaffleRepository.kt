package com.waff1e.waffle.data

import com.waff1e.waffle.dto.Login
import com.waff1e.waffle.dto.LoginResponse
import com.waff1e.waffle.dto.Response
import com.waff1e.waffle.dto.Signup
import com.waff1e.waffle.network.WaffleService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// TODO. 추후에 기능에 따라 Repository 분리 예정
interface WaffleRepository {
     suspend fun login(login: Login) : LoginResponse

     suspend fun signup(signup: Signup)

     suspend fun checkEmailStream(email: String): Flow<Response>

     suspend fun checkNickNameStream(nickname: String): Flow<Response>
}

class DefaultWaffleRepository @Inject constructor(
     private val waffleService: WaffleService,
) : WaffleRepository {
     override suspend fun login(login: Login) : LoginResponse {
          return waffleService.requestLogin(login)
     }

     override suspend fun signup(signup: Signup) {
          waffleService.requestSignup(signup)
     }

     override suspend fun checkEmailStream(email: String): Flow<Response> {
          return waffleService.checkEmail(email)
     }

     override suspend fun checkNickNameStream(nickname: String): Flow<Response> {
          return waffleService.checkNickname(nickname)
     }
}