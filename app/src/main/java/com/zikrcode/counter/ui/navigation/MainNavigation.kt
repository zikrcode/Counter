/*
 * Copyright (C) 2023 Zokirjon Mamadjonov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zikrcode.counter.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zikrcode.counter.ui.screen.about.AboutScreen
import com.zikrcode.counter.ui.screen.counter.CounterScreen
import com.zikrcode.counter.ui.screen.settings.SettingsScreen
import com.zikrcode.counter.ui.screen.counter_editor.CounterEditorScreen
import com.zikrcode.counter.ui.screen.counter_list.CounterListScreen
import kotlinx.serialization.Serializable

// Routes
@Serializable
sealed interface AppRoute {
    @Serializable
    data object Counter : AppRoute

    @Serializable
    data object CounterList : AppRoute

    @Serializable
    data object Settings : AppRoute

    @Serializable
    data object About : AppRoute

    @Serializable
    data class CounterEditor(val counterId: Int?) : AppRoute
}

@Composable
fun MainNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startDestination = AppRoute.Counter

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<AppRoute.Counter> {
            CounterScreen(
                onNavigateToSettings = {
                    navController.navigateToAppRoute(AppRoute.Settings)
                },
                onNavigateToCounterList = {
                    navController.navigateToAppRoute(AppRoute.CounterList)
                },
                onNavigateToCounterEditor = { counterId ->
                    navController.navigateToAppRoute(AppRoute.CounterEditor(counterId))
                }
            )
        }
        composable<AppRoute.CounterList> {
            CounterListScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToCounter = {
                    navController.navigateToAppRoute(AppRoute.Counter)
                },
                onNavigateToCounterEditor = { counterId ->
                    navController.navigateToAppRoute(AppRoute.CounterEditor(counterId))
                }
            )
        }
        composable<AppRoute.Settings> {
            SettingsScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToAbout = {
                    navController.navigateToAppRoute(AppRoute.About)
                }
            )
        }
        composable<AppRoute.About> {
            AboutScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
        composable<AppRoute.CounterEditor> {
            CounterEditorScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}

private fun <T : AppRoute> NavHostController.navigateToAppRoute(route: T) {
    navigate(route) {
        popUpTo(route) {
            inclusive = true
        }
    }
}
