package com.waff1e.waffle.member.ui.profile_detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.waff1e.waffle.R
import com.waff1e.waffle.di.LoginUser
import com.waff1e.waffle.member.ui.profile.ProfileUiState
import com.waff1e.waffle.ui.WaffleTopAppBar
import com.waff1e.waffle.ui.theme.Typography
import com.waff1e.waffle.utils.clickableSingle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailScreen(
    modifier: Modifier = Modifier,
    canNavigationBack: Boolean = true,
    navigateBack: () -> Unit,
) {
    Scaffold(
        modifier = modifier
            .background(Color.Transparent),
        topBar = {
            WaffleTopAppBar(
                hasNavigationIcon = canNavigationBack,
                navigationIconClicked = navigateBack,
                title = stringResource(id = R.string.profile_detail)
            )
        },
    ) { innerPadding ->
        ProfileDetailBody(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
        )
    }
}

@Composable
fun ProfileDetailBody(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        ProfileDetailProfileImage(
            imgURL = LoginUser.profileUrl!!
        )

        ProfileDetailItem(
            title = stringResource(id = R.string.nickname),
            content = LoginUser.nickname!!
        )

        ProfileDetailItem(
            title = stringResource(id = R.string.email),
            content = LoginUser.email!!
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileDetailProfileImage(
    modifier: Modifier = Modifier,
    imgURL: String,
) {
    val placeholder =
        if (isSystemInDarkTheme()) placeholder(R.drawable.person) else placeholder(R.drawable.person_white)

    Column(
        modifier = modifier
            .clickableSingle(
                disableRipple = true,
                onClick = {
                    // TODO. 프로필 이미지 변경
                }
            ),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        Text(
            text = stringResource(id = R.string.profile_img),
            style = Typography.titleSmall
        )

        GlideImage(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onBackground),
            model = imgURL,
            contentScale = ContentScale.Crop,
            transition = CrossFade,
            loading = placeholder,
            failure = placeholder,
            contentDescription = stringResource(id = R.string.profile_img),
        )
    }
}

@Composable
fun ProfileDetailItem(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    onClicked: () -> Unit = { },
) {
    Column(
        modifier = modifier
            .clickableSingle(disableRipple = true, onClick = { onClicked() }),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            text = title,
            style = Typography.titleSmall
        )

        Text(
            text = content,
            style = Typography.bodyMedium,
            color = MaterialTheme.colorScheme.inverseSurface
        )
    }
}