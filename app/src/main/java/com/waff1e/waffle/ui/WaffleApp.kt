package com.waff1e.waffle.ui

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.waff1e.waffle.R
import com.waff1e.waffle.di.DOUBLE_CLICK_DELAY
import com.waff1e.waffle.di.LoginUserPreferenceModule
import com.waff1e.waffle.member.ui.profile.ProfileUiState
import com.waff1e.waffle.ui.navigation.WaffleNavHost
import com.waff1e.waffle.ui.theme.Typography
import com.waff1e.waffle.utils.GlideImagePlaceholder
import com.waff1e.waffle.utils.TopAppbarType
import com.waff1e.waffle.utils.clickableSingle
import com.waff1e.waffle.waffle.ui.waffles.WaffleListUiState
import kotlinx.coroutines.delay

@Composable
fun WaffleApp(
    navController: NavHostController = rememberNavController(),
    loginUserPreference: LoginUserPreferenceModule,
) {
    WaffleNavHost(
        navController = navController,
        loginUserPreference = loginUserPreference,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaffleTopAppBar(
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.app_name),
    hasNavigationIcon: Boolean,
    navigationIconClicked: () -> Unit = { },
    navigationIcon: ImageVector = Icons.Filled.ArrowBack,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    type: TopAppbarType = TopAppbarType.Default,
    onAction: () -> Unit = { },
    enableAction: Boolean = false,
    actionBtnText: String = stringResource(id = R.string.post_waffle),
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = Typography.titleMedium
            )
        },
        modifier = modifier,
        navigationIcon = {
            if (hasNavigationIcon) {
                IconButton(onClick = navigationIconClicked) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = stringResource(R.string.navigation_icon),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        },
        actions = {
            when (type) {
                TopAppbarType.PostWaffle -> {
                    PostWaffleButton(
                        onAction = onAction,
                        enableAction = enableAction,
                        text = actionBtnText
                    )
                }

                else -> Unit
            }
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(scrolledContainerColor = MaterialTheme.colorScheme.background)
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ProfileTopAppBar(
    modifier: Modifier = Modifier,
    hasNavigationIcon: Boolean,
    navigationIconClicked: () -> Unit = { },
    navigationIcon: ImageVector = Icons.Filled.ArrowBack,
    actionIcon: ImageVector = Icons.Filled.Settings,
    onAction: () -> Unit = { },
    profile: () -> ProfileUiState,
    myWaffleListUiState: () -> WaffleListUiState,
    showTopAppbarTitle: Boolean,
) {
    TopAppBar(
        title = {
            if (showTopAppbarTitle) {
                Column {
                    Text(
                        text = profile().member?.nickname ?: "",
                        style = Typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "게시물 ${myWaffleListUiState().waffleList.size}개",
                        style = Typography.bodyMedium
                    )
                }
            }
        },
        modifier = modifier,
        navigationIcon = {
            if (hasNavigationIcon) {
                IconButton(onClick = navigationIconClicked) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = stringResource(R.string.navigation_icon),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onAction) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = stringResource(R.string.action_icon),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent)
    )
}

@Composable
fun PostWaffleButton(
    modifier: Modifier = Modifier,
    onAction: () -> Unit,
    enableAction: Boolean,
    text: String,
    hasHorizontalPadding: Boolean = true,
) {
    var defenderDoubleClick by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(key1 = defenderDoubleClick) {
        if (defenderDoubleClick) return@LaunchedEffect
        else delay(DOUBLE_CLICK_DELAY)

        defenderDoubleClick = true
    }

    Button(
        modifier = modifier
            .defaultMinSize(minHeight = 1.dp, minWidth = 1.dp)
            .padding(horizontal = if (hasHorizontalPadding) 10.dp else 0.dp),
        onClick = {
            if (defenderDoubleClick) {
                defenderDoubleClick = false
                onAction()
            }
        },
        enabled = enableAction,
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            disabledContainerColor = MaterialTheme.colorScheme.inversePrimary,
            disabledContentColor = MaterialTheme.colorScheme.inverseOnSurface
        )
    ) {
        Text(
            modifier = modifier
                .padding(horizontal = 15.dp, vertical = 5.dp),
            text = text,
            style = Typography.titleSmall,
        )
    }
}

@Composable
fun BackHandlerEndToast(
    modifier: Modifier = Modifier,
) {
    var backWait = 0L
    val context = LocalContext.current

    // 뒤로가기시 종료 안내 Toast 메세지
    BackHandler {
        if (System.currentTimeMillis() - backWait >= 2000) {
            backWait = System.currentTimeMillis()
            Toast.makeText(
                context,
                context.getText(R.string.exit_toast_message),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            (context as? Activity)?.finish()
        }
    }
}

@Composable
fun WaffleDivider(
    modifier: Modifier = Modifier,
    topPadding: Dp = 0.dp,
    bottomPadding: Dp = 0.dp,
) {
    Spacer(modifier = modifier.size(topPadding))

    Divider(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
        thickness = 1.dp
    )

    Spacer(modifier = modifier.size(bottomPadding))
}

@Composable
fun WaffleReportMenu(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onReportClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
            .clickableSingle(disableRipple = true) { onDismiss() },
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            WaffleEditDeleteMenuItem(
                title = stringResource(id = R.string.do_report),
                imageVector = ImageVector.vectorResource(id = R.drawable.edit),
                onClicked = { }
            )
        }
    }
}


@Composable
fun WaffleEditDeleteMenu(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onEditClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
            .clickableSingle(disableRipple = true) { onDismiss() },
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            WaffleEditDeleteMenuItem(
                title = stringResource(id = R.string.do_edit),
                imageVector = ImageVector.vectorResource(id = R.drawable.edit),
                onClicked = { onEditClicked() }
            )

            WaffleEditDeleteMenuItem(
                title = stringResource(id = R.string.do_delete),
                imageVector = ImageVector.vectorResource(id = R.drawable.delete),
                onClicked = {
                    onDeleteClicked()
                    onDismiss()
                }
            )
        }
    }
}

@Composable
fun WaffleEditDeleteMenuItem(
    modifier: Modifier = Modifier,
    title: String,
    imageVector: ImageVector,
    onClicked: () -> Unit = { },
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickableSingle(disableRipple = true, onClick = { onClicked() }),
        horizontalArrangement = Arrangement.spacedBy(30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(30.dp),
            imageVector = imageVector,
            contentDescription = stringResource(id = R.string.edit_delete_report_menu_item),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
        )

        Text(
            text = title,
            style = Typography.bodyLarge
        )
    }
}