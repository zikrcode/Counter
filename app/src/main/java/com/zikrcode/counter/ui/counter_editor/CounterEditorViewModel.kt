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

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.zikrcode.counter.domain.model.Counter
import com.zikrcode.counter.domain.use_case.CounterUseCases
import com.zikrcode.counter.ui.navigation.AppRoute
import com.zikrcode.counter.ui.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterEditorViewModel @Inject constructor(
    private val counterUseCases: CounterUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args: AppRoute.CounterEditor = savedStateHandle.toRoute()
    private var counterId: Int? = args.counterId

    private val _counterName = mutableStateOf("")
    val counterName: State<String> = _counterName

    private val _counterDescription = mutableStateOf("")
    val counterDescription: State<String> = _counterDescription

    private val _counterValue = mutableStateOf("")
    val counterValue: State<String> = _counterValue

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        counterId?.let { id ->
            viewModelScope.launch {
                counterUseCases.counterByIdUseCase(id).first().also { counter ->
                    counterId = counter.id
                    _counterName.value = counter.counterName
                    _counterDescription.value = counter.counterDescription
                    _counterValue.value = counter.counterSavedValue.toString()
                }
            }
        }
    }

    fun onEvent(counterEditorEvent: CounterEditorEvent) {

        when (counterEditorEvent) {
            is CounterEditorEvent.EnteredName -> {
                _counterName.value = counterEditorEvent.value
            }
            is CounterEditorEvent.EnteredValue -> {
                _counterValue.value = counterEditorEvent.value
            }
            is CounterEditorEvent.EnteredDescription -> {
                _counterDescription.value = counterEditorEvent.value
            }
            CounterEditorEvent.GoBack -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.NavigateBack)
                }
            }
            CounterEditorEvent.Cancel -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.CounterCanceled)
                }
            }
            CounterEditorEvent.Save -> {
                viewModelScope.launch {
                    val counterValidationResult = counterUseCases.insertCounterUseCase(
                        Counter(
                            id = counterId,
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