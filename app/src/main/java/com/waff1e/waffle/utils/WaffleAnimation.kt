package com.waff1e.waffle.utils

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.navigation.NavBackStackEntry

object WaffleAnimation {
    private const val scale = 0.85f
    private const val tweenDuration = 300

    private const val springStiffness = Spring.StiffnessMediumLow

    val fadeIn: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) = {
        scaleIn(
            animationSpec = spring(stiffness = springStiffness),
            initialScale = scale
        ) + fadeIn(animationSpec = spring(stiffness = springStiffness))
    }

    val fadeOut: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) = {
        scaleOut(
            animationSpec = spring(stiffness = springStiffness),
            targetScale = scale
        ) + fadeOut(animationSpec = spring(stiffness = springStiffness))
    }

    val slideInLeft: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?) = {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(tweenDuration)
        )
    }

    val slideInUp: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?) = {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Up,
            animationSpec = tween(tweenDuration)
        )
    }

    val slideOutRight: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?) = {
        slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(tweenDuration)
        )
    }

    val slideOutDown: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?) = {
        slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Down,
            animationSpec = tween(tweenDuration)
        )
    }
}