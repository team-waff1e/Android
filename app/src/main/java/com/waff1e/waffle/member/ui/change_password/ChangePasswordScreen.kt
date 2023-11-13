package com.waff1e.waffle.member.ui.change_password

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.alpha
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waff1e.waffle.MainActivity.Companion.screenHeightDp
import com.waff1e.waffle.R
import com.waff1e.waffle.di.DOUBLE_CLICK_DELAY
import com.waff1e.waffle.ui.WaffleTopAppBar
import com.waff1e.waffle.ui.theme.Error
import com.waff1e.waffle.ui.theme.Typography
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    modifier: Modifier = Modifier,
    viewModel: ChangePasswordViewModel = hiltViewModel(),
    canNavigateBack: Boolean = true,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isProgress by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .background(Color.Transparent),
        topBar = {
            WaffleTopAppBar(
                hasNavigationIcon = canNavigateBack,
                navigationIconClicked = navigateBack,
                title = stringResource(id = R.string.change_pwd_btn)
            )
        }
    ) { innerPadding ->
        ChangePasswordBody(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            navigateBack = navigateBack,
            changePasswordUiState = viewModel.changePasswordUiState,
            onItemValueChanged = viewModel::updateChangePasswordUiState,
            onChangePasswordBtnClicked = {
                coroutineScope.launch {
                    isProgress = true

                    val responseResult = viewModel.requestUpdatePassword()

                    if (responseResult.isSuccess) {
                        // TODO. 비밀번호 변경 성공
                        Log.d("로그", "비밀번호 변경 성공")
                    } else if (responseResult.body?.errorCode == 1111) {
                        // TODO. 현재 비밀번호 불일치
                        Toast.makeText(context, "현재 비밀번호 불일치", Toast.LENGTH_SHORT).show()
                        Log.d("로그", "현재 비밀번호 불일치")
                    } else if (responseResult.body?.errorCode == 2222) {
                        // TODO. 비밀번호 변경 실패
                        Toast.makeText(context, "비밀번호 변경 실패", Toast.LENGTH_SHORT).show()
                        Log.d("로그", "비밀번호 변경 실패")
                    }

                    isProgress = false
                }
            },
            isProgress = { isProgress }
        )
    }
}

@Composable
fun ChangePasswordBody(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    changePasswordUiState: ChangePasswordUiState,
    onItemValueChanged: (ChangePasswordUiState) -> Unit,
    onChangePasswordBtnClicked: () -> Unit,
    isProgress: () -> Boolean,
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
            .padding(25.dp)
            .onFocusChanged { isFocused = it.hasFocus },
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UpdateProfileTextField(
            placeholderText = stringResource(id = R.string.current_pwd),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            value = changePasswordUiState.currentPwd,
            changePasswordUiState = changePasswordUiState,
            onItemValueChanged = onItemValueChanged
        )

        UpdateProfileTextField(
            placeholderText = stringResource(id = R.string.new_pwd),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            value = changePasswordUiState.newPwd,
            changePasswordUiState = changePasswordUiState,
            onItemValueChanged = onItemValueChanged
        )

        UpdateProfileTextField(
            placeholderText = stringResource(id = R.string.password_confirm),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            value = changePasswordUiState.newPwdConfirm,
            changePasswordUiState = changePasswordUiState,
            onItemValueChanged = onItemValueChanged
        )

        Box(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (defenderDoubleClick) {
                    defenderDoubleClick = false
                    onChangePasswordBtnClicked()
                }
            },
            enabled = changePasswordUiState.canUpdate,
            modifier = Modifier
                .fillMaxWidth(),
            shape = ShapeDefaults.Medium,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 7.dp)
                        .align(Alignment.Center)
                        .alpha(if (isProgress()) 0f else 1f),
                    text = stringResource(id = R.string.change_pwd_btn),
                    style = Typography.labelMedium,
                )

                CircularProgressIndicator(
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.Center)
                        .alpha(if (isProgress()) 1f else 0f),
                    color = MaterialTheme.colorScheme.onBackground,
                    strokeWidth = 3.dp
                )
            }
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
    changePasswordUiState: ChangePasswordUiState,
    onItemValueChanged: (ChangePasswordUiState) -> Unit,
) {
    // 포커스 관리하는 포커스 매니저
    val focusManager = LocalFocusManager.current
    // getString() 사용하기 위한 context
    val context = LocalContext.current
    var visualTransformation: VisualTransformation by remember {
        mutableStateOf(PasswordVisualTransformation())
    }
    val interactionSource = remember { MutableInteractionSource() }

    OutlinedTextField(
        value = value,
        onValueChange = {
            when (placeholderText) {
                context.getString(R.string.current_pwd) -> onItemValueChanged(
                    changePasswordUiState.copy(
                        currentPwd = it
                    )
                )

                context.getString(R.string.new_pwd) -> onItemValueChanged(
                    changePasswordUiState.copy(
                        newPwd = it
                    )
                )

                context.getString(R.string.password_confirm) -> onItemValueChanged(
                    changePasswordUiState.copy(newPwdConfirm = it)
                )
            }
        },
        label = {
            Text(text = placeholderText)
        },
        trailingIcon = {
            Row(
                modifier = Modifier
                    .wrapContentSize()
            ) {
                if ((placeholderText == context.getString(R.string.current_pwd)) && changePasswordUiState.currentPwd.isNotEmpty()
                    || (placeholderText == context.getString(R.string.new_pwd) && changePasswordUiState.newPwd.isNotEmpty())
                    || (placeholderText == context.getString(R.string.password_confirm) && changePasswordUiState.newPwdConfirm.isNotEmpty())
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
        },
        shape = ShapeDefaults.Medium,
        textStyle = Typography.bodyLarge,
        singleLine = true,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        isError = when (placeholderText) {
            context.getString(R.string.new_pwd) -> changePasswordUiState.newPwd.isNotEmpty() && !changePasswordUiState.isNewPwdValid()
            context.getString(R.string.password_confirm) -> changePasswordUiState.newPwdConfirm.isNotEmpty() && !changePasswordUiState.isMatchNewPwdConfirm()
            else -> false
        },
        supportingText = {
            UpdateProfileSupportingText(
                placeholderText = placeholderText,
                changePasswordUiState = changePasswordUiState
            )
        },
        modifier = modifier
            .fillMaxWidth(),
    )
}

@Composable
fun UpdateProfileSupportingText(
    modifier: Modifier = Modifier,
    placeholderText: String,
    changePasswordUiState: ChangePasswordUiState,
) {
    val context = LocalContext.current
    var isNeed = false
    var text = ""

    when (placeholderText) {
        context.getString(R.string.new_pwd) -> if (changePasswordUiState.newPwd.isNotEmpty() && !changePasswordUiState.isNewPwdValid()) {
            isNeed = true
            text = stringResource(id = R.string.password_rule)
        }

        context.getString(R.string.password_confirm) -> if (changePasswordUiState.newPwdConfirm.isNotEmpty() && changePasswordUiState.newPwd != changePasswordUiState.newPwdConfirm) {
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