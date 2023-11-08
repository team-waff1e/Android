package com.waff1e.waffle.member.ui.change_password

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
import androidx.compose.ui.unit.dp
import com.waff1e.waffle.R
import com.waff1e.waffle.auth.ui.signup.SignupTextField
import com.waff1e.waffle.auth.ui.signup.SignupUiState
import com.waff1e.waffle.auth.ui.signup.SignupSupportingText
import com.waff1e.waffle.auth.ui.signup.isPasswordMatch
import com.waff1e.waffle.auth.ui.signup.isPasswordValid
import com.waff1e.waffle.di.DOUBLE_CLICK_DELAY
import com.waff1e.waffle.di.NAME_MAX_LENGTH
import com.waff1e.waffle.di.NICKNAME_MAX_LENGTH
import com.waff1e.waffle.member.ui.UpdateProfileUiState
import com.waff1e.waffle.ui.WaffleTopAppBar
import com.waff1e.waffle.ui.theme.Error
import com.waff1e.waffle.ui.theme.Typography
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    navigateBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar = {
        WaffleTopAppBar(
            hasNavigationIcon = canNavigateBack,
            navigationIconClicked = navigateBack
        )
    }) { innerPadding ->
        ChangePasswordBody(
            modifier = modifier
                .padding(innerPadding),
            navigateBack = navigateBack,
            updateProfileUiState = UpdateProfileUiState()
        )
    }
}

@Composable
fun ChangePasswordBody(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    updateProfileUiState: UpdateProfileUiState
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


        Box(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (defenderDoubleClick) {
                    defenderDoubleClick = false
                }
            },
            enabled = true,
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
fun UpdateProfileTextField(
    modifier: Modifier = Modifier,
    placeholderText: String,
    keyboardOptions: KeyboardOptions,
    value: String,
    signupUiState: SignupUiState,
    onItemValueChanged: (SignupUiState) -> Unit,
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

    OutlinedTextField(
        value = value,
        onValueChange = {
            when (placeholderText) {
                context.getString(R.string.password_confirm) -> onItemValueChanged(signupUiState.copy(passwordConfirm = it))
            }
        },
        label = {
            Text(text = placeholderText)
        },
        trailingIcon = if (placeholderText == context.getString(R.string.password) || placeholderText == context.getString(R.string.password_confirm)) {
            {
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                ) {
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
            context.getString(R.string.password_confirm) -> signupUiState.passwordConfirm.isNotEmpty() && !signupUiState.isPasswordMatch()
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
fun UpdateProfileSupportingText(
    modifier: Modifier = Modifier,
    placeholderText: String,
    signupUiState: SignupUiState,
) {
    val context = LocalContext.current
    var isNeed = false
    var text = ""

    when (placeholderText) {
        context.getString(R.string.password_confirm) -> if (signupUiState.passwordConfirm.isNotEmpty() && signupUiState.password != signupUiState.passwordConfirm) {
            isNeed = true
            text = stringResource(id = R.string.password_match_error)
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