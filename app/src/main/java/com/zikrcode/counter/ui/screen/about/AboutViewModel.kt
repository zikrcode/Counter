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

package com.zikrcode.counter.ui.screen.about

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class AboutUiState(
    val navTarget: AboutNavTarget = AboutNavTarget.Idle
)

sealed interface AboutNavTarget {
    data object NavigateBack : AboutNavTarget
    data object Idle : AboutNavTarget
}

@HiltViewModel
class AboutViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AboutUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: AboutEvent) = when (event) {
        AboutEvent.GoBack -> {
            _uiState.update { state ->
                state.copy(navTarget = AboutNavTarget.NavigateBack)
            }
        }
        AboutEvent.NavigationHandled -> {
            _uiState.update { state ->
                state.copy(navTarget = AboutNavTarget.Idle)
            }
        }
    }
}
