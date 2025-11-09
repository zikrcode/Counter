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

package com.zikrcode.counter.ui.screen.counter_editor

sealed interface CounterEditorEvent {
    data object GoBack : CounterEditorEvent
    data object RestoreCounter : CounterEditorEvent
    data class NameChanged(val name: String) : CounterEditorEvent
    data class DescriptionChanged(val description: String) : CounterEditorEvent
    data class ValueChanged(val value: Int?) : CounterEditorEvent
    data object Cancel : CounterEditorEvent
    data object Save : CounterEditorEvent
    data object MessageShown : CounterEditorEvent
    data object NavigationHandled : CounterEditorEvent
}
