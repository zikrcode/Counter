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

package com.zikrcode.counter.ui.screen.settings

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zikrcode.counter.domain.use_case.CounterUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val isLoading: Boolean = false,
    val vibrateOnTap: Boolean = false,
    val keepScreenOn: Boolean = false,
    val navTarget: SettingsNavTarget = SettingsNavTarget.Idle
)

sealed interface SettingsNavTarget {
    data object NavigateBack : SettingsNavTarget
    data object About : SettingsNavTarget
    data object Idle : SettingsNavTarget
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val counterUseCases: CounterUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        collectPreferences()
    }

    private fun collectPreferences() {
        _uiState.update { state ->
            state.copy(isLoading = true)
        }

        viewModelScope.apply {
            launch {
                counterUseCases.readUserPreferenceUseCase(
                    booleanPreferencesKey(PreferencesKey.VIBRATE_PREF_KEY)
                ).collectLatest { vibrateOnTap ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            vibrateOnTap = vibrateOnTap ?: false
                        )
                    }
                }
            }
            launch {
                counterUseCases.readUserPreferenceUseCase(
                    booleanPreferencesKey(PreferencesKey.KEEP_SCREEN_ON_PREF_KEY)
                ).collectLatest { keepScreenOn ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            keepScreenOn = keepScreenOn ?: false
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: SettingsEvent) = when (event) {
        SettingsEvent.GoBack -> {
            _uiState.update { state ->
                state.copy(navTarget = SettingsNavTarget.NavigateBack)
            }
        }

        SettingsEvent.About -> {
            _uiState.update { state ->
                state.copy(navTarget = SettingsNavTarget.About)
            }
        }

        SettingsEvent.VibrateOnTapPreferenceChanged -> {
            viewModelScope.launch {
                counterUseCases.writeUserPreferenceUseCase(
                    key = booleanPreferencesKey(PreferencesKey.VIBRATE_PREF_KEY),
                    value = !_uiState.value.vibrateOnTap
                )
            }
        }

        SettingsEvent.KeepScreenOnPreferenceChanged -> {
            viewModelScope.launch {
                counterUseCases.writeUserPreferenceUseCase(
                    key = booleanPreferencesKey(PreferencesKey.KEEP_SCREEN_ON_PREF_KEY),
                    value = !_uiState.value.keepScreenOn
                )
            }
        }

        SettingsEvent.NavigationHandled -> {
            _uiState.update { state ->
                state.copy(navTarget = SettingsNavTarget.Idle)
            }
        }
    }
}
