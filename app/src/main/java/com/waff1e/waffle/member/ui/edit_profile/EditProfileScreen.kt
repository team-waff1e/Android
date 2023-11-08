package com.waff1e.waffle.member.ui.edit_profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.waff1e.waffle.R
import com.waff1e.waffle.ui.WaffleTopAppBar
import com.waff1e.waffle.ui.theme.Typography
import com.waff1e.waffle.utils.clickableSingle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    canNavigationBack: Boolean = true,
    navigateBack: () -> Unit,
    navigateToProfileDetail: () -> Unit,
    navigateToHome: () -> Unit,
) {
    Scaffold(
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
                .padding(innerPadding),
            onProfileInfoClicked = navigateToProfileDetail,
            onLogoutClicked = navigateToHome,
        )
    }
}

@Composable
fun EditProfileBody(
    modifier: Modifier = Modifier,
    onProfileInfoClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
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
            imageVector = Icons.Outlined.Lock
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
            onConfirmClicked = { /*TODO*/ },
            onDismissRequest = { showDeleteDialog = false },
            title = stringResource(id = R.string.delete_account),
            content = stringResource(id = R.string.delete_dialog_desc),
            dismiss = stringResource(id = R.string.cancel),
            confirm = stringResource(id = R.string.delete)
        )
    }
}

@Composable
fun EditProfileItem(
    modifier: Modifier = Modifier,
    title: String,
    desc: String,
    imageVector: ImageVector,
    onClicked: () -> Unit = { },
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