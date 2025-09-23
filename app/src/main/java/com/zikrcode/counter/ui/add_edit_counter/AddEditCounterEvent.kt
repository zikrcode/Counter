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

package com.zikrcode.counter.ui.add_edit_counter

sealed class AddEditCounterEvent {

    data class EnteredName(val value: String) : AddEditCounterEvent()

    data class EnteredValue(val value: String) : AddEditCounterEvent()

    data class EnteredDescription(val value: String) : AddEditCounterEvent()

    object GoBack : AddEditCounterEvent()

    object Cancel : AddEditCounterEvent()

    object Save : AddEditCounterEvent()
}