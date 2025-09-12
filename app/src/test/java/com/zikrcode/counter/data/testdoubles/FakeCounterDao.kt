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

package com.zikrcode.counter.data.testdoubles

import com.zikrcode.counter.data.data_source.CounterDao
import com.zikrcode.counter.domain.model.Counter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeCounterDao : CounterDao {

    private var _counters: MutableMap<Int, Counter> = mutableMapOf()
    var counters: List<Counter>
        get() = _counters.values.toList()
        set(newCounters) {
            _counters = newCounters.associateBy { it.id!! }.toMutableMap()
        }

    override fun counterById(id: Int): Flow<Counter> {
        return flowOf(_counters[id]!!)
    }

    override fun allCounters(): Flow<List<Counter>> {
        return flowOf(counters)
    }

    override suspend fun insertCounter(counter: Counter) {
        _counters[counter.id!!] = counter
    }

    override suspend fun deleteCounter(counter: Counter) {
        _counters.remove(counter.id)
    }
}