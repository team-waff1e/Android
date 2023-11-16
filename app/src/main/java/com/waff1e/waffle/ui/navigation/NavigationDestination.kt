package com.waff1e.waffle.ui.navigation

import com.waff1e.waffle.R

sealed class NavigationDestination(val route: String, val titleRes: Int) {
    object Home : NavigationDestination("home", R.string.home)

    object Login : NavigationDestination("login", R.string.login)

    object Signup : NavigationDestination("signup", R.string.signup)

    object Waffles : NavigationDestination("waffles", R.string.waffles)

    object Waffle : NavigationDestination("waffle", R.string.waffle) {
        const val WAFFLE_ID = "waffleId"
    }

    object PostWaffle : NavigationDestination("postWaffle", R.string.post_waffle)

    object Profile : NavigationDestination("profile", R.string.profile) {
        const val MEMBER_ID = "memberId"
    }

    object EditProfile : NavigationDestination("edit_profile", R.string.edit_profile)

    object ProfileDetail : NavigationDestination("profile_detail", R.string.profile_detail)

    object ChangePassword : NavigationDestination("change_password", R.string.change_pwd_btn)

    object ChangeNickname : NavigationDestination("change_nickname", R.string.change_nickname_btn)

    object EditWaffle : NavigationDestination("edit_waffle", R.string.edit_waffle) {
        const val WAFFLE_ID = "waffleId"
    }

    object EditComment : NavigationDestination("edit_comment", R.string.edit_comment) {
        const val WAFFLE_ID = "waffleId"
        const val COMMENT_ID = "commentId"
    }
}
