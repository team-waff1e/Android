package com.waff1e.waffle.waffle.ui.waffles

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waff1e.waffle.R
import com.waff1e.waffle.ui.WaffleTopAppBar
import com.waff1e.waffle.ui.theme.Typography
import com.waff1e.waffle.waffle.dto.WaffleResponse
import kotlinx.datetime.toJavaLocalDateTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaffleListScreen(
    modifier: Modifier = Modifier,
    viewModel: WaffleListViewModel = hiltViewModel(),
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
            modifier = modifier.padding(innerPadding),
            viewModel = viewModel
        )
    }
}

@Composable
fun WafflesBody(
    modifier: Modifier = Modifier,
    viewModel: WaffleListViewModel,
) {
    WafflesLazyColumn(
        modifier = modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
        viewModel = viewModel,
    )
}

@Composable
fun WafflesLazyColumn(
    modifier: Modifier = Modifier,
    viewModel: WaffleListViewModel,
) {
    var isLoading by remember { mutableStateOf(true) }
    val list = viewModel.waffleListUiState.value.waffleList

    LaunchedEffect(key1 = isLoading, key2 = list) {
        if (list.isNotEmpty()) {
            isLoading = false
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        if (isLoading) {
            items(20) {
                LoadingWaffle()
            }
        } else {
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

                    Image(
                        modifier = Modifier
                            .size(20.dp),
                        painter = painterResource(id = R.drawable.more_vert),
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

@Composable
fun LoadingWaffle(
    modifier: Modifier = Modifier,
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
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .loadingEffect(),
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = modifier
                        .height(20.dp)
                        .fillMaxWidth()
                        .loadingEffect()
                )

                Box(
                    modifier = modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .loadingEffect()
                )

                Box(
                    modifier = modifier
                        .height(20.dp)
                        .fillMaxWidth()
                        .loadingEffect()
                )
            }
        }
    }
}

fun Modifier.loadingEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    val transition = rememberInfiniteTransition(label = "")
    val translateAnimation by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearOutSlowInEasing)
        ),
        label = ""
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color.LightGray.copy(alpha = 0.9f),
                Color.LightGray.copy(alpha = 0.4f),
            ),
            start = Offset(translateAnimation, translateAnimation),
            end = Offset(
                translateAnimation + size.width.toFloat(),
                translateAnimation + size.height.toFloat()
            ),
            tileMode = TileMode.Mirror
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}

@Composable
@Preview
fun WafflesPreview() {
    WaffleListScreen(
        onNavigateUp = { }
    )
}

@Composable
@Preview
fun LoadingWafflePreview() {
    LoadingWaffle()
}