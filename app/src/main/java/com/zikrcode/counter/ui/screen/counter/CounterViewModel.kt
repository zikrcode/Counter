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

package com.zikrcode.counter.ui.screen.counter

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zikrcode.counter.domain.model.Counter
import com.zikrcode.counter.domain.use_case.CounterUseCases
import com.zikrcode.counter.ui.screen.settings.PreferencesKey
import com.zikrcode.counter.ui.utils.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
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

private data class CounterValueWrite(val id: Int, val value: Int)

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

    /**
     * Tap increments are written straight through rather than debounced on a timer: a timer window
     * is a window in which taps are lost outright if the process is killed, and `onCleared` does
     * not run in that case. The channel is conflated so that if writes ever fall behind a very fast
     * burst, intermediate values are dropped and the newest one still lands — coalescing happens
     * under real backpressure instead of on a fixed delay. A single consumer keeps writes ordered.
     */
    private val counterValueWrites = Channel<CounterValueWrite>(Channel.CONFLATED)

    init {
        collectCounter()
        collectPreferences()
        consumeCounterValueWrites()
    }

    private fun consumeCounterValueWrites() {
        viewModelScope.launch {
            for (write in counterValueWrites) {
                counterUseCases.updateCounterValueUseCase(write.id, write.value)
            }
        }
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
                if (counter.value + 1 in AppConstants.COUNTER_VALUE_RANGE) {
                    val newCounterValue = counter.value.plus(1)
                    _uiState.update { state ->
                        state.copy(
                            counter = state.counter?.copy(value = newCounterValue)
                        )
                    }
                    saveCounterValue()
                }
            }
        }

        CounterEvent.Reset -> {
            _uiState.update { state ->
                state.copy(
                    counter = state.counter?.copy(value = 0)
                )
            }
            saveCounterValue()
        }

        CounterEvent.Decrement -> {
            _uiState.value.counter?.let { counter ->
                if (counter.value - 1 in AppConstants.COUNTER_VALUE_RANGE) {
                    val newCounterValue = counter.value.minus(1)
                    _uiState.update { state ->
                        state.copy(
                            counter = state.counter?.copy(value = newCounterValue)
                        )
                    }
                    saveCounterValue()
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

    private fun saveCounterValue() {
        val counter = _uiState.value.counter ?: return
        val id = counter.id ?: return
        counterValueWrites.trySend(CounterValueWrite(id, counter.value))
    }
}