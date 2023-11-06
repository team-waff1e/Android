package com.waff1e.waffle.member.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.waff1e.waffle.R
import com.waff1e.waffle.ui.WaffleTopAppBar
import com.waff1e.waffle.waffle.ui.waffle.WaffleBody
import com.waff1e.waffle.waffle.ui.waffle.WaffleViewModel

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
        ProfileBody(modifier = modifier.padding(innerPadding))
    }
}

@Composable
fun ProfileBody(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Text(text = stringResource(id = R.string.profile))
    }
}


