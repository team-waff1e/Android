package com.waff1e.waffle.auth.ui.login

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    navigateBack: () -> Unit,
    navigateToWaffles: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar = {
        WaffleTopAppBar(
            hasNavigationIcon = canNavigateBack,
            navigationIconClicked = navigateBack
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
                        navigateToWaffles()
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
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.login_title_text),
            style = Typography.titleLarge,
            textAlign = TextAlign.Start,
        )

        Box(modifier = Modifier.size(10.dp))

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
                .fillMaxWidth(),
            shape = ShapeDefaults.Medium,
        ) {
            Text(
                modifier = Modifier.padding(vertical = 7.dp),
                text = stringResource(id = R.string.login),
                style = Typography.labelMedium,
                color = Color.White
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
    var visualTransformation: VisualTransformation by remember {
        if (placeholderText == context.getString(R.string.password)) {
            mutableStateOf(PasswordVisualTransformation())
        } else {
            mutableStateOf(VisualTransformation.None)
        }
    }
    val interactionSource = remember { MutableInteractionSource() }


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
        trailingIcon = if (placeholderText == context.getString(R.string.password)) {
            {
                if (placeholderText == context.getString(R.string.password) && loginUiState.pwd.isNotEmpty()) {
                    Icon(
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            visualTransformation = if (visualTransformation == PasswordVisualTransformation()) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            }
                        },
                        imageVector = if (visualTransformation == PasswordVisualTransformation()) {
                            ImageVector.vectorResource(id = R.drawable.visibility)
                        } else {
                            ImageVector.vectorResource(id = R.drawable.visibility_off)
                        },
                        contentDescription = stringResource(id = R.string.password_visible_btn_description),
                    )
                }
            }
        } else {
            null
        },
        shape = ShapeDefaults.Medium,
        textStyle = Typography.bodyLarge,
        singleLine = true,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        modifier = modifier
            .fillMaxWidth(),
    )
}

@Composable
@Preview
fun LoginPreview() {
    LoginScreen(
        navigateBack = {  },
        navigateToWaffles = {  }
    )
}