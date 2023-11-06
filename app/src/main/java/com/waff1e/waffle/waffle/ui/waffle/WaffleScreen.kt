package com.waff1e.waffle.waffle.ui.waffle

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waff1e.waffle.R
import com.waff1e.waffle.member.dto.Member
import com.waff1e.waffle.ui.WaffleDivider
import com.waff1e.waffle.ui.WaffleTopAppBar
import com.waff1e.waffle.ui.theme.Typography
import com.waff1e.waffle.waffle.dto.Waffle
import com.waff1e.waffle.waffle.ui.waffles.LoadingWaffle
import com.waff1e.waffle.waffle.ui.waffles.WaffleListCard
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaffleScreen(
    modifier: Modifier = Modifier,
    viewModel: WaffleViewModel = hiltViewModel(),
    canNavigationBack: Boolean = true,
    navigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            WaffleTopAppBar(
                hasNavigationIcon = canNavigationBack,
                navigationIconClicked = navigateBack
            )
        },
    ) { innerPadding ->
        WaffleBody(
            modifier = modifier
                .padding(innerPadding),
            waffleUiState = { viewModel.waffleUiState }
        )
    }
}

@Composable
fun WaffleBody(
    modifier: Modifier = Modifier,
    waffleUiState: () -> WaffleUiState
) {
    val waffle = waffleUiState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        if (waffle.waffle == null && waffle.errorCode == null) {
            LoadingWaffle()
        } else if (waffle.waffle != null) {
            WaffleCard(
                item = waffle.waffle,
            )
        } else {
            // TODO. 응답 오류 처리 필요
            Text(text = "응답오류")
        }

        Text(text = "댓글은 아래에 표시할 예정")
    }
}

@Composable
fun WaffleCard(
    modifier: Modifier = Modifier,
    item: Waffle,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 10.dp, end = 10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = item.member.nickname!!,
                            style = Typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )

                        // TODO. 시간, 일, 주, 달 순으로 디테일하게 변경하도록 추가
                        val diff = ChronoUnit.HOURS.between(
                            item.createdAt.toJavaLocalDateTime(),
                            LocalDateTime.now()
                        )

                        val dateString = if (diff >= 24) {
                            item.createdAt.toJavaLocalDateTime()
                                .format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                        } else {
                            "${diff}h"
                        }

                        Text(
                            text = dateString,
                            color = Color.Gray
                        )
                    }

                    Icon(
                        modifier = Modifier
                            .size(20.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.more_vert),
                        contentDescription = stringResource(id = R.string.waffle_option),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Text(
                text = item.content,
                style = Typography.bodyLarge
            )
        }
    }

    WaffleDivider()

    Row(
        modifier = Modifier.padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.chat_bubble),
                contentDescription = stringResource(id = R.string.comments_cnt),
                tint = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = item.comments.toString(),
                style = Typography.bodyMedium
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.favorite),
                contentDescription = stringResource(id = R.string.likes_cnt),
                tint = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = item.likes.toString(),
                style = Typography.bodyMedium
            )
        }
    }

    WaffleDivider()
}

@Preview
@Composable
fun WaffleCardPreview(
    modifier: Modifier = Modifier,
) {
    val waffle = Waffle(
        id = 1,
        content = "내용입니다1",
        createdAt = LocalDateTime.now().toKotlinLocalDateTime(),
        updatedAt = LocalDateTime.now().toKotlinLocalDateTime(),
        likes = 1000,
        comments = 100,
        member = Member(
            nickname = "테스트 유저1",
            profileUrl = "프로필 URL1",
        ),
        isLike = false,
    )

    WaffleListCard(
        item = waffle,
        onItemClick = {}
    )
}