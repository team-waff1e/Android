package com.waff1e.waffle.waffle.ui.waffles

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.waff1e.waffle.ui.BackHandlerEndToast
import com.waff1e.waffle.ui.WaffleDivider
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
    navigateToWaffle: (Long) -> Unit,
    navigateToProfile: () -> Unit
) {
    Scaffold(
        topBar = {
            WaffleTopAppBar(
                hasNavigationIcon = true,
                navigationIconClicked = {
                    // TODO. 프로필 페이지 이동
                    navigateToProfile()
                },
                imageVector = Icons.Filled.AccountCircle
            )
        },
    ) { innerPadding ->
        WafflesBody(
            modifier = modifier
                .padding(innerPadding),
            viewModel = viewModel,
            onWaffleClick = navigateToWaffle
        )
    }
}

@Composable
fun WafflesBody(
    modifier: Modifier = Modifier,
    viewModel: WaffleListViewModel,
    onWaffleClick: (Long) -> Unit
) {
    BackHandlerEndToast()

    WafflesLazyColumn(
        modifier = modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
        viewModel = viewModel,
        onWaffleClick = { onWaffleClick(it.id) }
    )
}

@Composable
fun WafflesLazyColumn(
    modifier: Modifier = Modifier,
    viewModel: WaffleListViewModel,
    onWaffleClick: (WaffleResponse) -> Unit
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
        verticalArrangement = Arrangement.spacedBy(10.dp)
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
                WaffleListCard(
                    item = item,
                    onItemClick = onWaffleClick
                )
                
                Box(modifier = Modifier.size(10.dp))

                WaffleDivider()
            }
        }
    }
}

@Composable
fun WaffleListCard(
    modifier: Modifier = Modifier,
    item: WaffleResponse,
    onItemClick: (WaffleResponse) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick(item) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
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
                    .background(MaterialTheme.colorScheme.onBackground),
                painter = painterResource(id = R.drawable.person),
                contentDescription = "프로필 사진"
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.member.nickname,
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
                        painter = painterResource(id = R.drawable.more_vert),
                        contentDescription = stringResource(id = R.string.waffle_option),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Text(
                    text = item.content,
                    style = Typography.bodyMedium
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(40.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(20.dp),
                            painter = painterResource(id = R.drawable.chat_bubble),
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
                            painter = painterResource(id = R.drawable.favorite),
                            contentDescription = stringResource(id = R.string.likes_cnt),
                            tint = MaterialTheme.colorScheme.onBackground
                        )

                        Text(
                            text = item.likes.toString(),
                            style = Typography.bodyMedium
                        )
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
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
        navigateToWaffle = {  },
        navigateToProfile = {  },
    )
}

@Composable
@Preview
fun LoadingWafflePreview() {
    LoadingWaffle()
}