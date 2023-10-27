package com.waff1e.waffle.waffle.ui.waffle

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waff1e.waffle.R
import com.waff1e.waffle.ui.WaffleTopAppBar
import com.waff1e.waffle.waffle.dto.Member
import com.waff1e.waffle.waffle.dto.WaffleResponse
import com.waff1e.waffle.waffle.ui.waffles.LoadingWaffle
import com.waff1e.waffle.waffle.ui.waffles.WaffleCard
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaffleScreen(
    modifier: Modifier = Modifier,
    viewModel: WaffleViewModel = hiltViewModel(),
    canNavigationBack: Boolean = true,
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
) {
    Scaffold(
        topBar = {
            WaffleTopAppBar(
                title = stringResource(id = R.string.waffle),
                canNavigationBack = canNavigationBack,
                navigateUp = onNavigateUp
            )
        },
    ) { innerPadding ->
        WaffleBody(
            modifier = modifier
                .padding(innerPadding),
            viewModel = viewModel
        )
    }
}

@Composable
fun WaffleBody(
    modifier: Modifier = Modifier,
    viewModel: WaffleViewModel
) {
    val waffleUiState = viewModel.waffleUiState

    Log.d("로그", " - WaffleBody() 호출됨 - ${waffleUiState.value.waffle}")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        if (waffleUiState.value.waffle == null && waffleUiState.value.errorCode == null) {
            LoadingWaffle()
        } else if (waffleUiState.value.waffle != null) {
            WaffleCard(
                item = waffleUiState.value.waffle!!,
                onItemClick = {}
            )
        } else {
            // TODO. 응답 오류 처리 필요
            Text(text = "응답오류")
        }

        Text(text = "댓글은 아래에 표시할 예정")
    }
}

@Preview
@Composable
fun WaffleCardPreview(
    modifier: Modifier = Modifier,
) {
    val waffleResponse = WaffleResponse(
        id = 1,
        content = "내용입니다1",
        createdAt = LocalDateTime.now().toKotlinLocalDateTime(),
        updatedAt = LocalDateTime.now().toKotlinLocalDateTime(),
        likes = 1000,
        comments = 100,
        member = Member(
            nickname = "테스트 유저1",
            profileUrl = "프로필 URL1"
        )
    )

    WaffleCard(
        item = waffleResponse,
        onItemClick = {}
    )
}