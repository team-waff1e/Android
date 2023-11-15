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
import com.waff1e.waffle.di.LoginUserPreferenceModule
import com.waff1e.waffle.member.ui.change_nickname.ChangeNicknameScreen
import com.waff1e.waffle.member.ui.change_password.ChangePasswordScreen
import com.waff1e.waffle.member.ui.edit_profile.EditProfileScreen
import com.waff1e.waffle.member.ui.profile.ProfileScreen
import com.waff1e.waffle.member.ui.profile_detail.ProfileDetailScreen
import com.waff1e.waffle.ui.home.HomeScreen
import com.waff1e.waffle.ui.navigation.NavigationDestination.ChangePassword
import com.waff1e.waffle.ui.navigation.NavigationDestination.EditProfile
import com.waff1e.waffle.ui.navigation.NavigationDestination.Home
import com.waff1e.waffle.ui.navigation.NavigationDestination.Login
import com.waff1e.waffle.ui.navigation.NavigationDestination.PostWaffle
import com.waff1e.waffle.ui.navigation.NavigationDestination.Profile
import com.waff1e.waffle.ui.navigation.NavigationDestination.ProfileDetail
import com.waff1e.waffle.ui.navigation.NavigationDestination.Signup
import com.waff1e.waffle.ui.navigation.NavigationDestination.Waffle
import com.waff1e.waffle.ui.navigation.NavigationDestination.Waffles
import com.waff1e.waffle.ui.navigation.NavigationDestination.ChangeNickname
import com.waff1e.waffle.ui.navigation.NavigationDestination.EditWaffle
import com.waff1e.waffle.utils.WaffleAnimation.fadeIn
import com.waff1e.waffle.utils.WaffleAnimation.fadeOut
import com.waff1e.waffle.utils.WaffleAnimation.slideInLeft
import com.waff1e.waffle.utils.WaffleAnimation.slideInUp
import com.waff1e.waffle.utils.WaffleAnimation.slideOutDown
import com.waff1e.waffle.utils.WaffleAnimation.slideOutRight
import com.waff1e.waffle.waffle.ui.editwaffle.EditWaffleScreen
import com.waff1e.waffle.waffle.ui.postwaffle.PostWaffleScreen
import com.waff1e.waffle.waffle.ui.waffle.WaffleScreen
import com.waff1e.waffle.waffle.ui.waffles.WaffleListScreen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Composable
fun WaffleNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    loginUserPreference: LoginUserPreferenceModule,
) {
    val jsessionid = runBlocking {
        loginUserPreference.jsessionidFlow.first()
    }

    NavHost(
        navController = navController,
        startDestination = if (jsessionid != null) Waffles.route else Home.route,
        modifier = modifier,
        enterTransition = fadeIn,
        exitTransition = fadeOut,
        popEnterTransition = fadeIn,
        popExitTransition = fadeOut
    ) {
        // 메인화면
        composable(route = Home.route) {
            HomeScreen(
                navigateToLogin = {
                    navController.navigate(Login.route) {
                        launchSingleTop = true
                    }
                },
                navigateToSignup = {
                    navController.navigate(Signup.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // 로그인 화면
        composable(
            route = Login.route,
            enterTransition = slideInLeft,
            exitTransition = slideOutRight,
            popExitTransition = slideOutRight,
            popEnterTransition = fadeIn,
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
            popExitTransition = slideOutRight,
            popEnterTransition = fadeIn,
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
        composable(
            route = Waffles.route
        ) {
            WaffleListScreen(
                navigateToWaffle = {
                    navController.navigate(route = "${Waffle.route}/${it}") {
                        launchSingleTop = true
                    }
                },
                navigateToProfile = {
                    navController.navigate(route = Profile.route) {
                        launchSingleTop = true
                        popUpTo(Waffles.route) { inclusive = false }
                    }
                },
                navigateToHome = {
                    navController.navigate(route = Home.route) {
                        launchSingleTop = true
                        popUpTo(Home.route) { inclusive = false }
                    }
                },
                navigateToPostWaffle = {
                    navController.navigate(PostWaffle.route) {
                        launchSingleTop = true
                    }
                },
                navigateToEditWaffle = {
                    navController.navigate(route = "${EditWaffle.route}/${it}") {
                        launchSingleTop = true
                    }
                }
            )
        }

        // Waffle (단일 게시글 조회)
        composable(
            route = "${Waffle.route}/{${Waffle.waffleArg}}",
            arguments = listOf(navArgument(Waffle.waffleArg) { type = NavType.LongType }),
            enterTransition = slideInLeft,
            exitTransition = fadeOut,
            popExitTransition = slideOutRight,
            popEnterTransition = fadeIn,
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
            popExitTransition = slideOutDown,
            popEnterTransition = slideInUp,
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
            exitTransition = fadeOut,
            popExitTransition = slideOutRight,
            popEnterTransition = fadeIn,
        ) {
            ProfileScreen(
                navigateBack = {
                    navController.popBackStack(
                        route = Waffles.route,
                        inclusive = false
                    )
                },
                navigateToWaffle = { navController.navigate(route = "${Waffle.route}/${it}") },
                navigateToPostWaffle = {
                    navController.navigate(PostWaffle.route) {
                        launchSingleTop = true
                    }
                },
                navigateToEditProfile = {
                    navController.navigate(route = EditProfile.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // 프로필 정보 수정
        composable(
            route = EditProfile.route,
            enterTransition = slideInLeft,
            exitTransition = fadeOut,
            popExitTransition = slideOutRight,
            popEnterTransition = fadeIn,
        ) {
            EditProfileScreen(
                navigateBack = {
                    navController.popBackStack(
                        route = Profile.route,
                        inclusive = false
                    )
                },
                navigateToProfileDetail = {
                    navController.navigate(route = ProfileDetail.route) {
                        launchSingleTop = true
                    }
                },
                navigateToHome = {
                    navController.navigate(route = Home.route) {
                        launchSingleTop = true
                        popUpTo(Home.route) { inclusive = false }
                    }
                },
                navigateToChangePassword = {
                    navController.navigate(route = ChangePassword.route) {
                        launchSingleTop = true
                    }
                },
                navigateToChangeNickname = {
                    navController.navigate(route = ChangeNickname.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // 계정 정보
        composable(
            route = ProfileDetail.route,
            enterTransition = slideInLeft,
            popEnterTransition = slideInLeft,
            exitTransition = slideOutRight,
            popExitTransition = slideOutRight,
        ) {
            ProfileDetailScreen(
                navigateBack = {
                    navController.popBackStack(
                        route = EditProfile.route,
                        inclusive = false
                    )
                }
            )
        }

        // 비밀번호 변경
        composable(
            route = ChangePassword.route,
            enterTransition = slideInLeft,
            popEnterTransition = slideInLeft,
            exitTransition = slideOutRight,
            popExitTransition = slideOutRight,
        ) {
            ChangePasswordScreen(
                navigateBack = {
                    navController.popBackStack(
                        route = EditProfile.route,
                        inclusive = false
                    )
                }
            )
        }

        // 닉네임 변경
        composable(
            route = ChangeNickname.route,
            enterTransition = slideInLeft,
            popEnterTransition = slideInLeft,
            exitTransition = slideOutRight,
            popExitTransition = slideOutRight,
        ) {
            ChangeNicknameScreen(
                navigateBack = {
                    navController.popBackStack(
                        route = EditProfile.route,
                        inclusive = false
                    )
                }
            )
        }

        // 와플 수정
        composable(
            route = "${EditWaffle.route}/{${EditWaffle.waffleArg}}",
            arguments = listOf(navArgument(EditWaffle.waffleArg) { type = NavType.LongType }),
            enterTransition = slideInLeft,
            popEnterTransition = slideInLeft,
            exitTransition = slideOutRight,
            popExitTransition = slideOutRight,
        ) {
            EditWaffleScreen(
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
    }
}