package com.waff1e.waffle.auth.ui.signup

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.waff1e.waffle.di.DEBOUNCE_TIME
import com.waff1e.waffle.di.DOUBLE_CLICK_DELAY
import com.waff1e.waffle.di.NAME_MAX_LENGTH
import com.waff1e.waffle.di.NICKNAME_MAX_LENGTH
import com.waff1e.waffle.ui.WaffleTopAppBar
import com.waff1e.waffle.ui.theme.Error
import com.waff1e.waffle.ui.theme.Typography
import com.waff1e.waffle.ui.theme.WaffleTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    viewModel: SignupViewModel = hiltViewModel(),
    canNavigateBack: Boolean = true,
    navigateBack: () -> Unit,
    navigateToHome: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar = {
        WaffleTopAppBar(
            hasNavigationIcon = canNavigateBack,
            navigationIconClicked = navigateBack
        )
    }) { innerPadding ->
        SignupBody(
            modifier = modifier.padding(innerPadding),
            navigateBack = navigateBack,
            signupUiState = viewModel.signupUiState,
            onItemValueChanged = viewModel::updateSignupUiState,
            onSignupBtnClicked = {
                coroutineScope.launch {
                    val responseResult = viewModel.requestSignup()

                    if (responseResult.isSuccess) {
                        // TODO. 회원가입 성공 처리
                        navigateToHome()
                    } else {
                        // TODO. 회원가입 성공 처리
                    }
                }
            },
            checkEmail = viewModel::checkEmail,
            checkNickname = viewModel::checkNickname
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
    checkEmail: suspend () -> Unit,
    checkNickname: suspend () -> Unit,
) {
    // Composable에 포커스가 있는지 확인하는 변수
    var isFocused by remember { mutableStateOf(false) }
    // 포커스 관리하는 포커스 매니저
    val focusManager = LocalFocusManager.current

    BackHandler {
        if (isFocused) focusManager.clearFocus() else navigateBack()
    }

    var defenderDoubleClick by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(key1 = defenderDoubleClick) {
        if (defenderDoubleClick) return@LaunchedEffect
        else delay(DOUBLE_CLICK_DELAY)

        defenderDoubleClick = true
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
            text = stringResource(id = R.string.signup_title_text),
            style = Typography.titleLarge,
            textAlign = TextAlign.Start
        )

        Box(modifier = Modifier.size(10.dp))

        SignupTextField(
            placeholderText = stringResource(id = R.string.email),
            value = signupUiState.email,
            signupUiState = signupUiState,
            onItemValueChanged = onItemValueChanged,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            checkEmail = checkEmail,
            checkNickname = checkNickname,
        )

        SignupTextField(
            placeholderText = stringResource(id = R.string.name),
            value = signupUiState.name,
            signupUiState = signupUiState,
            onItemValueChanged = onItemValueChanged,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            checkEmail = checkEmail,
            checkNickname = checkNickname,
        )

        SignupTextField(
            placeholderText = stringResource(id = R.string.password),
            value = signupUiState.password,
            signupUiState = signupUiState,
            onItemValueChanged = onItemValueChanged,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            checkEmail = checkEmail,
            checkNickname = checkNickname,
        )

        SignupTextField(
            placeholderText = stringResource(id = R.string.password_confirm),
            value = signupUiState.passwordConfirm,
            signupUiState = signupUiState,
            onItemValueChanged = onItemValueChanged,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            checkEmail = checkEmail,
            checkNickname = checkNickname,
        )

        SignupTextField(
            placeholderText = stringResource(id = R.string.nickname),
            value = signupUiState.nickname,
            signupUiState = signupUiState,
            onItemValueChanged = onItemValueChanged,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            checkEmail = checkEmail,
            checkNickname = checkNickname,
        )

        Box(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (defenderDoubleClick) {
                    defenderDoubleClick = false
                    onSignupBtnClicked()
                }
            },
            enabled = signupUiState.canSignup,
            modifier = Modifier
                .fillMaxWidth(),
            shape = ShapeDefaults.Medium,
        ) {
            Text(
                modifier = Modifier.padding(vertical = 7.dp),
                text = stringResource(id = R.string.signup),
                style = Typography.labelMedium,
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
    checkEmail: suspend () -> Unit,
    checkNickname: suspend () -> Unit,
) {
    // 포커스 관리하는 포커스 매니저
    val focusManager = LocalFocusManager.current
    // getString() 사용하기 위한 context
    val context = LocalContext.current
    var visualTransformation: VisualTransformation by remember {
        if (placeholderText == context.getString(R.string.password) || placeholderText == context.getString(
                R.string.password_confirm
            )
        ) {
            mutableStateOf(PasswordVisualTransformation())
        } else {
            mutableStateOf(VisualTransformation.None)
        }
    }
    val interactionSource = remember { MutableInteractionSource() }

    when (placeholderText) {
        context.getString(R.string.email) -> {
            LaunchedEffect(key1 = signupUiState.email) {
                delay(DEBOUNCE_TIME)
                if (signupUiState.email.isNotBlank()) {
                    checkEmail()
                }
            }
        }

        context.getString(R.string.nickname) -> {
            LaunchedEffect(key1 = signupUiState.nickname) {
                delay(DEBOUNCE_TIME)
                if (signupUiState.nickname.isNotBlank()) {
                    checkNickname()
                }
            }
        }
    }

    OutlinedTextField(
        value = value,
        onValueChange = {
            when (placeholderText) {
                context.getString(R.string.email) -> onItemValueChanged(signupUiState.copy(email = it))
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

                context.getString(R.string.nickname) -> onItemValueChanged(
                    signupUiState.copy(
                        nickname = it
                    )
                )
            }
        },
        label = {
            Text(
                text = placeholderText,
            )
        },
        trailingIcon = if (placeholderText == context.getString(R.string.password) || placeholderText == context.getString(
                R.string.password_confirm
            )
        ) {
            {
                if ((placeholderText == context.getString(R.string.password) && signupUiState.password.isNotEmpty())
                    || (placeholderText == context.getString(R.string.password_confirm) && signupUiState.passwordConfirm.isNotEmpty())
                ) {
                    Icon(
                        modifier = modifier
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                visualTransformation =
                                    if (visualTransformation == PasswordVisualTransformation()) {
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
                        tint = MaterialTheme.colorScheme.onPrimary
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
        isError = when (placeholderText) {
            context.getString(R.string.email) -> !signupUiState.canEmail
            context.getString(R.string.password_confirm) -> signupUiState.passwordConfirm.isNotEmpty() && !signupUiState.isPasswordMatch()
            context.getString(R.string.nickname) -> signupUiState.nickname.length > NICKNAME_MAX_LENGTH || !signupUiState.canNickname
            context.getString(R.string.password) -> signupUiState.password.isNotEmpty() && !signupUiState.isPasswordValid()
            context.getString(R.string.name) -> signupUiState.name.length > NAME_MAX_LENGTH
            else -> false
        },
        supportingText = {
            SignupSupportingText(placeholderText = placeholderText, signupUiState = signupUiState)
        },
        modifier = modifier
            .fillMaxWidth(),
    )
}

@Composable
fun SignupSupportingText(
    modifier: Modifier = Modifier,
    placeholderText: String,
    signupUiState: SignupUiState,
) {
    var isNeed = false

    var text = ""
    val context = LocalContext.current

    when (placeholderText) {
        context.getString(R.string.email) -> if (signupUiState.email.isNotEmpty() && !signupUiState.canEmail) {
            isNeed = true
            text = stringResource(id = R.string.exist_email_error)
        }

        context.getString(R.string.password_confirm) -> if (signupUiState.passwordConfirm.isNotEmpty() && signupUiState.password != signupUiState.passwordConfirm) {
            isNeed = true
            text = stringResource(id = R.string.password_match_error)
        }

        context.getString(R.string.nickname) -> if (signupUiState.nickname.isNotEmpty() && !signupUiState.canNickname) {
            isNeed = true
            text = stringResource(id = R.string.exist_nickname_error)
        } else if (signupUiState.nickname.length > NICKNAME_MAX_LENGTH) {
            isNeed = true
            text = stringResource(id = R.string.nickname_length_error)
        }

        context.getString(R.string.password) -> if (signupUiState.password.isNotEmpty() && !signupUiState.isPasswordValid()) {
            isNeed = true
            text = stringResource(id = R.string.password_rule)
        }

        context.getString(R.string.name) -> if (signupUiState.name.length > NAME_MAX_LENGTH) {
            isNeed = true
            text = stringResource(id = R.string.name_length_error)
        }
    }

    if (isNeed) {
        Text(
            modifier = modifier,
            text = text,
            color = Error
        )
    }
}

@Composable
@Preview
fun SignupPreview() {
    WaffleTheme {
        SignupScreen(
            navigateBack = { },
            navigateToHome = { },
        )
    }
}