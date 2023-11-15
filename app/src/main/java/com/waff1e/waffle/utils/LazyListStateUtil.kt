package com.waff1e.waffle.utils

import androidx.compose.foundation.lazy.LazyListState

fun LazyListState.isEnd(): Boolean {
    return layoutInfo.visibleItemsInfo.firstOrNull()?.index != 0 && layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
}