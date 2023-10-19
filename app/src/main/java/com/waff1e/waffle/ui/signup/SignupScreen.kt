package com.waff1e.waffle.ui.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waff1e.waffle.ui.WaffleTopAppBar
import com.waff1e.waffle.ui.login.LoginTextField
import com.waff1e.waffle.ui.login.LoginViewModel
import com.waff1e.waffle.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    Scaffold(topBar = {
        WaffleTopAppBar(title = "Waffle")
    }) { innerPadding ->
        SignupBody(
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun SignupBody(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(25.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "회원가입",
            fontSize = 30.sp
        )

        Box (modifier = Modifier.weight(0.5f))

        LoginTextField(
            placeholderText = "이메일",
            value = ""
        )

        LoginTextField(
            placeholderText = "이름",
            value = ""
        )

        LoginTextField(
            placeholderText = "비밀번호",
            value = ""
        )

        LoginTextField(
            placeholderText = "비밀번호 확인",
            value = ""
        )

        LoginTextField(
            placeholderText = "닉네임",
            value = ""
        )

        Box (modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            shape = ShapeDefaults.Medium,
            onClick = { },
        ) {
            Text(
                modifier= Modifier.padding(0.dp, 10.dp),
                text = "회원가입",
                style = Typography.bodyLarge,
            )
        }
    }
}

@Composable
@Preview
fun SignupPreview() {
    SignupScreen()
}