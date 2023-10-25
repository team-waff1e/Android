package com.waff1e.waffle.auth.ui.signup

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    viewModel: SignupViewModel = hiltViewModel(),
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
        SignupBody(
            modifier = modifier.padding(innerPadding),
            navigateBack = navigateBack,
            signupUiState = viewModel.signupUiState,
            onItemValueChanged = viewModel::updateSignupUiState,
            onSignupBtnClicked = {
                coroutineScope.launch {
                    viewModel.requestSignup()
                }
            },
            viewModel = viewModel
        )
    }
}

@Composable
fun SignupBody(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    signupUiState: SignupUiState,
    onItemValueChanged: (SignupUiState) -> Unit,
    onSignupBtnClicked: () -> Unit,
    viewModel: SignupViewModel,
) {
    // Composable에 포커스가 있는지 확인하는 변수
    var isFocused by remember { mutableStateOf(false) }
    // 포커스 관리하는 포커스 매니저
    val focusManager = LocalFocusManager.current

    BackHandler {
        if (isFocused) focusManager.clearFocus() else navigateBack()
    }

    val debounceTime = 350L
    LaunchedEffect(key1 = viewModel.emailTerm.value) {
        delay(debounceTime)
        if (viewModel.emailTerm.value.isNotBlank()) {
            viewModel.checkEmail()
        }
    }

    LaunchedEffect(key1 = viewModel.nicknameTerm.value) {
        delay(debounceTime)
        if (viewModel.nicknameTerm.value.isNotBlank()) {
            viewModel.checkNickname()
        }
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
            text = "회원가입",
            fontSize = 30.sp
        )

        Box(modifier = Modifier.weight(0.5f))

        SignupTextField(
            placeholderText = "이메일",
            value = viewModel.emailTerm.value,
            signupUiState = signupUiState,
            onItemValueChanged = onItemValueChanged,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            viewModel = viewModel
        )

        SignupTextField(
            placeholderText = "이름",
            value = viewModel.signupUiState.name,
            signupUiState = signupUiState,
            onItemValueChanged = onItemValueChanged,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            viewModel = viewModel
        )

        SignupTextField(
            placeholderText = "비밀번호",
            value = viewModel.signupUiState.password,
            signupUiState = signupUiState,
            onItemValueChanged = onItemValueChanged,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            viewModel = viewModel
        )

        SignupTextField(
            placeholderText = "비밀번호 확인",
            value = viewModel.signupUiState.passwordConfirm,
            signupUiState = signupUiState,
            onItemValueChanged = onItemValueChanged,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            viewModel = viewModel
        )

        SignupTextField(
            placeholderText = "닉네임",
            value = viewModel.nicknameTerm.value,
            signupUiState = signupUiState,
            onItemValueChanged = onItemValueChanged,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            viewModel = viewModel
        )

        Box(modifier = Modifier.weight(1f))

        Button(
            onClick = onSignupBtnClicked,
            enabled = signupUiState.canSignup,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            shape = ShapeDefaults.Medium,
        ) {
            Text(
                modifier = Modifier.padding(0.dp, 10.dp),
                text = "회원가입",
                style = Typography.bodyLarge,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupTextField(
    modifier: Modifier = Modifier,
    placeholderText: String,
    keyboardOptions: KeyboardOptions,
    value: String,
    signupUiState: SignupUiState,
    onItemValueChanged: (SignupUiState) -> Unit,
    viewModel: SignupViewModel,
) {
    // 포커스 관리하는 포커스 매니저
    val focusManager = LocalFocusManager.current
    // getString() 사용하기 위한 context
    val context = LocalContext.current

    OutlinedTextField(
        value = value,
        onValueChange = {
            when (placeholderText) {
                context.getString(R.string.email) -> viewModel.emailTerm.value = it
                context.getString(R.string.name) -> onItemValueChanged(signupUiState.copy(name = it))
                context.getString(R.string.password) -> onItemValueChanged(
                    signupUiState.copy(
                        password = it
                    )
                )

                context.getString(R.string.password_confirm) -> onItemValueChanged(
                    signupUiState.copy(
                        passwordConfirm = it
                    )
                )

                context.getString(R.string.nickname) -> viewModel.nicknameTerm.value = it
            }
        },
        label = {
            Text(
                text = placeholderText,
            )
        },
        shape = ShapeDefaults.Medium,
        textStyle = Typography.bodyLarge,
        singleLine = true,
        visualTransformation = when (placeholderText) {
            context.getString(R.string.password) -> PasswordVisualTransformation()
            context.getString(R.string.password_confirm) -> PasswordVisualTransformation()
            else -> VisualTransformation.None
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        isError = when (placeholderText) {
            context.getString(R.string.email) -> !signupUiState.isEmailValid()
            context.getString(R.string.name) -> !signupUiState.isNameValid()
            context.getString(R.string.password) -> !signupUiState.isPasswordValid()
            context.getString(R.string.password_confirm) -> !signupUiState.isPasswordMatch()
            context.getString(R.string.nickname) -> !signupUiState.isNicknameValid()
            else -> false
        },
        modifier = modifier
            .fillMaxWidth(),
    )
}

@Composable
@Preview
fun SignupPreview() {
    SignupScreen(
        onNavigateUp = { },
        navigateBack = { },
    )
}