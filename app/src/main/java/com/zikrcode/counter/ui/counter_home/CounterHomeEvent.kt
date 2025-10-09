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

package com.zikrcode.counter.ui.counter_home

sealed interface CounterHomeEvent {
    data object Settings : CounterHomeEvent
    data object List : CounterHomeEvent
    data object Edit : CounterHomeEvent
    data object NavigationHandled : CounterHomeEvent
    data object Reset : CounterHomeEvent
    data object Increment : CounterHomeEvent
    data object Decrement : CounterHomeEvent
}
