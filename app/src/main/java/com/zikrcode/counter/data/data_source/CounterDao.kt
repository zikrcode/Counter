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

package com.zikrcode.counter.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.zikrcode.counter.data.data_source.entity.CounterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CounterDao {

    /**
     * Emits null when no counter with [id] exists — for example when a stale preference or
     * navigation argument points at a counter that has since been deleted.
     */
    @Query("SELECT * FROM counter WHERE id = :id")
    fun counterById(id: Int): Flow<CounterEntity?>

    @Query("SELECT * FROM counter")
    fun allCounters(): Flow<List<CounterEntity>>

    /**
     * Deliberately [Upsert] rather than [androidx.room.Insert] with
     * [androidx.room.OnConflictStrategy.REPLACE]: SQLite implements REPLACE as DELETE-then-INSERT,
     * which would cascade through counter_daily_total and wipe the counter's history on every save.
     */
    @Upsert
    suspend fun upsertCounter(counter: CounterEntity)

    /**
     * Targeted write for the tap path. Touches one column of one row and never the row's identity,
     * so it cannot trigger the cascade that a full-row replace would.
     */
    @Query("UPDATE counter SET `value` = :value WHERE id = :id")
    suspend fun updateCounterValue(id: Int, value: Int)

    @Delete
    suspend fun deleteCounter(counter: CounterEntity)
}