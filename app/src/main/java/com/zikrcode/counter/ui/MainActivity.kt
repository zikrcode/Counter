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

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zikrcode.counter.R
import com.zikrcode.counter.ui.common.theme.CounterTheme
import com.zikrcode.counter.ui.navigation.CounterHome
import com.zikrcode.counter.ui.navigation.CounterList
import com.zikrcode.counter.ui.navigation.CounterSettings
import com.zikrcode.counter.ui.navigation.MainNavigation
import com.zikrcode.counter.ui.utils.BottomNavigationItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            CounterTheme {
                ConfigureSystemUi()
                MainActivityContent()
            }
        }
    }
}

@Composable
private fun ComponentActivity.ConfigureSystemUi() {
    val darkTheme = isSystemInDarkTheme()
    val statusBarColor = MaterialTheme.colorScheme.background.toArgb()
    val navigationBarColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp).toArgb()

    DisposableEffect(darkTheme) {
        enableEdgeToEdge(
            statusBarStyle = when {
                Build.VERSION.SDK_INT >= 29 -> {
                    if (!darkTheme) {
                        SystemBarStyle.light(statusBarColor, statusBarColor)
                    } else {
                        SystemBarStyle.dark(statusBarColor)
                    }
                }
                else -> {
                    SystemBarStyle.auto(statusBarColor, statusBarColor) { darkTheme }
                }
            },
            navigationBarStyle = when {
                Build.VERSION.SDK_INT >= 29 -> {
                    if (!darkTheme) {
                        SystemBarStyle.light(navigationBarColor, navigationBarColor)
                    } else {
                        SystemBarStyle.dark(navigationBarColor)
                    }
                }
                else -> {
                    SystemBarStyle.auto(navigationBarColor, navigationBarColor) { darkTheme }
                }
            }
        )
        onDispose { }
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    device = Devices.PHONE
)
@Composable
fun MainActivityContentPreview() {
    CounterTheme {
        MainActivityContent()
    }
}

@Composable
fun MainActivityContent() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) {
        MainNavigation(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        )
    }
}

@Composable
private fun BottomNavigationBar(navController: NavController) {
    val bottomNavigationItems = listOf(
        BottomNavigationItem(
            route = CounterHome,
            title = stringResource(R.string.counter_home),
            selectedIcon = painterResource(R.drawable.ic_counter_filled),
            unselectedIcon = painterResource(R.drawable.ic_counter_outlined),
        ),
        BottomNavigationItem(
            route = CounterList,
            title = stringResource(R.string.counter_list),
            selectedIcon = painterResource(R.drawable.ic_featured_filled),
            unselectedIcon = painterResource(R.drawable.ic_featured_outlined),
        ),
        BottomNavigationItem(
            route = CounterSettings,
            title = stringResource(R.string.counter_settings),
            selectedIcon = painterResource(R.drawable.ic_settings_filled),
            unselectedIcon = painterResource(R.drawable.ic_settings_outlined),
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // TODO make sure it is not visible when CounterEditor screen is shown
    NavigationBar {
        bottomNavigationItems.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any { destination ->
                destination.hasRoute(item.route::class)
            } ?: false

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(text = item.title)
                }
            )
        }
    }
}