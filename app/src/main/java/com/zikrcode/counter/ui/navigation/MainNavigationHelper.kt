package com.zikrcode.counter.ui.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

// TODO double check the logic and backstack
fun <T : AppRoute> NavHostController.navigateToAppRoute(route: T) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
