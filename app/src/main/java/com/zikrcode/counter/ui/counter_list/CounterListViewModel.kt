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
    val allCounters: List<Counter> = emptyList(),
    val counterOrder: CounterOrder = CounterOrder.Date(OrderType.DESCENDING),
    val isOrderSectionVisible: Boolean = false,
    val recentlyDeletedCounter: Counter? = null,
    val userMessage: UiText? = null
)

@HiltViewModel
class CounterListViewModel @Inject constructor(
    private val counterUseCases: CounterUseCases
) : ViewModel() {

    private var loadAllCountersJob: Job? = null

    private val _uiState = MutableStateFlow(CounterListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadAllCounters(CounterOrder.Date(OrderType.DESCENDING))
    }

    private fun loadAllCounters(counterOrder: CounterOrder) {
        loadAllCountersJob?.cancel()
        loadAllCountersJob = counterUseCases.allCountersUseCase(counterOrder)
            .onEach { counters ->
                _uiState.update {
                    it.copy(
                        allCounters = counters,
                        counterOrder = counterOrder
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(counterListEvent: CounterListEvent) {
        when (counterListEvent) {
            CounterListEvent.ToggleOrderSection -> {
                _uiState.update {
                    it.copy(isOrderSectionVisible = !it.isOrderSectionVisible)
                }
            }
            is CounterListEvent.Order -> {
                loadAllCounters(counterListEvent.counterOrder)
            }
            is CounterListEvent.DeleteCounter -> {
                viewModelScope.launch {
                    val currentCounterId = counterUseCases.readUserPreferenceUseCase(
                        intPreferencesKey(AppConstants.LAST_USED_COUNTER_ID_KEY)
                    ).first()
                    if (counterListEvent.counter.id == currentCounterId) {
                        _uiState.update {
                            it.copy(
                                userMessage = UiText.StringResource(R.string.current_counter_in_use)
                            )
                        }
                    } else {
                        counterUseCases.deleteCounterUseCase(counterListEvent.counter)
                        _uiState.update {
                            it.copy(recentlyDeletedCounter = counterListEvent.counter)
                        }
                    }
                }
            }
            CounterListEvent.RestoreCounter -> {
                viewModelScope.launch {
                    _uiState.value.recentlyDeletedCounter?.let {
                        counterUseCases.insertCounterUseCase(it)
                    }
                    _uiState.update {
                        it.copy(recentlyDeletedCounter = null)
                    }
                }
            }
            CounterListEvent.FinishDeleteCounter -> {
                _uiState.update {
                    it.copy(recentlyDeletedCounter = null)
                }
            }
            CounterListEvent.UserMessageShown -> {
                _uiState.update {
                    it.copy(userMessage = null)
                }
            }
        }
    }
}