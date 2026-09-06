/*
 * Copyright (C) 2023–2026 Zokirjon Mamadjonov
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

import com.zikrcode.counter.domain.repository.CounterRepository

/**
 * Writes only the saved value of a single counter. Used by the tap path, which must not rewrite
 * the whole row on every increment.
 */
class UpdateCounterValueUseCase(
    private val counterRepository: CounterRepository
) {

    suspend operator fun invoke(id: Int, value: Int) {
        counterRepository.updateCounterValue(id, value)
    }
}
