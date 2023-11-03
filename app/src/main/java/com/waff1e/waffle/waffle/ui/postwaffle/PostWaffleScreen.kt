package com.waff1e.waffle.waffle.ui.postwaffle

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waff1e.waffle.R
import com.waff1e.waffle.ui.WaffleTopAppBar
import com.waff1e.waffle.ui.theme.Typography
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostWaffleScreen(
    modifier: Modifier = Modifier,
    viewModel: PostWaffleViewModel = hiltViewModel(),
    canNavigationBack: Boolean = true,
    navigateBack: () -> Unit,
    navigateToWaffles: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val enableAction = remember { derivedStateOf { viewModel.content.isNotBlank() } }

    Scaffold(
        topBar = {
            WaffleTopAppBar(
                hasNavigationIcon = canNavigationBack,
                navigationIconClicked = navigateBack,
                title = "",
                imageVector = Icons.Filled.Close,
                hasAction = true,
                enableAction = enableAction.value,
                onAction = {
                    coroutineScope.launch {
                        val responseResult = viewModel.postWaffle()

                        if (responseResult.isSuccess) {
                            // TODO. 게시글 등록 성공 처리
                            navigateToWaffles()
                        } else {
                            // TODO. 게시글 등록 실패 처리

                        }
                    }
                }
            )
        },
    ) { innerPadding ->
        PostWaffleBody(
            modifier = modifier
                .padding(innerPadding),
            content = { viewModel.content },
            setContent = viewModel::setContent
        )
    }
}

@Composable
fun PostWaffleBody(
    modifier: Modifier = Modifier,
    content: () -> String,
    setContent: (String) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        Image(
            modifier = Modifier
                .padding(top = 10.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onBackground),
            imageVector = ImageVector.vectorResource(id = R.drawable.person),
            contentDescription = stringResource(id = R.string.profile_img),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.background)
        )

        PostWaffleTextField(
            content = content,
            setContent = setContent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostWaffleTextField(
    modifier: Modifier = Modifier,
    content: () -> String,
    setContent: (String) -> Unit,
) {
    TextField(
        value = content(),
        onValueChange = { setContent(it) },
        placeholder = {
            Text(
                text = stringResource(id = R.string.post_waffle_placeholder),
                style = Typography.displayMedium,
                color = Color.Gray
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        ),
        textStyle = Typography.displayMedium
    )
}