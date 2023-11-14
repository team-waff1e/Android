package com.waff1e.waffle.ui

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.waff1e.waffle.R
import com.waff1e.waffle.di.DOUBLE_CLICK_DELAY
import com.waff1e.waffle.di.LoginUserPreferenceModule
import com.waff1e.waffle.ui.navigation.WaffleNavHost
import com.waff1e.waffle.ui.theme.Typography
import com.waff1e.waffle.utils.TopAppbarType
import kotlinx.coroutines.delay

@Composable
fun WaffleApp(
    navController: NavHostController = rememberNavController(),
    loginUserPreference: LoginUserPreferenceModule
) {
    WaffleNavHost(
        navController = navController,
        loginUserPreference = loginUserPreference,
    )
}

@Composable
fun WaffleTopAppBarTitleText(
    title: String
) {
    Text(
        text = title,
        style = Typography.titleMedium
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
    actionIcon: ImageVector = Icons.Filled.Settings,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    type: TopAppbarType = TopAppbarType.Default,
    onAction: () -> Unit = { },
    enableAction: Boolean = false,
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
                        modifier = modifier,
                        onAction = onAction,
                        enableAction = enableAction
                    )
                }
                TopAppbarType.Profile -> {
                    IconButton(onClick = onAction) {
                        Icon(
                            imageVector = actionIcon,
                            contentDescription = stringResource(R.string.action_icon),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
                TopAppbarType.Default -> Unit
            }
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(scrolledContainerColor = MaterialTheme.colorScheme.background)
    )
}

@Composable
fun PostWaffleButton(
    modifier: Modifier = Modifier,
    onAction: () -> Unit,
    enableAction: Boolean,
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
            .padding(horizontal = 10.dp),
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
            text = stringResource(id = R.string.post_waffle),
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

fun LazyListState.isEnd(): Boolean {
    return layoutInfo.visibleItemsInfo.firstOrNull()?.index != 0 && layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
}