package com.waff1e.waffle.auth.ui.login

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waff1e.waffle.R
import com.waff1e.waffle.ui.WaffleTopAppBar
import com.waff1e.waffle.ui.theme.Typography
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    canNavigateBack: Boolean = true,
    onNavigateUp: () -> Unit,
    navigateBack: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar = {
        WaffleTopAppBar(
            title = stringResource(id = R.string.app_name),
            canNavigationBack = canNavigateBack,
            navigateUp = onNavigateUp
        )
    }) { innerPadding ->
        LoginBody(
            modifier = modifier.padding(innerPadding),
            navigateBack = navigateBack,
            loginUiState = viewModel.loginUiState,
            onItemValueChanged = viewModel::updateLoginUiState,
            onLoginBtnClicked = {
                coroutineScope.launch {
                    val responseResult = viewModel.requestLogin()

                    if (responseResult.isSuccess) {
                        // TODO. 로그인 성공 처리

                    } else {
                        // TODO. 로그인 실패 처리
                        
                    }
                }
            }
        )
    }
}

@Composable
fun LoginBody(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    loginUiState: LoginUiState,
    onItemValueChanged: (LoginUiState) -> Unit,
    onLoginBtnClicked: () -> Unit,
) {
    // Composable에 포커스가 있는지 확인하는 변수
    var isFocused by remember { mutableStateOf(false) }
    // 포커스 관리하는 포커스 매니저
    val focusManager = LocalFocusManager.current

    BackHandler {
        if (isFocused) focusManager.clearFocus() else navigateBack()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(25.dp)
            .onFocusChanged { isFocused = it.hasFocus },
        verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "로그인",
            fontSize = 30.sp
        )

        Box(modifier = Modifier.weight(0.5f))

        LoginTextField(
            placeholderText = stringResource(id = R.string.email),
            value = loginUiState.email,
            loginUiState = loginUiState,
            onItemValueChanged = onItemValueChanged,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
        )

        LoginTextField(
            placeholderText = stringResource(id = R.string.password),
            value = loginUiState.pwd,
            loginUiState = loginUiState,
            onItemValueChanged = onItemValueChanged,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        Box(modifier = Modifier.weight(1f))

        Button(
            onClick = onLoginBtnClicked,
            enabled = loginUiState.canLogin,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            shape = ShapeDefaults.Medium,
        ) {
            Text(
                modifier = Modifier.padding(0.dp, 5.dp),
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
    loginUiState: LoginUiState,
    onItemValueChanged: (LoginUiState) -> Unit,
) {
    // 포커스 관리하는 포커스 매니저
    val focusManager = LocalFocusManager.current
    // getString() 사용하기 위한 context
    val context = LocalContext.current

    OutlinedTextField(
        value = value,
        onValueChange = {
            when (placeholderText) {
                context.getString(R.string.email) -> onItemValueChanged(loginUiState.copy(email = it))
                context.getString(R.string.password) -> onItemValueChanged(loginUiState.copy(pwd = it))
            }
        },
        label = {
            Text(
                text = placeholderText,
                style = Typography.bodyLarge,
                color = Color.Gray
            )
        },
        shape = ShapeDefaults.Medium,
        textStyle = Typography.bodyLarge,
        singleLine = true,
        visualTransformation = when (placeholderText) {
            context.getString(R.string.password) -> PasswordVisualTransformation()
            else -> VisualTransformation.None
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        isError = when (placeholderText) {
            context.getString(R.string.email) -> !loginUiState.isEmailValid()
            context.getString(R.string.password) -> !loginUiState.isPwdValid()
            else -> false
        },
        modifier = modifier
            .fillMaxWidth(),
    )
}

@Composable
@Preview
fun LoginPreview() {
    LoginScreen(
        onNavigateUp = { },
        navigateBack = { },
    )
}