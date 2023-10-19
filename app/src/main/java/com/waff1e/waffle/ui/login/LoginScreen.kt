package com.waff1e.waffle.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waff1e.waffle.ui.WaffleTopAppBar
import com.waff1e.waffle.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    Scaffold(topBar = {
        WaffleTopAppBar(title = "Waffle")
    }) { innerPadding ->
        LoginBody(
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun LoginBody(
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
            text = "로그인",
            fontSize = 30.sp
        )

        Box (modifier = Modifier.weight(0.5f))

        LoginTextField(
            placeholderText = "이메일",
            value = ""
        )

        LoginTextField(
            placeholderText = "비밀번호",
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
                modifier= Modifier
                    .padding(0.dp, 5.dp),
                text = "로그인",
                style = Typography.bodyLarge,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginTextField(
    modifier: Modifier = Modifier,
    placeholderText: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    value: String,
//    itemUiState: ItemUiState,
//    onItemValueChanged: (ItemUiState) -> Unit,
) {
    // 포커스 관리하는 포커스 매니저
    val focusManager = LocalFocusManager.current

    // getString() 사용하기 위한 context
    val context = LocalContext.current

    OutlinedTextField(
        value = value,
        onValueChange = {
        },
        placeholder = {
            Text(
                text = placeholderText,
                style = Typography.bodyLarge,
                color = Color.Gray
            )
        },
        shape = ShapeDefaults.Medium,
        textStyle = Typography.bodyLarge,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        modifier = modifier
            .fillMaxWidth(),
    )
}

@Composable
@Preview
fun LoginPreview() {
    LoginScreen()
}