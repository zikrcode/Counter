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

package com.zikrcode.counter.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.FloatingToolbarDefaults.ScreenOffset
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zikrcode.counter.R
import com.zikrcode.counter.ui.common.theme.CounterTheme
import com.zikrcode.counter.ui.navigation.AppRoute
import com.zikrcode.counter.ui.navigation.MainNavigation
import com.zikrcode.counter.ui.navigation.NavigationItem
import com.zikrcode.counter.ui.navigation.navigateToAppRoute
import com.zikrcode.counter.ui.navigation.toAppRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            CounterTheme {
                MainActivityContent()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MainActivityContent() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.toAppRoute()

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MainNavigation(navController)
            currentDestination?.let { destination ->
                MainToolbar(
                    currentRoute = destination,
                    onToolbarItemClick = { route ->
                        navController.navigateToAppRoute(route)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = -ScreenOffset)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun MainToolbar(
    currentRoute: AppRoute,
    onToolbarItemClick: (AppRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    val navigationItems = mainToolbarNavigationItems()
    HorizontalFloatingToolbar(
        expanded = true,
        floatingActionButton = {
            FloatingToolbarDefaults.VibrantFloatingActionButton(
                onClick = {
                    onToolbarItemClick.invoke(
                        AppRoute.CounterEditor(null)
                    )
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = null
                )
            }
        },
        modifier = modifier,
        content = {
            navigationItems.forEach { item ->
                val isSelected = currentRoute == item.route
                IconButton(
                    onClick = {
                        onToolbarItemClick.invoke(item.route)
                    },
                    colors = IconButtonDefaults.iconButtonColors().copy(
                        containerColor = if (isSelected) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            Color.Unspecified
                        }
                    )
                ) {
                    Icon(
                        painter = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label
                    )
                }
            }
        }
    )
}

@Composable
private fun mainToolbarNavigationItems(): List<NavigationItem> = listOf(
    NavigationItem(
        route = AppRoute.CounterHome,
        label = stringResource(R.string.counter_home),
        selectedIcon = painterResource(R.drawable.ic_counter_filled),
        unselectedIcon = painterResource(R.drawable.ic_counter_outlined),
    ),
    NavigationItem(
        route = AppRoute.CounterList,
        label = stringResource(R.string.counter_list),
        selectedIcon = painterResource(R.drawable.ic_featured_filled),
        unselectedIcon = painterResource(R.drawable.ic_featured_outlined),
    ),
    NavigationItem(
        route = AppRoute.CounterSettings,
        label = stringResource(R.string.counter_settings),
        selectedIcon = painterResource(R.drawable.ic_settings_filled),
        unselectedIcon = painterResource(R.drawable.ic_settings_outlined),
    )
)
