package com.waff1e.waffle.waffle.data

import com.waff1e.waffle.waffle.dto.WaffleListSuccessResponse
import com.waff1e.waffle.waffle.network.WaffleService
import retrofit2.Response
import javax.inject.Inject

interface WaffleRepository {
    // TODO. GET 메소드에서 데이터 전송 방식 설정 필요
    suspend fun requestWaffleList(): Response<WaffleListSuccessResponse>
}

class DefaultWaffleRepository @Inject constructor(
    private val waffleService: WaffleService
) : WaffleRepository {
    // TODO. GET 메소드에서 데이터 전송 방식 설정 필요
    override suspend fun requestWaffleList(): Response<WaffleListSuccessResponse> {
        return waffleService.getWaffleList()
    }
}