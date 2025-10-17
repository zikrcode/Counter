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

package com.zikrcode.counter.ui.screen.counter_list

import com.zikrcode.counter.domain.model.Counter
import com.zikrcode.counter.domain.utils.CounterOrder

sealed interface CounterListEvent {
    data object GoBack : CounterListEvent
    data object ToggleOrderSection : CounterListEvent
    data class Order(val counterOrder: CounterOrder) : CounterListEvent
    data class SelectCounter(val id: Int) : CounterListEvent
    data class Edit(val id: Int) : CounterListEvent
    data class Delete(val counter: Counter) : CounterListEvent
    data object NewCounter : CounterListEvent
    data object RestoreCounter : CounterListEvent
    data object SnackbarShown: CounterListEvent
    data object NavigationHandled : CounterListEvent
}
