package com.waff1e.waffle.ui.home

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waff1e.waffle.R
import com.waff1e.waffle.ui.WaffleTopAppBar
import com.waff1e.waffle.ui.theme.Typography
import com.waff1e.waffle.ui.theme.WaffleTheme
import okhttp3.internal.wait

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
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Text(
                text = stringResource(id = R.string.home_title_text),
                style = Typography.titleLarge,
            )

            Spacer(modifier = Modifier.size(120.dp))

            SignupButton(
                onClicked = onSignupBtnClicked,
                text = stringResource(id = R.string.signup_btn_text)
            )

            LoginButton(
                onClicked = onLoginBtnClicked,
                text = stringResource(id = R.string.login_btn_text)
            )
        }
    }
}

@Composable
fun SignupButton(
    modifier: Modifier = Modifier,
    onClicked: () -> Unit,
    text: String
) {
    Button(
        modifier = modifier
            .fillMaxWidth(),
        onClick = onClicked
    ) {
        Text(
            modifier = modifier
                .padding(vertical = 7.dp),
            text = text,
            fontSize = 20.sp,
            color = Color.White
        )
    }
}

@Composable
fun LoginButton(
    modifier: Modifier = Modifier,
    onClicked: () -> Unit,
    text: String
) {
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth(),
        onClick = onClicked,
        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
    ) {
        Text(
            modifier = modifier
                .padding(vertical = 7.dp),
            text = text,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview
@Composable
fun HomePreview() {
    WaffleTheme {
        HomeScreen(
            navigateToLogin = { },
            navigateToSignup = { },
            navigateToWaffles = { }
        )
    }
}