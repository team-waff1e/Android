package com.waff1e.waffle.comment.network

import com.waff1e.waffle.comment.dto.Comment
import com.waff1e.waffle.comment.dto.CommentRequest
import com.waff1e.waffle.dto.DefaultResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentService {
    @GET("/waffles/{waffleId}/comments")
    suspend fun getCommentList(
        @Path("waffleId") waffleId: Long,
        @Query("limit") limit: Int
    ): Response<List<Comment>>

    @GET("waffles/{waffleId}/comments/{commentId}")
    suspend fun getCommentDetail(
        @Path("waffleId") waffleId: Long,
        @Path("commentId") commentId: Long): Response<Comment>

    @POST("waffles/{waffleId}/comments")
    suspend fun postComment(
        @Path("waffleId") waffleId: Long,
        @Body comment: CommentRequest
    ): Response<DefaultResponse>

    @PATCH("waffles/{waffleId}/comments/{commentId}")
    suspend fun updateComment(
        @Path("waffleId") waffleId: Long,
        @Path("waffleId") commentId: Long,
        @Body comment: CommentRequest,
    ): Response<DefaultResponse>

    @DELETE("waffles/{waffleId}/comments/{commentId}")
    suspend fun deleteComment(
        @Path("waffleId") waffleId: Long,
        @Path("commentId") commentId: Long): Response<DefaultResponse>
}