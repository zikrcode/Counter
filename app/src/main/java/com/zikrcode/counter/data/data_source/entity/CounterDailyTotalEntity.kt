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
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * One row per counter per local day. Nothing reads or writes this table yet — it exists so the
 * schema and its cascade are in place before the stats work in Phase 2 depends on them.
 */
@Entity(
    tableName = "counter_daily_total",
    primaryKeys = ["counter_id", "epoch_day"],
    foreignKeys = [
        ForeignKey(
            entity = CounterEntity::class,
            parentColumns = ["id"],
            childColumns = ["counter_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["epoch_day"])]
)
data class CounterDailyTotalEntity(
    @ColumnInfo(name = "counter_id") val counterId: Int,
    @ColumnInfo(name = "epoch_day") val epochDay: Long,
    @ColumnInfo(name = "total") val total: Int
)
