package com.waff1e.waffle.member.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waff1e.waffle.R
import com.waff1e.waffle.ui.WaffleTopAppBar
import com.waff1e.waffle.ui.theme.Typography
import com.waff1e.waffle.waffle.ui.waffles.WafflesLazyColumn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    canNavigationBack: Boolean = true,
) {
    Scaffold(
        topBar = {
            WaffleTopAppBar(
                hasNavigationIcon = canNavigationBack,
                navigationIconClicked = navigateBack,
                title = ""
            )
        },
    ) { innerPadding ->
        ProfileBody(
            modifier = modifier.padding(innerPadding)
        ) { viewModel.myProfile }
    }
}

@Composable
fun ProfileBody(
    modifier: Modifier = Modifier,
    myProfile: () -> ProfileUiState,
) {
    Column(
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
    ) {
        Image(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onBackground),
            imageVector = ImageVector.vectorResource(id = R.drawable.person),
            contentDescription = stringResource(id = R.string.profile_img),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.background)
        )

        Text(
            text = myProfile().member?.nickname ?: "",
            style = Typography.bodyMedium
        )
    }
}


