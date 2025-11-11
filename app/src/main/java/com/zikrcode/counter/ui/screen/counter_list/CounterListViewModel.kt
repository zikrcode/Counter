/*
 * Copyright (C) 2023–2025 Zokirjon Mamadjonov
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

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zikrcode.counter.R
import com.zikrcode.counter.domain.model.Counter
import com.zikrcode.counter.domain.use_case.CounterUseCases
import com.zikrcode.counter.domain.utils.CounterOrder
import com.zikrcode.counter.domain.utils.OrderType
import com.zikrcode.counter.ui.utils.AppConstants
import com.zikrcode.counter.ui.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CounterListUiState(
    val isLoading: Boolean = false,
    val counters: List<Counter> = emptyList(),
    val counterOrder: CounterOrder = CounterOrder.Date(OrderType.DESCENDING),
    val isOrderSectionVisible: Boolean = false,
    val message: UiText? = null,
    val navTarget: CounterListNavTarget = CounterListNavTarget.Idle
)

sealed interface CounterListNavTarget {
    data object NavigateBack : CounterListNavTarget
    data object Counter : CounterListNavTarget
    data class CounterEditor(val id: Int) : CounterListNavTarget
    data object NewCounter : CounterListNavTarget
    data object Idle : CounterListNavTarget
}

@HiltViewModel
class CounterListViewModel @Inject constructor(
    private val counterUseCases: CounterUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(CounterListUiState())
    val uiState = _uiState.asStateFlow()

    private var loadAllCountersJob: Job? = null

    init {
        loadAllCounters(_uiState.value.counterOrder)
    }

    private fun loadAllCounters(counterOrder: CounterOrder) {
        _uiState.update { state ->
            state.copy(isLoading = true)
        }

        loadAllCountersJob?.cancel()
        loadAllCountersJob = counterUseCases
            .allCountersUseCase(counterOrder)
            .onEach { counters ->
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        counters = counters
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: CounterListEvent) = when (event) {
        CounterListEvent.GoBack -> {
            _uiState.update { state ->
                state.copy(navTarget = CounterListNavTarget.NavigateBack)
            }
        }

        CounterListEvent.ToggleOrderSection -> {
            // TODO
        }

        is CounterListEvent.Order -> {
            // TODO
        }

        is CounterListEvent.SelectCounter -> {
            viewModelScope.launch {
                counterUseCases.writeUserPreferenceUseCase(
                    key = intPreferencesKey(AppConstants.LAST_USED_COUNTER_ID_KEY),
                    value = event.id
                )
                _uiState.update { state ->
                    state.copy(navTarget = CounterListNavTarget.Counter)
                }
            }
        }

        is CounterListEvent.Edit -> {
            _uiState.update { state ->
                state.copy(
                    navTarget = CounterListNavTarget.CounterEditor(event.id)
                )
            }
        }

        is CounterListEvent.Delete -> {
            viewModelScope.launch {
                val currentCounterId = counterUseCases.readUserPreferenceUseCase(
                    intPreferencesKey(AppConstants.LAST_USED_COUNTER_ID_KEY)
                ).first()
                if (event.counter.id == currentCounterId) {
                    _uiState.update { state ->
                        state.copy(
                            message = UiText.StringResource(R.string.current_counter_in_use)
                        )
                    }
                } else {
                    counterUseCases.deleteCounterUseCase(event.counter)
                }
            }
        }

        CounterListEvent.NewCounter -> {
            _uiState.update { state ->
                state.copy(navTarget = CounterListNavTarget.NewCounter)
            }
        }

        CounterListEvent.RestoreCounter -> {
            // TODO RestoreCounter
        }

        is CounterListEvent.SnackbarShown -> {
            _uiState.update { state ->
                state.copy(message = null)
            }
        }

        CounterListEvent.NavigationHandled -> {
            _uiState.update { state ->
                state.copy(navTarget = CounterListNavTarget.Idle)
            }
        }
    }
}
