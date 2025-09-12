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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zikrcode.counter.R
import com.zikrcode.counter.domain.model.Counter
import com.zikrcode.counter.domain.utils.CounterOrder
import com.zikrcode.counter.domain.utils.OrderType
import com.zikrcode.counter.ui.counter_list.component.CounterGridItem
import com.zikrcode.counter.ui.counter_list.component.CounterListHeader
import com.zikrcode.counter.ui.counter_list.component.OrderSection
import com.zikrcode.counter.ui.utils.Dimens

@Composable
fun CounterListScreen(
    onCounterSelect: (Int) -> Unit,
    onCounterEdit: (Int) -> Unit,
    onAddCounter: () -> Unit,
    viewModel: CounterListViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CounterListContent(
        snackbarHostState = snackbarHostState,
        counterOrder = uiState.counterOrder,
        orderSectionVisible = uiState.isOrderSectionVisible,
        allCounters = uiState.allCounters,
        onCounterSelect = onCounterSelect,
        onCounterEdit = onCounterEdit,
        onAddCounter = onAddCounter,
        onEvent = viewModel::onEvent
    )

    val snackbarMessage = stringResource(R.string.counter_deleted)
    val snackbarActionLabel = stringResource(R.string.undo)
    val context = LocalContext.current

    uiState.apply {
        recentlyDeletedCounter?.let { counter ->
            LaunchedEffect(counter) {
                val result = snackbarHostState.showSnackbar(
                    message = snackbarMessage,
                    actionLabel = snackbarActionLabel,
                    duration = SnackbarDuration.Long
                )
                when (result) {
                    SnackbarResult.Dismissed -> {
                        viewModel.onEvent(CounterListEvent.FinishDeleteCounter)
                    }
                    SnackbarResult.ActionPerformed -> {
                        viewModel.onEvent(CounterListEvent.RestoreCounter)
                    }
                }
            }
        }
        userMessage?.let { uiText ->
            LaunchedEffect(uiText) {
                snackbarHostState.showSnackbar(message = uiText.asString(context))
                viewModel.onEvent(CounterListEvent.UserMessageShown)
            }
        }
    }
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
        counterOrder = CounterOrder.Date(OrderType.DESCENDING),
        orderSectionVisible = false,
        allCounters = listOf(),
        onCounterSelect = { },
        onCounterEdit = { },
        onAddCounter = { },
        onEvent = { }
    )
}

@Composable
private fun CounterListContent(
    snackbarHostState: SnackbarHostState,
    counterOrder: CounterOrder,
    orderSectionVisible: Boolean,
    allCounters: List<Counter>,
    onCounterSelect: (Int) -> Unit,
    onCounterEdit: (Int) -> Unit,
    onAddCounter: () -> Unit,
    onEvent: (CounterListEvent) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            CounterListScreenFAB { onAddCounter() }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = WindowInsets(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(Dimens.SpacingSingle)
        ) {
            CounterListHeader(
                orderSectionVisible = orderSectionVisible,
                onToggleOrderSection = {
                    onEvent(CounterListEvent.ToggleOrderSection)
                }
            )
            AnimatedVisibility(
                visible = orderSectionVisible,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                OrderSection(
                    counterOrder = counterOrder,
                    onOrderChange = { newCounterOrder ->
                        onEvent(CounterListEvent.Order(newCounterOrder))
                    }
                )
            }
            CounterListStaggeredGrid(
                allCounters = allCounters,
                onCounterSelect = onCounterSelect,
                onCounterEdit = onCounterEdit,
                onCounterDelete = { counter ->
                    onEvent(CounterListEvent.DeleteCounter(counter))
                }
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
    allCounters: List<Counter>,
    onCounterSelect: (Int) -> Unit,
    onCounterEdit: (Int) -> Unit,
    onCounterDelete: (Counter) -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2)
    ) {
        items(allCounters) { counter ->
            CounterGridItem(
                counter = counter,
                onClick = { onCounterSelect(counter.id!!) },
                onDeleteClick = { onCounterDelete(counter) },
                onEditClick = { onCounterEdit(counter.id!!) }
            )
        }
    }
}