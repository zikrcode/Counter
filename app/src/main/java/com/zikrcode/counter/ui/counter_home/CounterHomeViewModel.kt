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

package com.zikrcode.counter.ui.counter_home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterHomeViewModel @Inject constructor(
    private val counterUseCases: CounterUseCases,
) : ViewModel() {

    private val _keepScreenOn = mutableStateOf(false)
    val keepScreenOn: State<Boolean> = _keepScreenOn

    private val _counter = mutableStateOf<Counter?>(null)
    val counter: State<Counter?> = _counter

    private val _vibrateOnTap = mutableStateOf(false)
    val vibrateOnTap: State<Boolean> = _vibrateOnTap

    private var saveCounterJob: Job? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        loadCounter()
        loadPreferences()

        viewModelScope.launch {
            _keepScreenOn.value = counterUseCases.readUserPreferenceUseCase(
                booleanPreferencesKey(PreferencesKey.KEEP_SCREEN_ON_PREF_KEY)
            ).first() ?: false
        }
    }

    private fun loadCounter() {
        var counterJob: Job? = null

        fun changeCounter(id: Int) {
            counterJob?.cancel()
            counterJob = viewModelScope.launch {
                counterUseCases.counterByIdUseCase(id).collectLatest {
                    _counter.value = it
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
                    _eventFlow.emit(UiEvent.NoCounter)
                }
            }
        }
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            counterUseCases.readUserPreferenceUseCase(
                booleanPreferencesKey(PreferencesKey.VIBRATE_PREF_KEY)
            ).collectLatest {
                _vibrateOnTap.value = it ?: false
            }
        }
    }

    fun onEvent(counterHomeEvent: CounterHomeEvent) {
        when(counterHomeEvent) {
            is CounterHomeEvent.Edit -> {
                _counter.value?.let {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.EditCounter(it))
                    }
                }
            }
            CounterHomeEvent.Reset -> {
                _counter.value = counter.value?.copy(
                    counterSavedValue = 0
                )
                saveCounter()
            }
            CounterHomeEvent.Increment -> {
                counter.value?.let { counter ->
                    if (counter.counterSavedValue + 1 in AppConstants.COUNTER_VALUE_RANGE) {
                        _counter.value = counter.copy(
                            counterSavedValue = counter.counterSavedValue.plus(1)
                        )
                        saveCounter()
                    }
                }
            }
            CounterHomeEvent.Decrement -> {
                counter.value?.let { counter ->
                    if (counter.counterSavedValue - 1 in AppConstants.COUNTER_VALUE_RANGE) {
                        _counter.value = counter.copy(
                            counterSavedValue = counter.counterSavedValue.minus(1)
                        )
                        saveCounter()
                    }
                }
            }
        }
    }

    private fun saveCounter() {
        saveCounterJob?.cancel()
        saveCounterJob = viewModelScope.launch {
            _counter.value?.let {
                counterUseCases.insertCounterUseCase(it)
            }
        }
    }

    sealed class UiEvent {

        object NoCounter : UiEvent()

        data class EditCounter(val counter: Counter) : UiEvent()
    }
}