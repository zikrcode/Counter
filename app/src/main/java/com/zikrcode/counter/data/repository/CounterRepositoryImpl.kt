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

package com.zikrcode.counter.data.repository

import com.zikrcode.counter.data.data_source.CounterDao
import com.zikrcode.counter.data.data_source.entity.toCounter
import com.zikrcode.counter.data.data_source.entity.toCounterEntity
import com.zikrcode.counter.domain.model.Counter
import com.zikrcode.counter.domain.repository.CounterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CounterRepositoryImpl(
    private val counterDao: CounterDao
) : CounterRepository {

    override fun counterById(id: Int): Flow<Counter?> {
        return counterDao.counterById(id).map { it?.toCounter() }
    }

    override fun allCounters(): Flow<List<Counter>> {
        return counterDao.allCounters().map { counters ->
            counters.map { it.toCounter() }
        }
    }

    override suspend fun upsertCounter(counter: Counter) {
        counterDao.upsertCounter(counter.toCounterEntity())
    }

    override suspend fun updateCounterValue(id: Int, value: Int) {
        counterDao.updateCounterValue(id, value)
    }

    override suspend fun deleteCounter(counter: Counter) {
        counterDao.deleteCounter(counter.toCounterEntity())
    }
}