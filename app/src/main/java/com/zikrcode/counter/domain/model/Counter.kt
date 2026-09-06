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

/**
 * A counter as the rest of the app thinks about it.
 *
 * [id] is null until the counter has been saved for the first time; the storage layer maps that
 * onto whatever it needs to auto-assign a row id.
 */
data class Counter(
    val id: Int? = null,
    val name: String,
    val description: String,
    val value: Int,
    val step: Int = DEFAULT_STEP,
    val target: Int? = null,
    val loopOnTarget: Boolean = false,
    val rounds: Int = 0,
    val sortIndex: Int = 0,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
) {
    companion object {

        const val DEFAULT_STEP = 1

        fun instance() = Counter(name = "Coding", description = "Coding sessions", value = 50, createdAt = System.currentTimeMillis(), updatedAt = System.currentTimeMillis())
    }
}
