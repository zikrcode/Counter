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

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterListViewModel @Inject constructor(
    private val counterUseCases: CounterUseCases
) : ViewModel() {

    private var loadAllCountersJob: Job? = null

    private val _state = mutableStateOf(CounterListState())
    val state: State<CounterListState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        loadAllCounters(CounterOrder.Date(OrderType.DESCENDING))
    }

    private fun loadAllCounters(counterOrder: CounterOrder) {
        loadAllCountersJob?.cancel()
        loadAllCountersJob = counterUseCases.allCountersUseCase(counterOrder)
            .onEach { counters ->
                _state.value = state.value.copy(
                    allCounters = counters,
                    counterOrder = counterOrder
                )
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(counterListEvent: CounterListEvent) {
        when (counterListEvent) {
            CounterListEvent.ToggleOrderSection -> {

            }
            is CounterListEvent.Order -> {

            }
            is CounterListEvent.SelectCounter -> {
                viewModelScope.launch {
                    counterListEvent.counter.id?.let {
                        counterUseCases.writeUserPreferenceUseCase(
                            key = intPreferencesKey(AppConstants.LAST_USED_COUNTER_ID_KEY),
                            value = it
                        )
                        _eventFlow.emit(UiEvent.CounterSelected(counterListEvent.counter))
                    }
                }
            }
            is CounterListEvent.Delete -> {
                viewModelScope.launch {
                    val currentCounterId = counterUseCases.readUserPreferenceUseCase(
                        intPreferencesKey(AppConstants.LAST_USED_COUNTER_ID_KEY)
                    ).first()
                    if (counterListEvent.counter.id == currentCounterId) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                UiText.StringResource(R.string.current_counter_in_use)
                            )
                        )
                    } else {
                        counterUseCases.deleteCounterUseCase(counterListEvent.counter)
                    }
                }
            }
            is CounterListEvent.Edit -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.EditCounter(counterListEvent.counter))
                }
            }
            CounterListEvent.NewCounter -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.CreateNewCounter)
                }
            }
            CounterListEvent.RestoreCounter -> {

            }
        }
    }

    sealed class UiEvent {

        data class ShowSnackbar(val message: UiText) : UiEvent()

        data class CounterSelected(val counter: Counter) : UiEvent()

        data class EditCounter(val counter: Counter) : UiEvent()

        object CreateNewCounter : UiEvent()
    }
}