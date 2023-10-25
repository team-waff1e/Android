package com.waff1e.waffle.ui.waffles

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.waff1e.waffle.R
import com.waff1e.waffle.dto.Member
import com.waff1e.waffle.dto.WaffleResponse
import com.waff1e.waffle.ui.WaffleTopAppBar
import com.waff1e.waffle.ui.theme.Typography
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WafflesScreen(
    modifier: Modifier = Modifier,
    canNavigationBack: Boolean = true,
    onNavigateUp: () -> Unit,
) {
    Scaffold(
        topBar = {
            WaffleTopAppBar(
                title = stringResource(id = R.string.app_name),
                canNavigationBack = canNavigationBack,
                navigateUp = onNavigateUp
            )
        },
    ) { innerPadding ->
        WafflesBody(
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun WafflesBody(
    modifier: Modifier = Modifier,
) {
    WafflesLazyColumn(
        modifier = modifier
            .padding(horizontal = 10.dp)
    )
}

@Composable
fun WafflesLazyColumn(
    modifier: Modifier = Modifier,
) {
    val list = makeFakeList()

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        itemsIndexed(
            items = list,
            key = { _, item ->
                item.id
            }
        ) { _, item ->
            WaffleCard(
                modifier = modifier,
                item = item,
            )
        }
    }
}

@Composable
fun WaffleCard(
    modifier: Modifier = Modifier,
    item: WaffleResponse,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Image(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.Green),
                painter = painterResource(id = R.drawable.baseline_person_24),
                contentDescription = "프로필 사진"
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.member.nickname,
                            style = Typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        // TODO. 시간, 일, 주, 달 순으로 디테일하게 변경하도록 추가
                        val diff = ChronoUnit.HOURS.between(item.createdAt, LocalDateTime.now())

                        val dateString = if (diff >= 24) {
                            item.createdAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                        } else {
                            "${diff}h"
                        }

                        Text(
                            text = dateString,
                            color = Color.Gray
                        )
                    }

                    Image(
                        modifier = Modifier
                            .size(20.dp),
                        painter = painterResource(id = R.drawable.more_vert_fill0_wght400_grad0_opsz24),
                        contentDescription = "게시글 옵션"
                    )
                }

                Text(text = item.content)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(40.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Image(
                            modifier = Modifier
                                .size(20.dp),
                            painter = painterResource(id = R.drawable.baseline_chat_bubble_outline_24),
                            contentDescription = "댓글 수"
                        )

                        Text(text = item.comments.toString())
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Image(
                            modifier = Modifier
                                .size(20.dp),
                            painter = painterResource(id = R.drawable.baseline_favorite_border_24),
                            contentDescription = "좋아요 수"
                        )

                        Text(text = item.likes.toString())
                    }
                }
            }
        }
    }
}

// TODO. 더미 리스트 생성 메소드
fun makeFakeList(): List<WaffleResponse> {
    val list = mutableListOf<WaffleResponse>()

    for (i in 1..100) {
        list.add(
            WaffleResponse(
                i.toLong(),
                "테스트${i} 내용입니다~~~~~테스트${i} 내용입니다~~~~~테스트${i} 내용입니다~~~~~테스트${i} 내용입니다~~~~~테스트${i} 내용입니다~~~~~테스트${i} 내용입니다~~~~~테스트${i} 내용입니다~~~~~",
                LocalDateTime.now(),
                LocalDateTime.now(),
                1000,
                100,
                Member("테스트 유저 $i", "테스트 url $i")
            )
        )
    }

    return list
}

@Composable
@Preview
fun WafflesPreview() {
    WafflesScreen(
        onNavigateUp = { }
    )
}