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

package com.zikrcode.counter.data.data_source.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zikrcode.counter.domain.model.Counter

@Entity(tableName = "counter")
data class CounterEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = UNSAVED_ID,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "value") val value: Int,
    @ColumnInfo(name = "step", defaultValue = "1") val step: Int = Counter.DEFAULT_STEP,
    @ColumnInfo(name = "target") val target: Int? = null,
    @ColumnInfo(name = "loop_on_target", defaultValue = "0") val loopOnTarget: Boolean = false,
    @ColumnInfo(name = "rounds", defaultValue = "0") val rounds: Int = 0,
    @ColumnInfo(name = "sort_index", defaultValue = "0") val sortIndex: Int = 0,
    @ColumnInfo(name = "created_at", defaultValue = "0") val createdAt: Long = 0L,
    @ColumnInfo(name = "updated_at", defaultValue = "0") val updatedAt: Long = 0L
) {
    companion object {

        /** Room treats 0 in an auto-generated key as "not set" and lets SQLite assign the id. */
        const val UNSAVED_ID = 0
    }
}

fun CounterEntity.toCounter() = Counter(
    id = id,
    name = name,
    description = description,
    value = value,
    step = step,
    target = target,
    loopOnTarget = loopOnTarget,
    rounds = rounds,
    sortIndex = sortIndex,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Counter.toCounterEntity() = CounterEntity(
    id = id ?: CounterEntity.UNSAVED_ID,
    name = name,
    description = description,
    value = value,
    step = step,
    target = target,
    loopOnTarget = loopOnTarget,
    rounds = rounds,
    sortIndex = sortIndex,
    createdAt = createdAt,
    updatedAt = updatedAt
)
