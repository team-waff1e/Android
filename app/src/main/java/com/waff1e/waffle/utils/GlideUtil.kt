package com.waff1e.waffle.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableInferredTarget
import androidx.compose.runtime.ComposableTarget
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.Placeholder
import com.bumptech.glide.integration.compose.placeholder
import com.waff1e.waffle.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GlideImagePlaceholder(): Placeholder {
    return if (isSystemInDarkTheme()) placeholder(R.drawable.person) else placeholder(R.drawable.person_white)
}
