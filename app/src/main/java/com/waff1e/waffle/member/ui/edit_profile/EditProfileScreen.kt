package com.waff1e.waffle.member.ui.edit_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
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
        )
    }
}

@Composable
fun EditProfileBody(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        EditProfileItem(
            title = "계정 정보",
            desc = "닉네임과 이메일 주소와 같은 계정 정보를 조회 합니다",
            imageVector = Icons.Outlined.Person
        )

        EditProfileItem(
            title = "비밀번호 변경하기",
            desc = "언제든지 비밀번호를 변경하세요",
            imageVector = Icons.Outlined.Lock
        )

        EditProfileItem(
            title = "로그아웃",
            desc = "계정을 안전하게 로그아웃 하세요",
            imageVector = Icons.Outlined.ExitToApp
        )

        EditProfileItem(
            title = "계정 삭제하기",
            desc = "한 번 삭제한 계정은 되돌릴 수 없습니다",
            imageVector = Icons.Outlined.Warning
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