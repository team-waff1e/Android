package com.waff1e.waffle.ui.navigation

import com.waff1e.waffle.R

sealed class NavigationDestination(val route: String, val titleRes: Int) {
    object Home : NavigationDestination("home", R.string.home)

    object Login : NavigationDestination("login", R.string.login)

    object Signup : NavigationDestination("signup", R.string.signup)

    object Waffles : NavigationDestination("waffles", R.string.waffles)

    object Waffle : NavigationDestination("waffle", R.string.waffle) {
        const val waffleArg = "waffle"
    }

    object PostWaffle : NavigationDestination("postWaffle", R.string.post_waffle)

    object Profile : NavigationDestination("profile", R.string.profile)
}
