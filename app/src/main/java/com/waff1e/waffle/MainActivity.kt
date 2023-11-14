package com.waff1e.waffle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.waff1e.waffle.di.LoginUserPreferenceModule
import com.waff1e.waffle.ui.WaffleApp
import com.waff1e.waffle.ui.theme.WaffleTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var loginUserPreference: LoginUserPreferenceModule

    companion object {
        var screenHeightDp = 0f
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val displayMetrics = resources.displayMetrics
        screenHeightDp = displayMetrics.heightPixels / displayMetrics.density

        setContent {
            WaffleTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorScheme.background)
                ) {
                    WaffleApp(
                        loginUserPreference = loginUserPreference
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WaffleTheme {
        WaffleApp(
            loginUserPreference = LoginUserPreferenceModule(LocalContext.current)
        )
    }
}