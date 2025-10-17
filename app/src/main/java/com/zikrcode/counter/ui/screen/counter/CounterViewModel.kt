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

package com.zikrcode.counter.ui.screen.counter

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zikrcode.counter.domain.model.Counter
import com.zikrcode.counter.domain.use_case.CounterUseCases
import com.zikrcode.counter.ui.counter_settings.PreferencesKey
import com.zikrcode.counter.ui.utils.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CounterUiState(
    val isLoading: Boolean = false,
    val counter: Counter? = null,
    val keepScreenOn: Boolean = false,
    val vibrateOnTap: Boolean = false,
    val navTarget: CounterNavTarget = CounterNavTarget.Idle
)

sealed interface CounterNavTarget {
    data object Settings : CounterNavTarget
    data object CounterList : CounterNavTarget
    data object CounterEditor : CounterNavTarget
    data object Idle : CounterNavTarget
}

@HiltViewModel
class CounterViewModel @Inject constructor(
    private val counterUseCases: CounterUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(CounterUiState())
    val uiState = _uiState.asStateFlow()

    private var saveCounterJob: Job? = null

    init {
        collectCounter()
        collectPreferences()
    }

    private fun collectCounter() {
        _uiState.update { state ->
            state.copy(isLoading = true)
        }

        var counterJob: Job? = null

        fun changeCounter(id: Int) {
            counterJob?.cancel()
            counterJob = viewModelScope.launch {
                counterUseCases.counterByIdUseCase(id).collectLatest { counter ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            counter = counter
                        )
                    }
                }
            }
        }

        viewModelScope.launch {
            counterUseCases.readUserPreferenceUseCase(
                intPreferencesKey(AppConstants.LAST_USED_COUNTER_ID_KEY)
            ).collectLatest { counterId ->
                counterId?.let {
                    changeCounter(it)
                } ?: run {
                    _uiState.update { state ->
                        state.copy(isLoading = false)
                    }
                }
            }
        }
    }

    private fun collectPreferences() {
        viewModelScope.apply {
            launch {
                counterUseCases.readUserPreferenceUseCase(
                    booleanPreferencesKey(PreferencesKey.VIBRATE_PREF_KEY)
                ).collectLatest { vibrate ->
                    _uiState.update { state ->
                        state.copy(
                            vibrateOnTap = vibrate ?: false
                        )
                    }
                }
            }
            launch {
                val keepScreenOn = counterUseCases.readUserPreferenceUseCase(
                    booleanPreferencesKey(PreferencesKey.KEEP_SCREEN_ON_PREF_KEY)
                ).first() ?: false
                _uiState.update { state ->
                    state.copy(keepScreenOn = keepScreenOn)
                }
            }
        }
    }

    fun onEvent(event: CounterEvent) = when (event) {
        CounterEvent.Settings -> {
            _uiState.update { state ->
                state.copy(navTarget = CounterNavTarget.Settings)
            }
        }

        CounterEvent.Counters -> {
            _uiState.update { state ->
                state.copy(navTarget = CounterNavTarget.CounterList)
            }
        }

        CounterEvent.Increment -> {
            _uiState.value.counter?.let { counter ->
                if (counter.counterSavedValue + 1 in AppConstants.COUNTER_VALUE_RANGE) {
                    val newCounterValue = counter.counterSavedValue.plus(1)
                    _uiState.update { state ->
                        state.copy(
                            counter = state.counter?.copy(counterSavedValue = newCounterValue)
                        )
                    }
                    saveCounter()
                }
            }
        }

        CounterEvent.Reset -> {
            _uiState.update { state ->
                state.copy(
                    counter = state.counter?.copy(counterSavedValue = 0)
                )
            }
            saveCounter()
        }

        CounterEvent.Decrement -> {
            _uiState.value.counter?.let { counter ->
                if (counter.counterSavedValue - 1 in AppConstants.COUNTER_VALUE_RANGE) {
                    val newCounterValue = counter.counterSavedValue.minus(1)
                    _uiState.update { state ->
                        state.copy(
                            counter = state.counter?.copy(counterSavedValue = newCounterValue)
                        )
                    }
                    saveCounter()
                }
            }
        }

        CounterEvent.Edit -> {
            _uiState.update { state ->
                state.copy(navTarget = CounterNavTarget.CounterEditor)
            }
        }

        CounterEvent.NavigationHandled -> {
            _uiState.update { state ->
                state.copy(navTarget = CounterNavTarget.Idle)
            }
        }
    }

    private fun saveCounter() {
        saveCounterJob?.cancel()
        saveCounterJob = viewModelScope.launch {
            _uiState.value.counter?.let { counter ->
                counterUseCases.insertCounterUseCase(counter)
            }
        }
    }
}