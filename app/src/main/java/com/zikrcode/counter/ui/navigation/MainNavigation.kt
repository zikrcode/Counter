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
import com.zikrcode.counter.ui.screen.counter.CounterScreen
import com.zikrcode.counter.ui.counter_list.CounterListScreen
import com.zikrcode.counter.ui.counter_settings.CounterSettingsScreen
import com.zikrcode.counter.ui.screen.counter_editor.CounterEditorScreen
import kotlinx.serialization.Serializable

// Routes
@Serializable
sealed interface AppRoute {
    @Serializable
    data object Counter : AppRoute

    @Serializable
    data object Counters : AppRoute

    @Serializable
    data object Settings : AppRoute

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
                onNavigateToCounters = {
                    navController.navigateToAppRoute(AppRoute.Counters)
                },
                onNavigateToCounterEditor = { counterId ->
                    navController.navigateToAppRoute(AppRoute.CounterEditor(counterId))
                }
            )
        }
        composable<AppRoute.Settings> {
            CounterSettingsScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToAbout = {
                    // TODO: Implement navigation to About screen
                }
            )
        }
        composable<AppRoute.Counters> {
            CounterListScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToCounterHome = {
                    navController.navigateToAppRoute(AppRoute.Counter)
                },
                onNavigateToEditCounter = { counterId ->
                    navController.navigateToAppRoute(AppRoute.CounterEditor(counterId))
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
        popUpTo(route)
    }
}
