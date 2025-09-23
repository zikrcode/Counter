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

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zikrcode.counter.domain.model.Counter
import com.zikrcode.counter.domain.use_case.CounterUseCases
import com.zikrcode.counter.ui.utils.UiText
import com.zikrcode.counter.ui.utils.navigation.MainNavigationArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditCounterViewModel @Inject constructor(
    private val counterUseCases: CounterUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var currentCounterId: Int? = null

    private val _counterName = mutableStateOf("")
    val counterName: State<String> = _counterName

    private val _counterDescription = mutableStateOf("")
    val counterDescription: State<String> = _counterDescription

    private val _counterValue = mutableStateOf("")
    val counterValue: State<String> = _counterValue

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Int>(MainNavigationArgs.COUNTER_ID_ARG)?.let { counterId ->
            if (counterId != -1) {
                viewModelScope.launch {
                    counterUseCases.counterByIdUseCase(counterId).first().also { counter ->
                        currentCounterId = counter.id
                        _counterName.value = counter.counterName
                        _counterDescription.value = counter.counterDescription
                        _counterValue.value = counter.counterSavedValue.toString()
                    }
                }
            }
        }
    }

    fun onEvent(addEditCounterEvent: AddEditCounterEvent) {

        when (addEditCounterEvent) {
            is AddEditCounterEvent.EnteredName -> {
                _counterName.value = addEditCounterEvent.value
            }
            is AddEditCounterEvent.EnteredValue -> {
                _counterValue.value = addEditCounterEvent.value
            }
            is AddEditCounterEvent.EnteredDescription -> {
                _counterDescription.value = addEditCounterEvent.value
            }
            AddEditCounterEvent.GoBack -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.NavigateBack)
                }
            }
            AddEditCounterEvent.Cancel -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.CounterCanceled)
                }
            }
            AddEditCounterEvent.Save -> {
                viewModelScope.launch {
                    val counterValidationResult = counterUseCases.insertCounterUseCase(
                        Counter(
                            id = currentCounterId,
                            counterName = counterName.value,
                            counterDescription = counterDescription.value,
                            counterDate = System.currentTimeMillis(),
                            counterSavedValue = counterValue.value.toIntOrNull() ?: 0
                        )
                    )

                    counterValidationResult.errorMessage?.let {
                        _eventFlow.emit(UiEvent.ShowSnackbar(it))
                    } ?: run {
                        _eventFlow.emit(UiEvent.CounterSaved)
                    }
                }
            }
        }
    }

    sealed class UiEvent {

        data class ShowSnackbar(val message: UiText) : UiEvent()

        object NavigateBack : UiEvent()

        object CounterCanceled : UiEvent()

        object CounterSaved : UiEvent()
    }
}