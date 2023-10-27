package com.waff1e.waffle.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.waff1e.waffle.ui.home.HomeScreen
import com.waff1e.waffle.auth.ui.login.LoginScreen
import com.waff1e.waffle.ui.navigation.NavigationDestination.Home
import com.waff1e.waffle.ui.navigation.NavigationDestination.Login
import com.waff1e.waffle.ui.navigation.NavigationDestination.Signup
import com.waff1e.waffle.auth.ui.signup.SignupScreen
import com.waff1e.waffle.waffle.ui.waffles.WaffleListScreen
import com.waff1e.waffle.ui.navigation.NavigationDestination.Waffles
import com.waff1e.waffle.ui.navigation.NavigationDestination.Waffle
import com.waff1e.waffle.waffle.ui.waffle.WaffleScreen

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

        // Waffles(리스트) 화면
        composable(route = Waffles.route) {
            WaffleListScreen(
                onNavigateUp = { navController.navigateUp() },
                navigateToWaffle = { navController.navigate(route = "${Waffle.route}/${it}") }
            )
        }

        // Waffle
        composable(
            route = "${Waffle.route}/{${Waffle.waffleArg}}",
            arguments = listOf(navArgument(Waffle.waffleArg) { type = NavType.LongType })
        ) {
            WaffleScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }
    }
}