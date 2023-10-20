package com.waff1e.waffle.ui.home

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.waff1e.waffle.R
import com.waff1e.waffle.ui.WaffleTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToLogin: () -> Unit,
    navigateToSignup: () -> Unit,
    navigateToWaffle: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    Scaffold(
        topBar = {
            WaffleTopAppBar(
                title = stringResource(id = R.string.app_name),
                canNavigationBack = false
            )
        }
    ) { innerPadding ->
        HomeBody(
            modifier = modifier.padding(innerPadding),
            onLoginBtnClicked = navigateToLogin,
            onSignupBtnClicked = navigateToSignup
        )
    }
}

@Composable
fun HomeBody(
    modifier: Modifier = Modifier,
    onLoginBtnClicked: () -> Unit,
    onSignupBtnClicked: () -> Unit,
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
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Welcome to Waffle"
        )

        Text(
            text = "See What's happening in the world right now"
        )

        Row {
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
        }
    }
}

@Preview
@Composable
fun HomePreview() {
    HomeScreen(
        navigateToLogin = {  },
        navigateToSignup = {  },
        navigateToWaffle = {  }
    )
}