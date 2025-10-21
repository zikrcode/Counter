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

package com.zikrcode.counter.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

object CounterColor {
    val MAIN = Color(0xFF29BF12)
    val MAIN_VARIANT = Color(0xFFABFF4F)
    val MAIN_LIGHT = Color(0xFF4ACF22)
    val MAIN_LIGHT_VARIANT = Color(0xFF8BEF40)
    val DR_WHITE = Color(0xFFFAFAFA)
    val BRIGHT_GRAY = Color(0xFFEAEAEA)
    val AMETHYST_HAZE = Color(0xFFA1A1AA)
    val DARK_GRAY = Color(0xFF44474A)
    val ONYX = Color(0xFF343A40)
    val WASHED_BLACK = Color(0xFF212529)
    val DRACULA_ORCHID = Color(0xFF2D3338)
    val WHITE = Color(0xFFFFFFFF)
    val RUSTY_RED = Color(0xFFD7263D)
    val PARADISE_PINK = Color(0xFFE94B5B)
    val BLACK = Color.Black
    val LIGHT_GRAY = Color.LightGray
    val BLUE = Color(0xFF0077b6)
}

@Immutable
data class CounterColorScheme(
    val background: Color,
    val container: Color,
    val divider: Color,
    val icon: Color,
    val iconDark: Color,
    val iconLight: Color,
    val main: Color,
    val mainVariant: Color,
    val red: Color,
    val text: Color
)

val LightCounterColorScheme = CounterColorScheme(
    background = CounterColor.DR_WHITE,
    container = CounterColor.BRIGHT_GRAY,
    divider = CounterColor.AMETHYST_HAZE,
    icon = CounterColor.DARK_GRAY,
    iconDark = CounterColor.DARK_GRAY,
    iconLight = CounterColor.WHITE,
    main = CounterColor.MAIN,
    mainVariant = CounterColor.MAIN_VARIANT,
    red = CounterColor.RUSTY_RED,
    text = CounterColor.DARK_GRAY
)

val DarkCounterColorScheme = CounterColorScheme(
    background = CounterColor.WASHED_BLACK,
    container = CounterColor.ONYX,
    divider = CounterColor.DRACULA_ORCHID,
    icon = CounterColor.WHITE,
    iconDark = CounterColor.DARK_GRAY,
    iconLight = CounterColor.WHITE,
    main = CounterColor.MAIN_LIGHT,
    mainVariant = CounterColor.MAIN_LIGHT_VARIANT,
    red = CounterColor.PARADISE_PINK,
    text = CounterColor.WHITE
)