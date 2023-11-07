package com.waff1e.waffle.ui.home

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waff1e.waffle.R
import com.waff1e.waffle.di.DOUBLE_CLICK_DELAY
import com.waff1e.waffle.ui.BackHandlerEndToast
import com.waff1e.waffle.ui.WaffleTopAppBar
import com.waff1e.waffle.ui.theme.Typography
import com.waff1e.waffle.ui.theme.WaffleTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToLogin: () -> Unit,
    navigateToSignup: () -> Unit,
) {
    Scaffold(
        topBar = {
            WaffleTopAppBar(
                hasNavigationIcon = false
            )
        }
    ) { innerPadding ->
        HomeBody(
            modifier = modifier.padding(innerPadding),
            onLoginBtnClicked = navigateToLogin,
            onSignupBtnClicked = navigateToSignup,
        )
    }
}

@Composable
fun HomeBody(
    modifier: Modifier = Modifier,
    onLoginBtnClicked: () -> Unit,
    onSignupBtnClicked: () -> Unit,
) {
    BackHandlerEndToast()

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
    text: String,
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
            .fillMaxWidth(),
        onClick = {
            if (defenderDoubleClick) {
                defenderDoubleClick = false
                onClicked()
            }
        },
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
    text: String,
) {
    var defenderDoubleClick by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(key1 = defenderDoubleClick) {
        if (defenderDoubleClick) return@LaunchedEffect
        else delay(DOUBLE_CLICK_DELAY)

        defenderDoubleClick = true
    }

    OutlinedButton(
        modifier = modifier
            .fillMaxWidth(),
        onClick = {
            if (defenderDoubleClick) {
                defenderDoubleClick = false
                onClicked()
            }
        },
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
        )
    }
}