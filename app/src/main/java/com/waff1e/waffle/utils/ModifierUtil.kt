package com.waff1e.waffle.utils

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

fun Modifier.loadingEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    val transition = rememberInfiniteTransition(label = "")
    val translateAnimation by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearOutSlowInEasing)
        ),
        label = ""
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color.LightGray.copy(alpha = 0.9f),
                Color.LightGray.copy(alpha = 0.4f),
            ),
            start = Offset(translateAnimation, translateAnimation),
            end = Offset(
                translateAnimation + size.width.toFloat(),
                translateAnimation + size.height.toFloat()
            ),
            tileMode = TileMode.Mirror
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
fun Modifier.optionalNestedScroll(scrollBehavior: TopAppBarScrollBehavior, pullRefreshState: PullRefreshState) = then(
    if (pullRefreshState.progress == 0f) {
        Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    } else {
        Modifier
    }
)

fun Modifier.optionalNestedScroll(canNestedScroll: Boolean, nestedScrollConnection: NestedScrollConnection?) = then(
    if (canNestedScroll) {
        Modifier.nestedScroll(nestedScrollConnection!!)
    } else {
        Modifier
    }
)
