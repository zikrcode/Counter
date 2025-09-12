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

package com.zikrcode.counter.ui.counter_settings

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

data class CounterSettingsUiState(
    val vibrateOnTap: Boolean = false,
    val keepScreenOn: Boolean = false
)

@HiltViewModel
class CounterSettingsViewModel @Inject constructor(
    private val counterUseCases: CounterUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(CounterSettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        collectPreferences()
    }

    private fun collectPreferences() {
        viewModelScope.apply {
            launch {
                counterUseCases.readUserPreferenceUseCase(
                    booleanPreferencesKey(PreferencesKey.VIBRATE_PREF_KEY)
                ).collectLatest { value ->
                    value?.let {
                        _uiState.update {
                            it.copy(vibrateOnTap = value)
                        }
                    }
                }
            }
            launch {
                counterUseCases.readUserPreferenceUseCase(
                    booleanPreferencesKey(PreferencesKey.KEEP_SCREEN_ON_PREF_KEY)
                ).collectLatest { value ->
                    value?.let {
                        _uiState.update {
                            it.copy(keepScreenOn = value)
                        }
                    }
                }
            }
        }
    }

    fun onEvent(counterSettingsEvent: CounterSettingsEvent) {
        when (counterSettingsEvent) {
            is CounterSettingsEvent.PreferenceChanged -> {
                viewModelScope.launch {
                    when (counterSettingsEvent.key) {
                        PreferencesKey.VIBRATE_PREF_KEY -> {
                            counterUseCases.writeUserPreferenceUseCase(
                                key = booleanPreferencesKey(PreferencesKey.VIBRATE_PREF_KEY),
                                value = !_uiState.value.vibrateOnTap
                            )
                        }
                        PreferencesKey.KEEP_SCREEN_ON_PREF_KEY -> {
                            counterUseCases.writeUserPreferenceUseCase(
                                key = booleanPreferencesKey(PreferencesKey.KEEP_SCREEN_ON_PREF_KEY),
                                value = !_uiState.value.keepScreenOn
                            )
                        }
                    }
                }
            }
        }
    }
}