package com.waff1e.waffle.data

import com.waff1e.waffle.dto.Login
import com.waff1e.waffle.dto.LoginResponse
import com.waff1e.waffle.dto.Signup
import com.waff1e.waffle.network.WaffleService
import retrofit2.Call
import javax.inject.Inject

// TODO. 추후에 기능에 따라 Repository 분리 예정
interface WaffleRepository {
     suspend fun login(login: Login) : LoginResponse

     suspend fun signup(signup: Signup)
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
}