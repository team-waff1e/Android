package com.waff1e.waffle.ui

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.waff1e.waffle.R
import com.waff1e.waffle.ui.navigation.WaffleNavHost
import com.waff1e.waffle.ui.theme.Typography

@Composable
fun WaffleApp(navController: NavHostController = rememberNavController()) {
    WaffleNavHost(navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaffleTopAppBar(
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.app_name),
    hasNavigationIcon: Boolean,
    navigationIconClicked: () -> Unit = { },
    imageVector: ImageVector = Icons.Filled.ArrowBack,
) {
    if (hasNavigationIcon) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    style = Typography.titleMedium
                )
            },
            modifier = modifier,
            navigationIcon = {
                IconButton(onClick = navigationIconClicked) {
                    Icon(
                        imageVector = imageVector,
                        contentDescription = stringResource(R.string.navigation_icon)
                    )
                }
            },
        )
    } else {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    style = Typography.titleMedium
                )
            },
            modifier = modifier,
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
) {
    Divider(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
        thickness = 1.dp
    )
}