package com.waff1e.waffle.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.waff1e.waffle.R
import com.waff1e.waffle.ui.navigation.WaffleNavHost

@Composable
fun WaffleApp(navController: NavHostController = rememberNavController()) {
    WaffleNavHost(navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaffleTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
) {
    TopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "뒤로가기"
                )
            }
        },
    )
}