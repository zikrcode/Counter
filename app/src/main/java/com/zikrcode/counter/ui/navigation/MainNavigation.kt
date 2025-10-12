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
import androidx.compose.ui.graphics.painter.Painter
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zikrcode.counter.ui.counter_editor.CounterEditorScreen
import com.zikrcode.counter.ui.counter_home.CounterHomeScreen
import com.zikrcode.counter.ui.counter_list.CounterListScreen
import com.zikrcode.counter.ui.counter_settings.CounterSettingsScreen
import kotlinx.serialization.Serializable

// Routes
@Serializable
sealed interface AppRoute {
    @Serializable
    data object CounterHome : AppRoute

    @Serializable
    data object CounterList : AppRoute

    @Serializable
    data object CounterSettings : AppRoute

    @Serializable
    data class CounterEditor(val counterId: Int?) : AppRoute
}

data class NavigationItem(
    val route: AppRoute,
    val label: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter = selectedIcon
)

@Composable
fun MainNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startDestination = AppRoute.CounterHome

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<AppRoute.CounterHome> {
            CounterHomeScreen(
                onSettingsClick = {
                    navController.navigateToAppRoute(AppRoute.CounterSettings)
                },
                onListClick = {
                    navController.navigateToAppRoute(AppRoute.CounterList)
                },
                onEditClick = { counterId ->
                    navController.navigateToAppRoute(AppRoute.CounterEditor(counterId))
                }
            )
        }
        composable<AppRoute.CounterList> {
            CounterListScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToCounterHome = {
                    navController.navigateToAppRoute(AppRoute.CounterHome)
                },
                onNavigateToEditCounter = { counterId ->
                    navController.navigateToAppRoute(AppRoute.CounterEditor(counterId))
                }
            )
        }
        composable<AppRoute.CounterSettings> {
            CounterSettingsScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToAbout = {
                    // TODO: Implement navigation to About screen
                }
            )
        }

        composable<AppRoute.CounterEditor> {
            CounterEditorScreen(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}