package com.waff1e.waffle.waffle.ui.waffles

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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
import com.waff1e.waffle.waffle.dto.Waffle
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
    navigateToPostWaffle: () -> Unit,
    navigateToHome: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // BackHandler 관련
    var backWait = 0L
    val context = LocalContext.current

    // FAB 관련
    var isFABVisible by remember { mutableStateOf(true) }
    var isFABExpanded by remember { mutableStateOf(false) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < -1) {
                    coroutineScope.launch {
                        isFABVisible = false
                        isFABExpanded = false
                    }
                }
                if (available.y > 1)
                    isFABVisible = true
                return Offset.Zero
            }
        }
    }

    BackHandler {
        if (drawerState.isOpen) {
            coroutineScope.launch {
                drawerState.apply { close() }
            }
        } else if (isFABExpanded) {
            isFABExpanded = false
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
                onLogoutClicked = navigateToHome,
                onProfileClicked = navigateToProfile
            )
        },
        scrimColor = Color.Black.copy(alpha = 0.7f)
    ) {
        Scaffold(
            modifier = modifier
                .background(Color.Transparent)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                WaffleTopAppBar(
                    hasNavigationIcon = true,
                    navigationIconClicked = {
                        coroutineScope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },
                    imageVector = Icons.Filled.AccountCircle,
                    scrollBehavior = scrollBehavior
                )
            },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                WaffleListFAB(
                    isFABVisible = { isFABVisible },
                    isFABExpanded = { isFABExpanded },
                    changeFabExpandedState = { isFABExpanded = true },
                    navigateToPostWaffle = navigateToPostWaffle
                )
            }
        ) { innerPadding ->
            WafflesBody(
                modifier = modifier
                    .padding(innerPadding),
                onWaffleClick = navigateToWaffle,
                waffleListUiState = { viewModel.waffleListUiState },
                getWaffleList = viewModel::getWaffleList,
                nestedScrollConnection = nestedScrollConnection,
            )
        }
    }
}

@Composable
fun WafflesBody(
    modifier: Modifier = Modifier,
    onWaffleClick: (Long) -> Unit,
    waffleListUiState: () -> WaffleListUiState,
    getWaffleList: suspend (Boolean) -> Unit,
    nestedScrollConnection: NestedScrollConnection,
) {
    WafflesLazyColumn(
        modifier = modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
        onWaffleClick = { onWaffleClick(it.id) },
        list = waffleListUiState().waffleList,
        getWaffleList = getWaffleList,
        nestedScrollConnection = nestedScrollConnection,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WafflesLazyColumn(
    modifier: Modifier = Modifier,
    onWaffleClick: (Waffle) -> Unit,
    list: List<Waffle>,
    getWaffleList: suspend (Boolean) -> Unit,
    nestedScrollConnection: NestedScrollConnection,
) {
    val coroutineScope = rememberCoroutineScope()
    var isInitializing by remember { mutableStateOf(true) }
    var isRefreshing by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            coroutineScope.launch {
                isRefreshing = true
                getWaffleList(true)
                isRefreshing = false
            }
        }
    )

    LaunchedEffect(key1 = isInitializing, key2 = list) {
        if (list.isNotEmpty()) {
            isInitializing = false
        }
    }

    val isEnd by remember { derivedStateOf { lazyListState.isEnd() } }

    if (isEnd) {
        LaunchedEffect(Unit) {
            isLoading = true
            getWaffleList(false)
            isLoading = false
        }
    } else if (isLoading) { // 스크롤 하다가 갑자기 올리면 CircularProgressIndicator 사라지지 않는 문제 대응
        isLoading = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            state = lazyListState,
        ) {
            if (isInitializing) {
                items(20) {
                    LoadingWaffle()
                }
            } else {
                itemsIndexed(
                    items = list,
                    key = { _, item ->
                        item.id
                    },
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

        PullRefreshIndicator(
            modifier = modifier,
            refreshing = isRefreshing,
            state = pullRefreshState
        )

        if (isLoading) {
            CircularProgressIndicator(
                modifier = modifier
                    .size(25.dp)
                    .align(Alignment.BottomCenter),
                strokeWidth = 3.dp
            )
        }
    }
}

@Composable
fun WaffleListCard(
    modifier: Modifier = Modifier,
    item: Waffle,
    onItemClick: (Waffle) -> Unit,
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
                imageVector = ImageVector.vectorResource(id = R.drawable.person),
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
fun WaffleListFAB(
    modifier: Modifier = Modifier,
    isFABVisible: () -> Boolean,
    isFABExpanded: () -> Boolean,
    changeFabExpandedState: () -> Unit,
    navigateToPostWaffle: () -> Unit,
) {
    val durationMillis = 500

    AnimatedVisibility(
        visible = isFABVisible(),
//        enter = slideInVertically(initialOffsetY = { it * 2 }),
//        exit = slideOutVertically(targetOffsetY = { it * 2 }),
        enter = scaleIn(tween(durationMillis = durationMillis)) + fadeIn(tween(durationMillis = durationMillis)) + slideInHorizontally(
            tween(durationMillis = durationMillis), initialOffsetX = { -it / 2 }),
        exit = scaleOut(tween(durationMillis = durationMillis)) + fadeOut(tween(durationMillis = durationMillis)) + slideOutHorizontally(
            tween(durationMillis = durationMillis), targetOffsetX = { -it / 2 }),
    ) {
        ExtendedFloatingActionButton(
            modifier = modifier,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.fab_desc)
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.post_waffle),
                    style = Typography.bodyLarge
                )
            },
            expanded = isFABExpanded(),
            onClick = {
                if (!isFABExpanded()) {
                    changeFabExpandedState()
                } else {
                    navigateToPostWaffle()
                }
            },
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        )
    }
}

@Composable
@Preview
fun WafflesPreview() {
    WaffleListScreen(
        navigateToWaffle = { },
        navigateToProfile = { },
        navigateToHome = { },
        navigateToPostWaffle = { },
    )
}

@Composable
@Preview
fun LoadingWafflePreview() {
    LoadingWaffle()
}