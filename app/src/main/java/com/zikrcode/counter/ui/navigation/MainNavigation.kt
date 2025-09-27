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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.zikrcode.counter.ui.counter_editor.CounterEditorScreen
import com.zikrcode.counter.ui.counter_home.CounterHomeScreen
import com.zikrcode.counter.ui.counter_list.CounterListScreen
import com.zikrcode.counter.ui.counter_settings.CounterSettingsScreen

@Composable
fun MainNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navActions = remember(navController) {
        MainNavigationActions(navController)
    }
    val startDestination = CounterHome

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<CounterHome> {
            CounterHomeScreen(onEditCounterClick = navActions::navigateToCounterEditor)
        }
        composable<CounterList> {
            CounterListScreen(
                onCounterClick = navActions::navigateToCounterHome,
                onEditCounterClick = navActions::navigateToCounterEditor
            )
        }
        composable<CounterSettings> {
            CounterSettingsScreen()
        }

        composable<CounterEditor> { backStackEntry ->
            val args = backStackEntry.toRoute<CounterEditor>()
            CounterEditorScreen(onBackClick = navActions::navigateBack)
        }
    }
}