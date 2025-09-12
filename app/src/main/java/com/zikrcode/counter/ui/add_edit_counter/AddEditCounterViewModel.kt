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

package com.zikrcode.counter.ui.add_edit_counter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zikrcode.counter.domain.model.Counter
import com.zikrcode.counter.domain.use_case.CounterUseCases
import com.zikrcode.counter.ui.navigation.MainNavigationArgs
import com.zikrcode.counter.ui.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddEditCounterUiState(
    val counterName: String = "",
    val counterDescription: String = "",
    val counterValue: Int? = null,
    val isLoading: Boolean = false,
    val userMessage: UiText? = null,
    val isCounterCanceled: Boolean = false,
    val isCounterSaved: Boolean = false
)

@HiltViewModel
class AddEditCounterViewModel @Inject constructor(
    private val counterUseCases: CounterUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val counterId: Int? = savedStateHandle[MainNavigationArgs.COUNTER_ID_ARG]
        get() = field.takeIf { it != -1 }

    private val _uiState = MutableStateFlow(AddEditCounterUiState())
    val uiState = _uiState.asStateFlow()

    init {
        counterId?.let { loadCounter(it) }
    }
    
    private fun loadCounter(counterId: Int) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            counterUseCases.counterByIdUseCase(counterId).first().let { counter ->
                _uiState.update {
                    it.copy(
                        counterName = counter.counterName,
                        counterDescription = counter.counterDescription,
                        counterValue = counter.counterSavedValue,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onEvent(addEditCounterEvent: AddEditCounterEvent) {
        when (addEditCounterEvent) {
            is AddEditCounterEvent.EnteredName -> {
                _uiState.update {
                    it.copy(counterName = addEditCounterEvent.value)
                }
            }
            is AddEditCounterEvent.EnteredDescription -> {
                _uiState.update {
                    it.copy(counterDescription = addEditCounterEvent.value)
                }
            }
            is AddEditCounterEvent.EnteredValue -> {
                _uiState.update {
                    it.copy(counterValue = addEditCounterEvent.value)
                }
            }
            AddEditCounterEvent.Cancel -> {
                _uiState.update {
                    it.copy(isCounterCanceled = true)
                }
            }
            AddEditCounterEvent.Save -> {
                viewModelScope.launch {
                    val counter = _uiState.value.let { state ->
                        Counter(
                            id = counterId,
                            counterName = state.counterName,
                            counterDescription = state.counterDescription,
                            counterSavedValue = state.counterValue ?: 0,
                            counterDate = System.currentTimeMillis()
                        )
                    }
                    val counterValidationResult = counterUseCases.insertCounterUseCase(counter)

                    _uiState.update {
                        if (counterValidationResult.successful) {
                            it.copy(isCounterSaved = true)
                        } else {
                            it.copy(userMessage = counterValidationResult.errorMessage)
                        }
                    }
                }
            }
        }
    }
}