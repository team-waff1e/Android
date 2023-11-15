package com.waff1e.waffle.utils

import com.waff1e.waffle.comment.dto.Comment
import com.waff1e.waffle.waffle.dto.Waffle

fun List<Waffle>.updateLikes(id: Long) {
    val waffle = find { it.id == id }
    waffle?.apply {
        if (liked) likesCount-- else likesCount++
        liked = !liked
    }
}

fun List<Waffle>.removeWaffle(id: Long): List<Waffle> {
    val mutableList = toMutableList()
    mutableList.removeIf { it.id == id }
    return mutableList.toList()
}

fun List<Comment>.removeComment(id: Long): List<Comment> {
    val mutableList = toMutableList()
    mutableList.removeIf { it.id == id }
    return mutableList.toList()
}