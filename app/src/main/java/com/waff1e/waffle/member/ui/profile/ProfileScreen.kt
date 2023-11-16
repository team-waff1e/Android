package com.waff1e.waffle.member.ui.profile

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waff1e.waffle.R
import com.waff1e.waffle.ui.WaffleEditDeleteMenu
import com.waff1e.waffle.ui.WaffleReportMenu
import com.waff1e.waffle.ui.WaffleTopAppBar
import com.waff1e.waffle.ui.theme.Typography
import com.waff1e.waffle.utils.TabItem
import com.waff1e.waffle.utils.TopAppbarType
import com.waff1e.waffle.utils.updateLikes
import com.waff1e.waffle.waffle.ui.waffles.WaffleListFAB
import com.waff1e.waffle.waffle.ui.waffles.WaffleListUiState
import com.waff1e.waffle.waffle.ui.waffles.WafflesBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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

    var showEditDeletePopUpMenu by remember { mutableStateOf(false) }
    var showReportPopUpMenu by remember { mutableStateOf(false) }
    var clickedWaffleId by remember { mutableLongStateOf(0L) }

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
        Scaffold(
            modifier = modifier
                .background(Color.Transparent),
            topBar = {
                WaffleTopAppBar(
                    hasNavigationIcon = canNavigationBack,
                    navigationIconClicked = navigateBack,
                    title = "",
                    type = TopAppbarType.Profile,
                    onAction = { navigateToEditProfile() },
                    actionIcon = Icons.Filled.Settings
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
            ProfileBody(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                myProfile = { viewModel.profile },
                getMyWaffleList = viewModel::getWaffleListByMemberId,
                myWaffleListUiState = { viewModel.waffleListUiState },
                onWaffleClick = navigateToWaffle,
                nestedScrollConnection = nestedScrollConnection,
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
                }
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

@Composable
fun ProfileBody(
    modifier: Modifier = Modifier,
    myProfile: () -> ProfileUiState,
    myWaffleListUiState: () -> WaffleListUiState,
    getMyWaffleList: suspend (Boolean) -> Unit,
    onWaffleClick: (Long) -> Unit,
    nestedScrollConnection: NestedScrollConnection,
    onLikeBtnClicked: suspend (Long) -> Unit,
    changeClickedWaffleId: (Long) -> Unit,
    changeShowEditDeletePopUpMenu: () -> Unit,
    changeShowReportPopUpMenu: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
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
                Text(
                    text = myProfile().member?.nickname ?: "",
                    style = Typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "게시물 ${myWaffleListUiState().waffleList.size}개",
                    style = Typography.bodyMedium
                )
            }
        }

        ProfileTab(
            myWaffleListUiState = myWaffleListUiState,
            getMyWaffleList = getMyWaffleList,
            onWaffleClick = onWaffleClick,
            nestedScrollConnection = nestedScrollConnection,
            onLikeBtnClicked = onLikeBtnClicked,
            changeClickedWaffleId = changeClickedWaffleId,
            changeShowEditDeletePopUpMenu = changeShowEditDeletePopUpMenu,
            changeShowReportPopUpMenu = changeShowReportPopUpMenu
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileTab(
    modifier: Modifier = Modifier,
    myWaffleListUiState: () -> WaffleListUiState,
    getMyWaffleList: suspend (Boolean) -> Unit,
    onWaffleClick: (Long) -> Unit,
    nestedScrollConnection: NestedScrollConnection,
    onLikeBtnClicked: suspend (Long) -> Unit,
    changeClickedWaffleId: (Long) -> Unit,
    changeShowEditDeletePopUpMenu: () -> Unit,
    changeShowReportPopUpMenu: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val tabItems = listOf(TabItem.Waffle, TabItem.Comment, TabItem.Like)
    var selectedTabIdx by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState { tabItems.size }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        TabRow(
            modifier = modifier,
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
            modifier = modifier
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
                        onProfileImageClicked = { _: String? -> }
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