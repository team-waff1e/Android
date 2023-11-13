package com.waff1e.waffle

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.waff1e.waffle.ui.WaffleApp
import com.waff1e.waffle.ui.theme.WaffleTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.JsonNull.content

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

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
                    WaffleApp()
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
        WaffleApp()
    }
}