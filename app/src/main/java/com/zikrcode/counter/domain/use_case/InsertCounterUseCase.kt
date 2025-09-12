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

package com.zikrcode.counter.domain.use_case

import com.zikrcode.counter.R
import com.zikrcode.counter.data.repository.CounterRepository
import com.zikrcode.counter.domain.model.Counter
import com.zikrcode.counter.domain.use_case.counter_validation.CounterValidationResult
import com.zikrcode.counter.ui.utils.UiText
import javax.inject.Inject

class InsertCounterUseCase @Inject constructor(
    private val counterRepository: CounterRepository
) {

    suspend operator fun invoke(counter: Counter): CounterValidationResult {
        if (counter.counterName.isBlank()) {
            return CounterValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.empty_counter_name)
            )
        }
        counterRepository.insertCounter(counter)
        return CounterValidationResult(successful = true)
    }
}