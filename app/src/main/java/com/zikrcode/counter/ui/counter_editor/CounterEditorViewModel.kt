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

package com.zikrcode.counter.ui.counter_editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.zikrcode.counter.domain.model.Counter
import com.zikrcode.counter.domain.use_case.CounterUseCases
import com.zikrcode.counter.ui.navigation.AppRoute
import com.zikrcode.counter.ui.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CounterEditorUiState(
    val isLoading: Boolean = false,
    val counterId: Int?,
    val counterName: String = "",
    val counterDescription: String = "",
    val counterValue: Int = 0,
    val message: UiText? = null,
    val navTarget: CounterEditorNavTarget = CounterEditorNavTarget.Idle
)

sealed interface CounterEditorNavTarget {
    data object NavigateBack : CounterEditorNavTarget
    data object Idle : CounterEditorNavTarget
}

@HiltViewModel
class CounterEditorViewModel @Inject constructor(
    private val counterUseCases: CounterUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args: AppRoute.CounterEditor = savedStateHandle.toRoute()
    private var counterId: Int? = args.counterId

    private val _uiState = MutableStateFlow(
        CounterEditorUiState(counterId = counterId)
    )
    val uiState = _uiState.asStateFlow()

    init {
        loadCounter()
    }

    private fun loadCounter() {
        counterId?.let { id ->
            _uiState.update { state ->
                state.copy(isLoading = true)
            }

            viewModelScope.launch {
                val counter = counterUseCases.counterByIdUseCase(id).first()
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        counterName = counter.counterName,
                        counterDescription = counter.counterDescription,
                        counterValue = counter.counterSavedValue
                    )
                }
            }
        }
    }

    fun onEvent(event: CounterEditorEvent) = when (event) {
        CounterEditorEvent.RestoreCounter -> {
            loadCounter()
        }
        is CounterEditorEvent.NameChanged -> {
            _uiState.update { state ->
                state.copy(counterName = event.name)
            }
        }
        is CounterEditorEvent.DescriptionChanged -> {
            _uiState.update { state ->
                state.copy(counterDescription = event.description)
            }
        }
        is CounterEditorEvent.ValueChanged -> {
            _uiState.update { state ->
                state.copy(counterValue = event.value)
            }
        }
        CounterEditorEvent.MessageShown -> {
            _uiState.update { state ->
                state.copy(message = null)
            }
        }
        CounterEditorEvent.GoBack, CounterEditorEvent.Cancel -> {
            _uiState.update { state ->
                state.copy(navTarget = CounterEditorNavTarget.NavigateBack)
            }
        }
        CounterEditorEvent.Save -> {
            viewModelScope.launch {
                val counter = Counter(
                    id = counterId,
                    counterName = _uiState.value.counterName,
                    counterDescription = _uiState.value.counterDescription,
                    counterDate = System.currentTimeMillis(),
                    counterSavedValue = _uiState.value.counterValue
                )
                val counterValidationResult = counterUseCases.insertCounterUseCase(counter)
                _uiState.update { state ->
                    counterValidationResult.errorMessage?.let { message ->
                        state.copy(message = message)
                    } ?: run {
                        state.copy(navTarget = CounterEditorNavTarget.NavigateBack)
                    }
                }
            }
        }
    }
}
