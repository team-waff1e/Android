package com.waff1e.waffle.waffle.ui.waffles

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waff1e.waffle.R
import com.waff1e.waffle.ui.WaffleDivider
import com.waff1e.waffle.ui.WaffleTopAppBar
import com.waff1e.waffle.ui.isEnd
import com.waff1e.waffle.ui.loadingEffect
import com.waff1e.waffle.ui.theme.Typography
import com.waff1e.waffle.waffle.dto.WaffleResponse
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
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
    navigateToProfile: () -> Unit,
    navigateToHome: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var backWait = 0L
    val context = LocalContext.current

    BackHandler {
        if (drawerState.isOpen) {
            scope.launch {
                drawerState.apply { close() }
            }
        } else if (System.currentTimeMillis() - backWait >= 2000) {
            backWait = System.currentTimeMillis()
            Toast.makeText(
                context,
                context.getText(R.string.exit_toast_message),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            (context as? Activity)?.finish()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            WaffleListDrawerSheet(
                onLogoutClicked = navigateToHome
            )
        },
        scrimColor = Color.Black.copy(alpha = 0.7f)
    ) {
        Scaffold(
            modifier = modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                WaffleTopAppBar(
                    hasNavigationIcon = true,
                    navigationIconClicked = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },
                    imageVector = Icons.Filled.AccountCircle,
                    scrollBehavior = scrollBehavior
                )
            },
        ) { innerPadding ->
            WafflesBody(
                modifier = modifier
                    .padding(innerPadding),
                onWaffleClick = navigateToWaffle,
                waffleListUiState = { viewModel.waffleListUiState },
                getWaffleList = viewModel::getWaffleList,
            )
        }
    }
}

@Composable
fun WafflesBody(
    modifier: Modifier = Modifier,
    onWaffleClick: (Long) -> Unit,
    waffleListUiState: () -> State<WaffleListUiState>,
    getWaffleList: suspend (Boolean) -> Unit,
) {
    WafflesLazyColumn(
        modifier = modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
        onWaffleClick = { onWaffleClick(it.id) },
        waffleListUiState = waffleListUiState,
        getWaffleList = getWaffleList,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WafflesLazyColumn(
    modifier: Modifier = Modifier,
    onWaffleClick: (WaffleResponse) -> Unit,
    waffleListUiState: () -> State<WaffleListUiState>,
    getWaffleList: suspend (Boolean) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(true) }
    val list = waffleListUiState().value.waffleList
    val listState = rememberLazyListState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = {
            coroutineScope.launch {
                getWaffleList(true)
            }
        }
    )

    val isEnd by remember { derivedStateOf { listState.isEnd() } }

    if (isEnd) {
        LaunchedEffect(Unit) {
            getWaffleList(false)
        }
    }

    LaunchedEffect(key1 = isLoading, key2 = list) {
        if (list.isNotEmpty()) {
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            state = if (isLoading) rememberLazyListState() else listState,
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
                        item = { item },
                        onItemClick = onWaffleClick
                    )

                    Box(modifier = Modifier.size(10.dp))

                    WaffleDivider()
                }
            }
        }

        PullRefreshIndicator(
            refreshing = isLoading,
            state = pullRefreshState
        )
    }
}

@Composable
fun WaffleListCard(
    modifier: Modifier = Modifier,
    item: () -> WaffleResponse,
    onItemClick: (WaffleResponse) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick(item()) },
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
                contentDescription = stringResource(id = R.string.profile_img),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.background)
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
                            text = item().member.nickname,
                            style = Typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )

                        // TODO. 시간, 일, 주, 달 순으로 디테일하게 변경하도록 추가
                        val diff = ChronoUnit.HOURS.between(
                            item().createdAt.toJavaLocalDateTime(),
                            LocalDateTime.now()
                        )

                        val dateString = if (diff >= 24) {
                            item().createdAt.toJavaLocalDateTime()
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
                    text = item().content,
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
                            text = item().comments.toString(),
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
                            text = item().likes.toString(),
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

@Composable
@Preview
fun WafflesPreview() {
    WaffleListScreen(
        navigateToWaffle = { },
        navigateToProfile = { },
        navigateToHome = { },
    )
}

@Composable
@Preview
fun LoadingWafflePreview() {
    LoadingWaffle()
}