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

package com.zikrcode.counter.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "counter")
data class Counter(
    @PrimaryKey val id: Int? = null,
    @ColumnInfo(name = "counter_name") val counterName: String,
    @ColumnInfo(name = "counter_description") val counterDescription: String,
    @ColumnInfo(name = "counter_date") val counterDate: Long,
    @ColumnInfo(name = "counter_saved_value") val counterSavedValue: Int
) {
    companion object {

        fun instance() = Counter(counterName = "Coding", counterDescription = "Coding sessions", counterDate = System.currentTimeMillis(), counterSavedValue = 50)
    }
}