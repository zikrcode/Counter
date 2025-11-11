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

package com.zikrcode.counter.domain.use_case

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.zikrcode.counter.domain.repository.UserPreferencesRepository

class WriteUserPreferenceUseCase(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun <T> invoke(key: Preferences.Key<T>, value: T) {
        userPreferencesRepository.getDataStore().edit { preferences ->
            preferences[key] = value
        }
    }
}