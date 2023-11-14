package com.waff1e.waffle.member.ui.edit_profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.waff1e.waffle.R
import com.waff1e.waffle.di.DOUBLE_CLICK_DELAY
import com.waff1e.waffle.ui.WaffleTopAppBar
import com.waff1e.waffle.ui.theme.Typography
import com.waff1e.waffle.utils.clickableSingle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: EditProfileViewModel = hiltViewModel(),
    canNavigationBack: Boolean = true,
    navigateBack: () -> Unit,
    navigateToProfileDetail: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToChangePassword: () -> Unit,
    navigateToChangeNickname: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var deleteConfirmDialogState by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        modifier = modifier
            .background(Color.Transparent),
        topBar = {
            WaffleTopAppBar(
                hasNavigationIcon = canNavigationBack,
                navigationIconClicked = navigateBack,
                title = stringResource(id = R.string.edit_profile)
            )
        },
    ) { innerPadding ->
        EditProfileBody(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            onProfileInfoClicked = navigateToProfileDetail,
            onLogoutClicked = {
                coroutineScope.launch {
                    viewModel.logout()
                    navigateToHome()
                }
            },
            onChangePasswordClicked = navigateToChangePassword,
            onChangeNicknameClicked = navigateToChangeNickname,
            onDeleteMyProfileClicked = {
                coroutineScope.launch {
                    val responseResult = viewModel.requestDeleteMyProfile()

                    if (responseResult.isSuccess) {
                        // TODO. 계정 삭제 성공
                        Log.d("로그", "계정 삭제 성공")
                        deleteConfirmDialogState = false
                        navigateToHome()
                    } else if (responseResult.body?.errorCode == 1111) {
                        // TODO. 비밀번호 불일치
                        Log.d("로그", "비밀번호 불일치")
                        Toast.makeText(context, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                    } else if (responseResult.body?.errorCode == 2222) {
                        // TODO. 계정 삭제 실패
                        Log.d("로그", "계정 삭제 실패")
                        deleteConfirmDialogState = false
                        Toast.makeText(context, "계정 삭제에 실패하였습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            pwd = { viewModel.pwd },
            updatePwd = viewModel::updatePwd,
            deleteConfirmDialogState = { deleteConfirmDialogState },
            showDeleteConfirmDialog = { deleteConfirmDialogState = true },
            hideDeleteConfirmDialog = { deleteConfirmDialogState = false }
        )
    }
}

@Composable
fun EditProfileBody(
    modifier: Modifier = Modifier,
    onProfileInfoClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
    onChangePasswordClicked: () -> Unit,
    onChangeNicknameClicked: () -> Unit,
    onDeleteMyProfileClicked: () -> Unit,
    pwd: () -> String,
    updatePwd: (String) -> Unit,
    deleteConfirmDialogState: () -> Boolean,
    showDeleteConfirmDialog: () -> Unit,
    hideDeleteConfirmDialog: () -> Unit
) {
    var showLogoutDialog by rememberSaveable { mutableStateOf(false) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        EditProfileItem(
            title = stringResource(id = R.string.profile_info),
            desc = stringResource(id = R.string.profile_info_desc),
            imageVector = Icons.Outlined.Person,
            onClicked = onProfileInfoClicked
        )

        EditProfileItem(
            title = stringResource(id = R.string.change_password),
            desc = stringResource(id = R.string.change_password_desc),
            imageVector = Icons.Outlined.Lock,
            onClicked = onChangePasswordClicked
        )

        EditProfileItem(
            title = stringResource(id = R.string.change_nickname),
            desc = stringResource(id = R.string.change_nickname_desc),
            imageVector = Icons.Outlined.Edit,
            onClicked = onChangeNicknameClicked
        )

        EditProfileItem(
            title = stringResource(id = R.string.logout),
            desc = stringResource(id = R.string.logout_desc),
            imageVector = Icons.Outlined.ExitToApp,
            onClicked = { showLogoutDialog = true }
        )

        EditProfileItem(
            title = stringResource(id = R.string.delete_account),
            desc = stringResource(id = R.string.delete_account_desc),
            imageVector = Icons.Outlined.Warning,
            onClicked = { showDeleteDialog = true }
        )
    }

    if (showLogoutDialog) {
        EditProfileDialog(
            onConfirmClicked = onLogoutClicked,
            onDismissRequest = { showLogoutDialog = false },
            title = stringResource(id = R.string.logout),
            content = stringResource(id = R.string.logout_dialog_desc),
            confirm = stringResource(id = R.string.logout),
            dismiss = stringResource(id = R.string.cancel)
        )
    }

    if (showDeleteDialog) {
        EditProfileDialog(
            onConfirmClicked = showDeleteConfirmDialog,
            onDismissRequest = { showDeleteDialog = false },
            title = stringResource(id = R.string.delete_account),
            content = stringResource(id = R.string.delete_dialog_desc),
            dismiss = stringResource(id = R.string.cancel),
            confirm = stringResource(id = R.string.delete)
        )
    }

    if (deleteConfirmDialogState()) {
        DeleteConfirmDialog(
            onConfirmClicked = onDeleteMyProfileClicked,
            onDismissRequest = hideDeleteConfirmDialog,
            title = stringResource(id = R.string.delete_account),
            content = stringResource(id = R.string.enter_password),
            dismiss = stringResource(id = R.string.cancel),
            confirm = stringResource(id = R.string.confirm),
            pwd = pwd,
            updatePwd = updatePwd
        )
    }
}

@Composable
fun EditProfileItem(
    modifier: Modifier = Modifier,
    title: String,
    desc: String,
    imageVector: ImageVector,
    onClicked: () -> Unit = { }
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickableSingle(disableRipple = true, onClick = { onClicked() }),
        horizontalArrangement = Arrangement.spacedBy(30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(30.dp),
            imageVector = imageVector,
            contentDescription = stringResource(id = R.string.edit_profile_item_icon),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = title,
                style = Typography.titleSmall
            )

            Text(
                text = desc,
                style = Typography.bodyMedium,
                color = MaterialTheme.colorScheme.inverseSurface
            )
        }
    }
}

@Composable
fun EditProfileDialog(
    modifier: Modifier = Modifier,
    onConfirmClicked: () -> Unit,
    onDismissRequest: () -> Unit,
    title: String,
    content: String,
    dismiss: String,
    confirm: String
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = title,
                style = Typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = content,
                style = Typography.titleSmall
            )


            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd),
                ) {
                    TextButton(
                        onClick = { onDismissRequest() }
                    ) {
                        Text(
                            text = dismiss,
                            style = Typography.titleSmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    TextButton(
                        onClick = {
                            onDismissRequest()
                            onConfirmClicked()
                        }
                    ) {
                        Text(
                            text = confirm,
                            style = Typography.titleSmall,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteConfirmDialog(
    modifier: Modifier = Modifier,
    onConfirmClicked: () -> Unit,
    onDismissRequest: () -> Unit,
    title: String,
    content: String,
    dismiss: String,
    confirm: String,
    pwd: () -> String,
    updatePwd: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var visualTransformation: VisualTransformation by remember {
        mutableStateOf(PasswordVisualTransformation())
    }
    val interactionSource = remember { MutableInteractionSource() }

    var defenderDoubleClick by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(key1 = defenderDoubleClick) {
        if (defenderDoubleClick) return@LaunchedEffect
        else delay(DOUBLE_CLICK_DELAY)

        defenderDoubleClick = true
    }

    Dialog(
        onDismissRequest = { onDismissRequest() },
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = title,
                style = Typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = content,
                style = Typography.titleSmall
            )

            OutlinedTextField(
                value = pwd(),
                onValueChange = { updatePwd(it) },
                shape = ShapeDefaults.Medium,
                textStyle = Typography.bodyLarge,
                singleLine = true,
                visualTransformation = visualTransformation,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                trailingIcon = {
                    if (pwd().isNotEmpty()) {
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
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd),
                ) {
                    TextButton(
                        onClick = { onDismissRequest() }
                    ) {
                        Text(
                            text = dismiss,
                            style = Typography.titleSmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    TextButton(
                        onClick = {
                            if (defenderDoubleClick) {
                                defenderDoubleClick = false
                                onConfirmClicked()
                            }
                        }
                    ) {
                        Text(
                            text = confirm,
                            style = Typography.titleSmall,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}