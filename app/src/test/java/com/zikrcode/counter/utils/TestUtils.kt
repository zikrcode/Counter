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

package com.zikrcode.counter.utils

import com.zikrcode.counter.domain.model.Counter

val testCounters = listOf(
    Counter(
        id = 1,
        counterName = "AName",
        counterDescription = "ADescription",
        counterDate = 1L,
        counterSavedValue = 11
    ),
    Counter(
        id = 2,
        counterName = "BName",
        counterDescription = "BDescription",
        counterDate = 2L,
        counterSavedValue = 22
    ),
    Counter(
        id = 3,
        counterName = "CName",
        counterDescription = "CDescription",
        counterDate = 3L,
        counterSavedValue = 33
    )
)

val testPreferencesString = mapOf(
    "S_KEY_1" to "Value1",
    "S_KEY_2" to "Value2",
    "S_KEY_3" to "Value3"
)

val testPreferencesInt = mapOf(
    "I_KEY_1" to 1,
    "I_KEY_2" to 2,
    "I_KEY_3" to 3
)

val testPreferencesBoolean = mapOf(
    "B_KEY_1" to true,
    "B_KEY_2" to false,
    "B_KEY_3" to true
)