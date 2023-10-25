package com.waff1e.waffle.ui.navigation

import com.waff1e.waffle.R

sealed class NavigationDestination(val route: String, val titleRes: Int) {
    object Home : NavigationDestination("home", R.string.home)

    object Login : NavigationDestination("login", R.string.login)

    object Signup : NavigationDestination("signup", R.string.signup)

    object Waffles : NavigationDestination("waffles", R.string.app_name)
}
