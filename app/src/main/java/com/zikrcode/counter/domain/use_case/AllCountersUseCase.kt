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

import com.zikrcode.counter.domain.model.Counter
import com.zikrcode.counter.domain.repository.CounterRepository
import com.zikrcode.counter.domain.utils.CounterOrder
import com.zikrcode.counter.domain.utils.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AllCountersUseCase(
    private val counterRepository: CounterRepository
) {

    operator fun invoke(
        counterOrder: CounterOrder = CounterOrder.Date(OrderType.DESCENDING)
    ): Flow<List<Counter>> {
        return counterRepository.allCounters().map { counters ->
            when (counterOrder.orderType) {
                OrderType.ASCENDING -> {
                    when (counterOrder) {
                        is CounterOrder.Name -> counters.sortedBy { it.counterName }
                        is CounterOrder.Date -> counters.sortedBy { it.counterDate }
                    }
                }
                OrderType.DESCENDING -> {
                    when (counterOrder) {
                        is CounterOrder.Name -> counters.sortedByDescending { it.counterName }
                        is CounterOrder.Date -> counters.sortedByDescending { it.counterDate }
                    }
                }
            }
        }
    }
}