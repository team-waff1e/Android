package com.waff1e.waffle.waffle.ui.waffles

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waff1e.waffle.R
import com.waff1e.waffle.ui.WaffleDivider
import com.waff1e.waffle.ui.home.LoginButton
import com.waff1e.waffle.ui.home.SignupButton
import com.waff1e.waffle.ui.theme.Typography


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaffleListDrawerSheet(
    modifier: Modifier = Modifier,
    onLogoutClicked: () -> Unit,
) {
    ModalDrawerSheet(
        modifier = modifier
            .fillMaxWidth(0.8f),
        drawerShape = RectangleShape,
        drawerContainerColor = MaterialTheme.colorScheme.background,
        drawerTonalElevation = 0.dp
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(20.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Image(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onBackground),
                    painter = painterResource(id = R.drawable.person),
                    contentDescription = stringResource(id = R.string.profile_img),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.background)
                )

                Text(
                    text = "사용자명",
                    style = Typography.titleSmall.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = "1 팔로잉 0 팔로워",
                    style = Typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onPrimary.copy(
                            alpha = 0.5f
                        )
                    )
                )
            }

            WaffleDivider(
                topPadding = 40.dp,
                bottomPadding = 25.dp
            )

            Column {
                DrawerListItem(
                    imageVector = Icons.Filled.Person,
                    text = stringResource(id = R.string.profile)
                )

                DrawerListItem(
                    imageVector = Icons.Filled.Star,
                    text = stringResource(id = R.string.bookmark)
                )
            }

            WaffleDivider(
                topPadding = 25.dp,
                bottomPadding = 40.dp
            )

            Spacer(modifier = modifier.weight(1f))

            SignupButton(
                onClicked = { onLogoutClicked() },
                text = stringResource(id = R.string.logout)
            )
        }
    }
}

@Composable
fun DrawerListItem(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    text: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 15.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = modifier
                .size(25.dp),
            imageVector = imageVector,
            contentDescription = stringResource(id = R.string.drawer_item_icon_desc)
        )

        Text(
            text = text,
            style = Typography.titleMedium
        )
    }
}