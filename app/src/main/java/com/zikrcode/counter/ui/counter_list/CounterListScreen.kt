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

package com.zikrcode.counter.ui.counter_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.zikrcode.counter.R
import com.zikrcode.counter.domain.model.Counter
import com.zikrcode.counter.ui.counter_list.component.CounterGridItem
import com.zikrcode.counter.ui.utils.Dimens
import com.zikrcode.counter.ui.utils.navigation.Screen
import com.zikrcode.counter.ui.utils.navigation.MainNavigationArgs
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CounterListScreen(
    navController: NavController,
    viewModel: CounterListViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is CounterListViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message.asString(context)
                    )
                }
                is CounterListViewModel.UiEvent.CounterSelected -> {
                    navController.navigate(Screen.CounterHomeScreen.route) {
                        popUpTo(Screen.CounterHomeScreen.route) {
                            saveState = true
                        }
                        restoreState = true
                    }
                }
                is CounterListViewModel.UiEvent.EditCounter -> {
                    navController.navigate(
                        Screen.AddEditCounterScreen.route +
                                "?${MainNavigationArgs.COUNTER_ID_ARG}=${event.counter.id}"
                    )
                }
                CounterListViewModel.UiEvent.CreateNewCounter -> {
                    navController.navigate(Screen.AddEditCounterScreen.route)
                }
            }
        }
    }

    val state = viewModel.state.value

    CounterListContent(
        snackbarHostState = snackbarHostState,
        state = state,
        onEventClick = viewModel::onEvent
    )
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    device = Devices.PHONE
)
@Composable
fun CounterListContentPreview() {
    CounterListContent(
        snackbarHostState = SnackbarHostState(),
        state = CounterListState(),
        onEventClick = { }
    )
}

@Composable
private fun CounterListContent(
    snackbarHostState: SnackbarHostState,
    state: CounterListState,
    onEventClick: (CounterListEvent) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            CounterListScreenFAB {
                onEventClick(CounterListEvent.NewCounter)
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(Dimens.SpacingSingle)
        ) {
            CounterListStaggeredGrid(
                counters = state.allCounters,
                onEventClick = onEventClick
            )
        }
    }
}

@Composable
private fun CounterListScreenFAB(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_add),
            contentDescription = stringResource(R.string.add)
        )
    }
}

@Composable
private fun CounterListStaggeredGrid(
    counters: List<Counter>,
    onEventClick: (CounterListEvent) -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2)
    ) {
        items(counters) { counter ->
            CounterGridItem(
                counter = counter,
                onClick = {
                    onEventClick(CounterListEvent.SelectCounter(counter))
                },
                onDeleteClick = {
                    onEventClick(CounterListEvent.Delete(counter))
                },
                onEditClick = {
                    onEventClick(CounterListEvent.Edit(counter))
                }
            )
        }
    }
}