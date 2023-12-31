package com.waff1e.waffle.member.ui.profile

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.waff1e.waffle.R
import com.waff1e.waffle.di.DOUBLE_CLICK_DELAY
import com.waff1e.waffle.ui.ProfileTopAppBar
import com.waff1e.waffle.ui.WaffleEditDeleteMenu
import com.waff1e.waffle.ui.WaffleReportMenu
import com.waff1e.waffle.ui.theme.Typography
import com.waff1e.waffle.utils.GlideImagePlaceholder
import com.waff1e.waffle.utils.TabItem
import com.waff1e.waffle.utils.updateLikes
import com.waff1e.waffle.waffle.ui.waffles.WaffleListFAB
import com.waff1e.waffle.waffle.ui.waffles.WaffleListUiState
import com.waff1e.waffle.waffle.ui.waffles.WafflesBody
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalGlideComposeApi::class
)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    canNavigationBack: Boolean = true,
    navigateToWaffle: (Long) -> Unit,
    navigateToPostWaffle: () -> Unit,
    navigateToEditWaffle: (Long) -> Unit,
    navigateToEditProfile: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val scrollState = rememberScrollState()
    var isFABExpanded by remember { mutableStateOf(false) }

    val canRefresh by remember {
        derivedStateOf {
            !scrollState.canScrollBackward
        }
    }
    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            coroutineScope.launch {
                isRefreshing = true
                viewModel.getWaffleListByMemberId(true)
                isRefreshing = false
            }
        }
    )

    var showEditDeletePopUpMenu by remember { mutableStateOf(false) }
    var showReportPopUpMenu by remember { mutableStateOf(false) }
    var clickedWaffleId by remember { mutableLongStateOf(0L) }

    val showTopAppbarTitle by remember {
        derivedStateOf { !scrollState.canScrollForward }
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                var blockScroll = true

                if (available.y < -1) {
                    blockScroll = scrollState.canScrollForward
                }

                if (available.y > 1) {
                    blockScroll = scrollState.canScrollBackward && !lazyListState.canScrollBackward
                }

                return if (blockScroll) {
                    scrollState.dispatchRawDelta(available.y * -1)
                    Offset(0f, available.y)
                } else {
                    Offset.Zero
                }
            }
        }
    }

    BackHandler {
        if (isFABExpanded) {
            isFABExpanded = false
        } else {
            navigateBack()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        GlideImage(
            modifier = Modifier
                .fillMaxHeight(fraction = 0.4f)
                .background(MaterialTheme.colorScheme.onBackground),
            model = "https://images.dog.ceo/breeds/terrier-border/n02093754_2959.jpg",
            contentScale = ContentScale.Crop,
            transition = CrossFade,
            loading = GlideImagePlaceholder(),
            failure = GlideImagePlaceholder(),
            contentDescription = stringResource(id = R.string.profile_img),
        )

        Scaffold(
            modifier = modifier,
            topBar = {
                ProfileTopAppBar(
                    hasNavigationIcon = canNavigationBack,
                    navigationIconClicked = navigateBack,
                    onAction = { navigateToEditProfile() },
                    actionIcon = Icons.Filled.Settings,
                    profile = { viewModel.profile },
                    myWaffleListUiState = { viewModel.waffleListUiState },
                    showTopAppbarTitle = showTopAppbarTitle
                )
            },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                WaffleListFAB(
                    isFABVisible = { true },
                    isFABExpanded = { isFABExpanded },
                    changeFabExpandedState = { isFABExpanded = true },
                    navigateToPostWaffle = navigateToPostWaffle
                )
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            ProfileBody(
                modifier = modifier
                    .background(Color.Transparent)
                    .fillMaxSize()
                    .padding(innerPadding),
                profile = { viewModel.profile },
                getWaffleList = viewModel::getWaffleListByMemberId,
                waffleListUiState = { viewModel.waffleListUiState },
                onWaffleClick = navigateToWaffle,
                onLikeBtnClicked = { id ->
                    viewModel.waffleListUiState.waffleList.updateLikes(id)

                    coroutineScope.launch {
                        viewModel.requestWaffleLike(id)
                    }
                },
                changeClickedWaffleId = { id ->
                    clickedWaffleId = id
                },
                changeShowEditDeletePopUpMenu = {
                    showEditDeletePopUpMenu = true
                },
                changeShowReportPopUpMenu = {
                    showReportPopUpMenu = true
                },
                isMyProfile = viewModel.isMyProfile,
                canRefresh = canRefresh,
                isRefreshing = isRefreshing,
                pullRefreshState = pullRefreshState,
                topPadding = innerPadding.calculateTopPadding(),
                nestedScrollConnection = nestedScrollConnection,
                scrollState = scrollState,
                lazyListState = lazyListState,
            )
        }

        AnimatedVisibility(
            visible = showEditDeletePopUpMenu,
            enter = slideInVertically(
                initialOffsetY = { it * 2 }
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }
            )
        ) {
            WaffleEditDeleteMenu(
                onDismiss = { showEditDeletePopUpMenu = false },
                onEditClicked = {
                    navigateToEditWaffle(clickedWaffleId)
                },
                onDeleteClicked = {
                    coroutineScope.launch {
                        viewModel.removeWaffle(clickedWaffleId)
                    }
                },
            )
        }

        AnimatedVisibility(
            visible = showReportPopUpMenu,
            enter = slideInVertically(
                initialOffsetY = { it * 2 }
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }
            )
        ) {
            WaffleReportMenu(
                onDismiss = { showReportPopUpMenu = false },
                onReportClicked = { },
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileBody(
    modifier: Modifier = Modifier,
    profile: () -> ProfileUiState,
    waffleListUiState: () -> WaffleListUiState,
    getWaffleList: suspend (Boolean) -> Unit,
    onWaffleClick: (Long) -> Unit,
    onLikeBtnClicked: suspend (Long) -> Unit,
    changeClickedWaffleId: (Long) -> Unit,
    changeShowEditDeletePopUpMenu: () -> Unit,
    changeShowReportPopUpMenu: () -> Unit,
    isMyProfile: Boolean,
    canRefresh: Boolean,
    isRefreshing: Boolean,
    pullRefreshState: PullRefreshState,
    topPadding: Dp,
    nestedScrollConnection: NestedScrollConnection,
    scrollState: ScrollState,
    lazyListState: LazyListState,
) {
    // TODO. 팔로우 처리 필요
    var follow by remember {
        mutableStateOf(false)
    }

    BoxWithConstraints {
        val screenHeight = maxHeight - topPadding

        Column(
            modifier = modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Image(
                        modifier = Modifier
                            .size(60.dp)
                            .background(MaterialTheme.colorScheme.onBackground),
                        imageVector = ImageVector.vectorResource(id = R.drawable.person),
                        contentDescription = stringResource(id = R.string.profile_img),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.background)
                    )

                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )

                    if (!isMyProfile) {
                        AnimatedContent(targetState = follow, label = "") { targetState ->
                            FollowButton(
                                onAction = {
                                    // TODO. 팔로우 버튼 클릭 처리 필요
                                    follow = !follow
                                },
                                isFollow = targetState
                            )
                        }
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = profile().member?.nickname ?: "",
                        style = Typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "게시물 ${waffleListUiState().waffleList.size}개",
                        style = Typography.bodyMedium
                    )
                }
            }

            ProfileTab(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .height(screenHeight)
                    .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                myWaffleListUiState = waffleListUiState,
                getMyWaffleList = getWaffleList,
                onWaffleClick = onWaffleClick,
                onLikeBtnClicked = onLikeBtnClicked,
                changeClickedWaffleId = changeClickedWaffleId,
                changeShowEditDeletePopUpMenu = changeShowEditDeletePopUpMenu,
                changeShowReportPopUpMenu = changeShowReportPopUpMenu,
                canRefresh = canRefresh,
                isRefreshing = isRefreshing,
                pullRefreshState = pullRefreshState,
                nestedScrollConnection = nestedScrollConnection,
                lazyListState = lazyListState,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun ProfileTab(
    modifier: Modifier = Modifier,
    myWaffleListUiState: () -> WaffleListUiState,
    getMyWaffleList: suspend (Boolean) -> Unit,
    onWaffleClick: (Long) -> Unit,
    onLikeBtnClicked: suspend (Long) -> Unit,
    changeClickedWaffleId: (Long) -> Unit,
    changeShowEditDeletePopUpMenu: () -> Unit,
    changeShowReportPopUpMenu: () -> Unit,
    canRefresh: Boolean,
    isRefreshing: Boolean,
    pullRefreshState: PullRefreshState,
    nestedScrollConnection: NestedScrollConnection,
    lazyListState: LazyListState,
) {
    val coroutineScope = rememberCoroutineScope()
    val tabItems = listOf(TabItem.Waffle, TabItem.Comment, TabItem.Like)
    var selectedTabIdx by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState { tabItems.size }
    val context = LocalContext.current

    Column(
        modifier = modifier
    ) {
        TabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = selectedTabIdx
        ) {
            tabItems.forEachIndexed { idx, type ->
                Tab(
                    selected = (idx == selectedTabIdx),
                    onClick = {
                        coroutineScope.launch {
                            selectedTabIdx = idx
                            pagerState.animateScrollToPage(idx)
                        }
                    },
                    text = {
                        Text(
                            text = when (type) {
                                TabItem.Waffle -> stringResource(id = R.string.tab_item_type_waffle)
                                TabItem.Comment -> stringResource(id = R.string.tab_item_type_comment)
                                TabItem.Like -> stringResource(id = R.string.tab_item_type_like)
                            },
                            fontWeight = if (idx == selectedTabIdx) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    selectedContentColor = MaterialTheme.colorScheme.onBackground,
                    unselectedContentColor = MaterialTheme.colorScheme.inverseSurface,
                    interactionSource = object : MutableInteractionSource {
                        override val interactions: Flow<Interaction> = emptyFlow()

                        override suspend fun emit(interaction: Interaction) {}

                        override fun tryEmit(interaction: Interaction) = true
                    }
                )
            }
        }

        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth(),
            state = pagerState,
            userScrollEnabled = false
        ) { idx ->
            when (idx) {
                0 -> {
                    WafflesBody(
                        onWaffleClick = { onWaffleClick(it.id) },
                        list = myWaffleListUiState().waffleList,
                        getWaffleList = getMyWaffleList,
                        canNestedScroll = true,
                        nestedScrollConnection = nestedScrollConnection,
                        onLikeBtnClicked = {
                            coroutineScope.launch {
                                onLikeBtnClicked(it)
                            }
                        },
                        onShowPopUpMenuClicked = { isMine, id ->
                            changeClickedWaffleId(id)

                            if (isMine) changeShowEditDeletePopUpMenu() else changeShowReportPopUpMenu()
                        },
                        onProfileImageClicked = { _ ->
                            // TODO. 페이지에서 프로필 이미지 클릭 시 처리 필요
                            Toast.makeText(context, "같은 페이지 이동 불가!", Toast.LENGTH_SHORT).show()
                        },
                        canRefresh = canRefresh,
                        isRefreshing = isRefreshing,
                        pullRefreshState = pullRefreshState,
                        lazyListState = lazyListState,
                    )
                }

                1 -> {
                    // TODO. 답글 페이지 표시
                }

                2 -> {
                    // TODO. 좋아요 페이지 표시
                }
            }
        }
    }
}

@Composable
fun FollowButton(
    modifier: Modifier = Modifier,
    onAction: () -> Unit,
    isFollow: Boolean,
) {
    val buttonContentColor by animateColorAsState(
        if (isFollow) MaterialTheme.colorScheme.onBackground else Color.White,
        label = ""
    )

    val buttonContainerColorColor by animateColorAsState(
        if (isFollow) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary,
        label = ""
    )

    var defenderDoubleClick by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(key1 = defenderDoubleClick) {
        if (defenderDoubleClick) return@LaunchedEffect
        else delay(DOUBLE_CLICK_DELAY)

        defenderDoubleClick = true
    }

    Button(
        modifier = modifier
            .defaultMinSize(minHeight = 1.dp, minWidth = 1.dp),
        onClick = {
            if (defenderDoubleClick) {
                defenderDoubleClick = false
                onAction()
            }
        },
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(
            contentColor = buttonContentColor,
            containerColor = buttonContainerColorColor
        ),
        border = if (isFollow) {
            BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground
            )
        } else {
            null
        }
    ) {
        Text(
            modifier = modifier
                .padding(horizontal = 15.dp, vertical = 5.dp),
            text = if (isFollow) stringResource(id = R.string.follow) else stringResource(id = R.string.unfollow),
            style = Typography.titleSmall,
        )
    }
}