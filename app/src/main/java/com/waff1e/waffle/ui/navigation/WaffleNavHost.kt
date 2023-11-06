package com.waff1e.waffle.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.waff1e.waffle.auth.ui.login.LoginScreen
import com.waff1e.waffle.auth.ui.signup.SignupScreen
import com.waff1e.waffle.member.ui.profile.ProfileScreen
import com.waff1e.waffle.ui.home.HomeScreen
import com.waff1e.waffle.ui.navigation.NavigationDestination.Home
import com.waff1e.waffle.ui.navigation.NavigationDestination.Login
import com.waff1e.waffle.ui.navigation.NavigationDestination.PostWaffle
import com.waff1e.waffle.ui.navigation.NavigationDestination.Signup
import com.waff1e.waffle.ui.navigation.NavigationDestination.Waffle
import com.waff1e.waffle.ui.navigation.NavigationDestination.Waffles
import com.waff1e.waffle.ui.navigation.NavigationDestination.Profile
import com.waff1e.waffle.utils.WaffleAnimation.fadeIn
import com.waff1e.waffle.utils.WaffleAnimation.fadeOut
import com.waff1e.waffle.utils.WaffleAnimation.slideInLeft
import com.waff1e.waffle.utils.WaffleAnimation.slideInUp
import com.waff1e.waffle.utils.WaffleAnimation.slideOutDown
import com.waff1e.waffle.utils.WaffleAnimation.slideOutRight
import com.waff1e.waffle.waffle.ui.postwaffle.PostWaffleScreen
import com.waff1e.waffle.waffle.ui.waffle.WaffleScreen
import com.waff1e.waffle.waffle.ui.waffles.WaffleListScreen

@Composable
fun WaffleNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Waffles.route,
        modifier = modifier,
        enterTransition = fadeIn,
        exitTransition = fadeOut,
        popEnterTransition = fadeIn,
        popExitTransition = fadeOut
    ) {
        // 메인화면
        composable(route = Home.route) {
            HomeScreen(
                navigateToLogin = { navController.navigate(Login.route) },
                navigateToSignup = { navController.navigate(Signup.route) }
            )
        }

        // 로그인 화면
        composable(
            route = Login.route,
            enterTransition = slideInLeft,
            popExitTransition = slideOutRight
        ) {
            LoginScreen(
                navigateBack = {
                    navController.popBackStack(route = Home.route, inclusive = false)
                },
                navigateToWaffles = {
                    navController.navigate(Waffles.route) {
                        popUpTo(Home.route) { inclusive = true }
                    }
                }
            )
        }

        // 회원가입 화면
        composable(
            route = Signup.route,
            enterTransition = slideInLeft,
            exitTransition = slideOutRight,
            popExitTransition = slideOutRight
        ) {
            SignupScreen(
                navigateBack = {
                    navController.popBackStack(
                        route = Home.route,
                        inclusive = false
                    )
                },
                navigateToHome = { navController.navigate(Home.route) }
            )
        }

        // Waffles(리스트) 화면
        composable(route = Waffles.route) {
            WaffleListScreen(
                navigateToWaffle = { navController.navigate(route = "${Waffle.route}/${it}") },
                navigateToProfile = {
                    navController.navigate(route = Profile.route) {
                        popUpTo(Waffles.route) { inclusive = false }
                    }
                },
                navigateToHome = {
                    navController.navigate(route = Home.route) {
                        popUpTo(Home.route) { inclusive = false }
                    }
                },
                navigateToPostWaffle = {
                    navController.navigate(PostWaffle.route) {
                        popUpTo(Waffles.route) { inclusive = false }
                    }
                }
            )
        }

        // Waffle (단일 게시글 조회)
        composable(
            route = "${Waffle.route}/{${Waffle.waffleArg}}",
            arguments = listOf(navArgument(Waffle.waffleArg) { type = NavType.LongType }),
            enterTransition = slideInLeft,
            popExitTransition = slideOutRight
        ) {
            WaffleScreen(
                navigateBack = {
                    navController.popBackStack(
                        route = Waffles.route,
                        inclusive = false
                    )
                },
            )
        }

        // PostWaffle (게시글 작성)
        composable(
            route = PostWaffle.route,
            enterTransition = slideInUp,
            exitTransition = slideOutDown,
            popEnterTransition = slideInUp,
            popExitTransition = slideOutDown
        ) {
            PostWaffleScreen(
                navigateBack = {
                    navController.popBackStack(
                        route = Waffles.route,
                        inclusive = false
                    )
                },
                navigateToWaffles = {
                    navController.navigate(route = Waffles.route) {
                        popUpTo(Home.route) { inclusive = false }
                    }
                }
            )
        }

        // 프로필
        composable(
            route = Profile.route,
            enterTransition = slideInLeft,
            popExitTransition = slideOutRight
        ) {
            ProfileScreen(
                navigateBack = {
                    navController.popBackStack(
                        route = Waffles.route,
                        inclusive = false
                    )
                },
            )
        }
    }
}