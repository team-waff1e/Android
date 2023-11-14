package com.waff1e.waffle.comment.data

import com.waff1e.waffle.comment.dto.Comment
import com.waff1e.waffle.comment.dto.CommentRequest
import com.waff1e.waffle.comment.network.CommentService
import com.waff1e.waffle.dto.DefaultResponse
import retrofit2.Response
import javax.inject.Inject

interface CommentRepository {
    suspend fun getCommentList(waffleId: Long, limit: Int): Response<List<Comment>>

    suspend fun getCommentDetail(waffleId: Long, commentId: Long): Response<Comment>

    suspend fun postComment(waffleId: Long, comment: CommentRequest): Response<DefaultResponse>

    suspend fun updateComment(waffleId: Long, commentId: Long, comment: CommentRequest): Response<DefaultResponse>

    suspend fun deleteComment(waffleId: Long, commentId: Long): Response<DefaultResponse>
}

class DefaultCommentRepository @Inject constructor(
    private val commentService: CommentService
): CommentRepository {
    override suspend fun getCommentList(waffleId: Long, limit: Int): Response<List<Comment>> {
        return commentService.getCommentList(waffleId, limit)
    }

    override suspend fun getCommentDetail(waffleId: Long, commentId: Long): Response<Comment> {
        return commentService.getCommentDetail(waffleId, commentId)
    }

    override suspend fun postComment(waffleId: Long, comment: CommentRequest): Response<DefaultResponse> {
        return commentService.postComment(waffleId, comment)
    }

    override suspend fun updateComment(waffleId: Long, commentId: Long, comment: CommentRequest): Response<DefaultResponse> {
        return commentService.updateComment(waffleId, commentId, comment)
    }

    override suspend fun deleteComment(waffleId: Long, commentId: Long): Response<DefaultResponse> {
        return commentService.deleteComment(waffleId, commentId)
    }
}