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

package com.zikrcode.counter.ui.screen.counter_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MediumFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zikrcode.counter.R
import com.zikrcode.counter.domain.model.Counter
import com.zikrcode.counter.ui.common.composables.AppIconButton
import com.zikrcode.counter.ui.common.composables.AppScreenContent
import com.zikrcode.counter.ui.common.theme.CounterTheme
import com.zikrcode.counter.ui.screen.counter_list.component.CounterListItem
import com.zikrcode.counter.ui.screen.counter_list.component.CounterListItemHeight
import com.zikrcode.counter.ui.utils.Dimens
import com.zikrcode.counter.ui.utils.UiText

@Composable
fun CounterListScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCounter: () -> Unit,
    onNavigateToCounterEditor: (Int?) -> Unit,
    viewModel: CounterListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.navTarget) {
        when (val navTarget = uiState.navTarget) {
            CounterListNavTarget.NavigateBack -> {
                onNavigateBack.invoke()
            }
            CounterListNavTarget.Counter -> {
                onNavigateToCounter.invoke()
            }
            is CounterListNavTarget.CounterEditor -> {
                onNavigateToCounterEditor.invoke(navTarget.id)
            }
            CounterListNavTarget.NewCounter -> {
                onNavigateToCounterEditor.invoke(null)
            }
            CounterListNavTarget.Idle -> {
                // no-op
            }
        }
        viewModel.onEvent(CounterListEvent.NavigationHandled)
    }

    CounterListContent(
        isLoading = uiState.isLoading,
        message = uiState.message,
        counters = uiState.counters,
        onEvent = viewModel::onEvent
    )
}

@PreviewLightDark
@Composable
private fun CounterListContentPreview() {
    CounterTheme {
        CounterListContent(
            isLoading = false,
            message = null,
            counters = listOf(
                Counter.instance().copy(id = 0),
                Counter.instance().copy(id = 1),
                Counter.instance().copy(
                    id = 2,
                    counterDescription = "",
                    counterSavedValue = 9999999
                )
            ),
            onEvent = { }
        )
    }
}

@Composable
private fun CounterListContent(
    isLoading: Boolean,
    message: UiText?,
    counters: List<Counter>,
    onEvent: (CounterListEvent) -> Unit
) {
    AppScreenContent(
        loading = isLoading,
        title = stringResource(R.string.counter_list),
        topBarStartIcon = {
            AppIconButton(
                onClick = {
                    onEvent.invoke(CounterListEvent.GoBack)
                },
                icon = Icons.AutoMirrored.Outlined.ArrowBack,
                iconDescription = stringResource(R.string.go_back)
            )
        },
        topBarEndIcon = {
            AppIconButton(
                onClick = {
                    onEvent.invoke(CounterListEvent.ToggleOrderSection)
                },
                icon = Icons.Outlined.FilterList,
                iconDescription = stringResource(R.string.counter_order)
            )
        },
        floatingActionButton = {
            NewCounterFloatingActionButton(
                onClick = {
                    onEvent.invoke(CounterListEvent.NewCounter)
                }
            )
        },
        snackbarMessage = message?.asString(),
        onSnackbarShown = {
            onEvent.invoke(CounterListEvent.SnackbarShown)
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = CounterListItemHeight),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingDouble)
        ) {
            items(counters) { counter ->
                val counterId = counter.id ?: return@items
                CounterListItem(
                    counter = counter,
                    onClick = {
                        onEvent.invoke(CounterListEvent.SelectCounter(counterId))
                    },
                    onDeleteClick = {
                        onEvent.invoke(CounterListEvent.Delete(counter))
                    },
                    onEditClick = {
                        onEvent.invoke(CounterListEvent.Edit(counterId))
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun NewCounterFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    MediumFloatingActionButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.new_counter),
            modifier = Modifier.size(FloatingActionButtonDefaults.MediumIconSize),
        )
    }
}
