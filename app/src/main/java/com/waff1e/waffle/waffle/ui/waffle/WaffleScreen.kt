package com.waff1e.waffle.waffle.ui.waffle

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waff1e.waffle.R
import com.waff1e.waffle.comment.dto.Comment
import com.waff1e.waffle.ui.PostWaffleButton
import com.waff1e.waffle.ui.WaffleDivider
import com.waff1e.waffle.ui.WaffleTopAppBar
import com.waff1e.waffle.ui.theme.Typography
import com.waff1e.waffle.utils.clickableSingle
import com.waff1e.waffle.utils.isEnd
import com.waff1e.waffle.waffle.dto.Waffle
import com.waff1e.waffle.waffle.dto.updateLikes
import com.waff1e.waffle.waffle.ui.waffles.LoadingWaffle
import kotlinx.coroutines.launch
import kotlinx.datetime.toJavaLocalDateTime
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
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier
            .background(Color.Transparent),
        topBar = {
            WaffleTopAppBar(
                hasNavigationIcon = canNavigationBack,
                navigationIconClicked = navigateBack
            )
        },
    ) { innerPadding ->
        WaffleBody(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            waffleUiState = { viewModel.waffleUiState },
            onLikeBtnClicked = { id ->
                viewModel.waffleUiState.waffle?.updateLikes()

                coroutineScope.launch {
                    viewModel.requestWaffleLike(id)
                }
            },
            getCommentList = viewModel::getCommentList,
            list = viewModel.waffleUiState.commentList,
            navigateBack = navigateBack,
            commentContent = viewModel.commentContent,
            onCommentChange = {
                viewModel.commentContent = it
            },
            onPostCommentBtnClicked = {
                coroutineScope.launch {
                    viewModel.postComment()
                    viewModel.commentContent = ""
                }
            }
        )
    }
}

@Composable
fun WaffleBody(
    modifier: Modifier = Modifier,
    waffleUiState: () -> WaffleUiState,
    onLikeBtnClicked: (Long) -> Unit,
    list: List<Comment>,
    getCommentList: suspend () -> Unit,
    navigateBack: () -> Unit,
    commentContent: String,
    onCommentChange: (String) -> Unit,
    onPostCommentBtnClicked: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val waffle = waffleUiState()
    var isInitializing by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()
    val isEnd by remember { derivedStateOf { lazyListState.isEnd() } }

    BackHandler {
        if (isFocused) focusManager.clearFocus() else navigateBack()
    }

    if (isEnd) {
        LaunchedEffect(Unit) {
            isLoading = true
            getCommentList()
            isLoading = false
        }
    } else if (isLoading) { // 스크롤 하다가 갑자기 올리면 CircularProgressIndicator 사라지지 않는 문제 대응
        isLoading = false
    }

    LaunchedEffect(key1 = isInitializing, key2 = list) {
        if (list.isNotEmpty()) {
            isInitializing = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
    ) {
        Box(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                .weight(1f)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    if (waffle.waffle == null && waffle.errorCode == null) {
                        LoadingWaffle()
                    } else if (waffle.waffle != null) {
                        WaffleCard(
                            item = waffle.waffle,
                            onLikeBtnClicked = onLikeBtnClicked
                        )
                    } else {
                        // TODO. 응답 오류 처리 필요
                        Text(text = "응답 오류")
                    }
                }

                if (isInitializing) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(25.dp)
                                    .align(Alignment.Center),
                                strokeWidth = 3.dp
                            )
                        }
                    }
                } else {
                    itemsIndexed(
                        items = list,
                        key = { _, item ->
                            item.id
                        }
                    ) { _, item ->
                        CommentCard(
                            item = item,
                            onItemClick = { },
                        )
                    }
                }
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = modifier
                        .size(25.dp)
                        .align(Alignment.BottomCenter),
                    strokeWidth = 3.dp
                )
            }
        }

        CommentTextField(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, bottom = 5.dp),
            isFocused = { isFocused },
            changeFocus = { hasFocus ->
                isFocused = hasFocus
            },
            commentContent = commentContent,
            onCommentChange = onCommentChange,
            onPostCommentBtnClicked = onPostCommentBtnClicked
        )
    }
}

@Composable
fun WaffleCard(
    modifier: Modifier = Modifier,
    item: Waffle,
    onLikeBtnClicked: (Long) -> Unit,
) {
    var isLike by remember {
        mutableStateOf(item.liked)
    }

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
                            text = item.owner.nickname!!,
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
                            color = Color.Gray,
                            style = Typography.bodyMedium
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

    Box(modifier = modifier.size(30.dp))
    WaffleDivider()
    Box(modifier = modifier.size(20.dp))

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
                text = item.commentCount.toString(),
                style = Typography.bodyMedium
            )
        }

        Row(
            modifier = Modifier
                .clickableSingle {
                    isLike = !isLike
                    onLikeBtnClicked(item.id)
                },
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(20.dp),
                imageVector = ImageVector.vectorResource(
                    id =
                    if (isLike) R.drawable.favorite else R.drawable.empty_favorite
                ),
                contentDescription = stringResource(id = R.string.likes_cnt),
                tint = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = item.likesCount.toString(),
                style = Typography.bodyMedium
            )
        }
    }

    Box(modifier = modifier.size(20.dp))
    WaffleDivider()
}

@Composable
fun CommentCard(
    modifier: Modifier = Modifier,
    item: Comment,
    onItemClick: (Comment) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickableSingle(disableRipple = true) { onItemClick(item) },
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Image(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onBackground),
                imageVector = ImageVector.vectorResource(id = R.drawable.person),
                contentDescription = stringResource(id = R.string.profile_img),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.background)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier,
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
                                color = Color.Gray,
                                style = Typography.bodyMedium,
                            )
                        }

                        Spacer(modifier = modifier.weight(1f))

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
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentTextField(
    modifier: Modifier = Modifier,
    isFocused: () -> Boolean,
    changeFocus: (Boolean) -> Unit,
    commentContent: String,
    onCommentChange: (String) -> Unit,
    onPostCommentBtnClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .height(IntrinsicSize.Min),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        // TODO. 답글 다는 기능
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { changeFocus(it.hasFocus) },
            value = commentContent,
            onValueChange = {
                onCommentChange(it)
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.post_comment),
                    style = Typography.titleSmall
                )
            },
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colorScheme.onBackground,
                containerColor = MaterialTheme.colorScheme.background,
            ),
            textStyle = Typography.titleSmall
        )

        if (isFocused()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 10.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.image),
                    contentDescription = stringResource(id = R.string.add_image),
                    tint = MaterialTheme.colorScheme.onBackground
                )

                PostWaffleButton(
                    onAction = onPostCommentBtnClicked,
                    enableAction = commentContent.isNotBlank(),
                    text = "답글"
                )
            }
        }
    }
}