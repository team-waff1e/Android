package com.waff1e.waffle.ui.home

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waff1e.waffle.R
import com.waff1e.waffle.ui.WaffleTopAppBar
import com.waff1e.waffle.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    navigateToLogin: () -> Unit,
    navigateToSignup: () -> Unit,
    navigateToWaffles: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    Scaffold(
        topBar = {
            WaffleTopAppBar(
                title = stringResource(id = R.string.app_name),
                canNavigationBack = canNavigateBack
            )
        }
    ) { innerPadding ->
        HomeBody(
            modifier = modifier.padding(innerPadding),
            onLoginBtnClicked = navigateToLogin,
            onSignupBtnClicked = navigateToSignup,
            onWafflesBtnClicked = navigateToWaffles
        )
    }
}

@Composable
fun HomeBody(
    modifier: Modifier = Modifier,
    onLoginBtnClicked: () -> Unit,
    onSignupBtnClicked: () -> Unit,
    onWafflesBtnClicked: () -> Unit
) {
    var backWait = 0L
    val context = LocalContext.current

    // 뒤로가기시 종료 안내 Toast 메세지
    BackHandler {
        if (System.currentTimeMillis() - backWait >= 2000) {
            backWait = System.currentTimeMillis()
            Toast.makeText(context, context.getText(R.string.exit_toast_message), Toast.LENGTH_SHORT).show()
        } else {
            (context as? Activity)?.finish()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to Waffle",
            style = Typography.titleMedium,
        )

        Text(
            text = "See What's happening in the world right now",
            style = Typography.titleMedium,
        )

        Spacer(modifier = Modifier.size(150.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onSignupBtnClicked
            ) {
                Text(
                    text = "Sign Up"
                )
            }

            Button(
                onClick = onLoginBtnClicked
            ) {
                Text(
                    text = "Log in"
                )
            }

            // TODO. 로그인 api 연결 전 임시 버튼
            Button(
                onClick = onWafflesBtnClicked
            ) {
                Text(
                    text = "Waffles"
                )
            }
        }
    }
}

@Preview
@Composable
fun HomePreview() {
    HomeScreen(
        navigateToLogin = {  },
        navigateToSignup = {  },
        navigateToWaffles = {  }
    )
}