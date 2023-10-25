package com.waff1e.waffle.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.waff1e.waffle.ui.home.HomeScreen
import com.waff1e.waffle.auth.ui.login.LoginScreen
import com.waff1e.waffle.ui.navigation.NavigationDestination.Home
import com.waff1e.waffle.ui.navigation.NavigationDestination.Login
import com.waff1e.waffle.ui.navigation.NavigationDestination.Signup
import com.waff1e.waffle.auth.ui.signup.SignupScreen
import com.waff1e.waffle.waffles.ui.waffles.WafflesScreen
import com.waff1e.waffle.ui.navigation.NavigationDestination.Waffles

@Composable
fun WaffleNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        }
    ) {
        // TODO. navigate 관련 설정 추가 필요
        // 메인화면
        composable(route = Home.route) {
            HomeScreen(
                canNavigateBack = false,
                navigateToLogin = { navController.navigate(Login.route) },
                navigateToSignup = { navController.navigate(Signup.route) },
                navigateToWaffles = { navController.navigate(Waffles.route) })
        }

        // 로그인 화면
        composable(route = Login.route) {
            LoginScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        // 회원가입 화면
        composable(route = Signup.route) {
            SignupScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        // Waffle 화면
        composable(route = Waffles.route) {
            WafflesScreen(
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}