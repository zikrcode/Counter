package com.zikrcode.counter.ui.navigation

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

fun <T : AppRoute> NavHostController.navigateToAppRoute(route: T) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

fun NavDestination.toAppRoute(): AppRoute? = when {
    hasRoute(AppRoute.CounterHome::class) -> AppRoute.CounterHome
    hasRoute(AppRoute.CounterList::class) -> AppRoute.CounterList
    hasRoute(AppRoute.CounterSettings::class) -> AppRoute.CounterSettings
    else -> null
}
