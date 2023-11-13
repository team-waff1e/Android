package com.waff1e.waffle.member.ui.change_nickname

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waff1e.waffle.R
import com.waff1e.waffle.di.DEBOUNCE_TIME
import com.waff1e.waffle.di.DOUBLE_CLICK_DELAY
import com.waff1e.waffle.ui.WaffleTopAppBar
import com.waff1e.waffle.ui.theme.Error
import com.waff1e.waffle.ui.theme.Typography
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeNicknameScreen(
    modifier: Modifier = Modifier,
    viewModel: ChangeNicknameViewModel = hiltViewModel(),
    canNavigateBack: Boolean = true,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isProgress by remember { mutableStateOf(false) }

    Scaffold(topBar = {
        WaffleTopAppBar(
            hasNavigationIcon = canNavigateBack,
            navigationIconClicked = navigateBack,
            title = stringResource(id = R.string.change_nickname_btn)
        )
    }) { innerPadding ->
        ChangeNicknameBody(
            modifier = modifier
                .padding(innerPadding),
            navigateBack = navigateBack,
            changeNicknameUiState = viewModel.changeNicknameUiState,
            onItemValueChanged = viewModel::updateChangeNicknameUiState,
            onChangeNicknameBtnClicked = {
                coroutineScope.launch {
                    isProgress = true

                    val responseResult = viewModel.requestChangeNickname()

                    if (responseResult.isSuccess) {
                        // TODO. 닉네임 변경 성공
                        Log.d("로그", "닉네임 변경 성공")
                        navigateBack()
                    } else if (responseResult.body?.errorCode == 1111) {
                        // TODO. 이미 사용중인 닉네임
                        Toast.makeText(context, "이미 사용중인 닉네임", Toast.LENGTH_SHORT).show()
                        Log.d("로그", "이미 사용중인 닉네임")
                    } else if (responseResult.body?.errorCode == 2222) {
                        // TODO. 닉네임 변경 실패
                        Toast.makeText(context, "닉네임 변경 실패", Toast.LENGTH_SHORT).show()
                        Log.d("로그", "닉네임 변경 실패")
                    }

                    isProgress = false
                }
            },
            isProgress = { isProgress },
            checkNickname = viewModel::checkNickname
        )
    }
}


@Composable
fun ChangeNicknameBody(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    changeNicknameUiState: ChangeNicknameUiState,
    onItemValueChanged: (ChangeNicknameUiState) -> Unit,
    onChangeNicknameBtnClicked: () -> Unit,
    isProgress: () -> Boolean,
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
        ChangeNicknameTextField(
            placeholderText = stringResource(id = R.string.nickname),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            value = changeNicknameUiState.nickname,
            changeNicknameUiState = changeNicknameUiState,
            onItemValueChanged = onItemValueChanged,
            checkNickname = checkNickname
        )

        Box(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (defenderDoubleClick) {
                    defenderDoubleClick = false
                    onChangeNicknameBtnClicked()
                }
            },
            enabled = changeNicknameUiState.canUpdate,
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
                    text = stringResource(id = R.string.change_nickname_btn),
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
fun ChangeNicknameTextField(
    modifier: Modifier = Modifier,
    placeholderText: String,
    keyboardOptions: KeyboardOptions,
    value: String,
    changeNicknameUiState: ChangeNicknameUiState,
    onItemValueChanged: (ChangeNicknameUiState) -> Unit,
    checkNickname: suspend () -> Unit,
) {
    // 포커스 관리하는 포커스 매니저
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = changeNicknameUiState.nickname) {
        delay(DEBOUNCE_TIME)
        if (changeNicknameUiState.nickname.isNotBlank()) {
            checkNickname()
        }
    }

    OutlinedTextField(
        value = value,
        onValueChange = {
            onItemValueChanged(
                changeNicknameUiState.copy(
                    nickname = it
                )
            )
        },
        label = {
            Text(text = placeholderText)
        },
        shape = ShapeDefaults.Medium,
        textStyle = Typography.bodyLarge,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        isError = false,
        supportingText = {
            ChangeNicknameSupportingText(
                changeNicknameUiState = changeNicknameUiState
            )
        },
        modifier = modifier
            .fillMaxWidth(),
    )
}

@Composable
fun ChangeNicknameSupportingText(
    modifier: Modifier = Modifier,
    changeNicknameUiState: ChangeNicknameUiState,
) {
    var isNeed = false
    val text = if (!changeNicknameUiState.canNickname) {
        isNeed = true
        stringResource(id = R.string.exist_nickname_error)
    } else if (changeNicknameUiState.nickname.isNotEmpty() && !changeNicknameUiState.isNicknameValid()) {
        isNeed = true
        stringResource(id = R.string.nickname_length_error)
    } else {
        ""
    }

    if (isNeed) {
        Text(
            modifier = modifier,
            text = text,
            color = Error
        )
    }
}