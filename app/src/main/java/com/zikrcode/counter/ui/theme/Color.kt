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

package com.zikrcode.counter.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object CounterColor {
    val LINK_LIGHT = Color(0xFF0478B7)
    val LINK_DARK = Color(0xFF91CCFF)
}

val LightColorScheme = lightColorScheme(
    // Primary
    // Material's default here is tone 40 (#006E00). Tone 48 is used instead: the
    // lightest tone still clearing 4.5:1 against the page, measured 4.60:1, so the
    // green stays lively without failing contrast for small labels.
    primary = Color(0xFF008500),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF74FF58),
    onPrimaryContainer = Color(0xFF002200),
    inversePrimary = Color(0xFF54E23B),

    // Secondary
    secondary = Color(0xFF54634D),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD7E8CC),
    onSecondaryContainer = Color(0xFF121F0E),

    // Tertiary
    tertiary = Color(0xFF386568),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFBCEBEE),
    onTertiaryContainer = Color(0xFF002022),

    // Error
    error = Color(0xFFBA1B1B),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD4),
    onErrorContainer = Color(0xFF410001),

    // Surfaces
    background = Color(0xFFFAFAF3),
    onBackground = Color(0xFF1A1C19),
    surface = Color(0xFFFAFAF3),
    onSurface = Color(0xFF1A1C19),
    surfaceVariant = Color(0xFFDFE5D7),
    onSurfaceVariant = Color(0xFF43493F),
    surfaceTint = Color(0xFF008500),
    surfaceBright = Color(0xFFFAFAF3),
    surfaceDim = Color(0xFFDADBD4),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF4F4EE),
    surfaceContainer = Color(0xFFEEEEE8),
    surfaceContainerHigh = Color(0xFFE8E8E2),
    surfaceContainerHighest = Color(0xFFE3E3DD),
    inverseSurface = Color(0xFF2F312D),
    inverseOnSurface = Color(0xFFF1F1EB),

    // Outlines
    outline = Color(0xFF73796E),
    outlineVariant = Color(0xFFC2C8BB),
    scrim = Color(0xFF000000),

    // Fixed accents, identical in both schemes
    primaryFixed = Color(0xFF74FF58),
    primaryFixedDim = Color(0xFF54E23B),
    onPrimaryFixed = Color(0xFF002200),
    onPrimaryFixedVariant = Color(0xFF005300),
    secondaryFixed = Color(0xFFD7E8CC),
    secondaryFixedDim = Color(0xFFBBCBB1),
    onSecondaryFixed = Color(0xFF121F0E),
    onSecondaryFixedVariant = Color(0xFF3D4B37),
    tertiaryFixed = Color(0xFFBCEBEE),
    tertiaryFixedDim = Color(0xFFA0CFD1),
    onTertiaryFixed = Color(0xFF002022),
    onTertiaryFixedVariant = Color(0xFF1E4D50),
)

val DarkColorScheme = darkColorScheme(
    // Primary
    primary = Color(0xFF54E23B),
    onPrimary = Color(0xFF003A00),
    primaryContainer = Color(0xFF005300),
    onPrimaryContainer = Color(0xFF74FF58),
    inversePrimary = Color(0xFF006E00),

    // Secondary
    secondary = Color(0xFFBBCBB1),
    onSecondary = Color(0xFF273422),
    secondaryContainer = Color(0xFF3D4B37),
    onSecondaryContainer = Color(0xFFD7E8CC),

    // Tertiary
    tertiary = Color(0xFFA0CFD1),
    onTertiary = Color(0xFF00373A),
    tertiaryContainer = Color(0xFF1E4D50),
    onTertiaryContainer = Color(0xFFBCEBEE),

    // Error
    error = Color(0xFFFFB4A9),
    onError = Color(0xFF680003),
    errorContainer = Color(0xFF930006),
    onErrorContainer = Color(0xFFFFDAD4),

    // Surfaces
    background = Color(0xFF121411),
    onBackground = Color(0xFFE3E3DD),
    surface = Color(0xFF121411),
    onSurface = Color(0xFFE3E3DD),
    surfaceVariant = Color(0xFF43493F),
    onSurfaceVariant = Color(0xFFC2C8BB),
    surfaceTint = Color(0xFF54E23B),
    surfaceBright = Color(0xFF383A36),
    surfaceDim = Color(0xFF121411),
    surfaceContainerLowest = Color(0xFF0C0F0B),
    surfaceContainerLow = Color(0xFF1A1C19),
    surfaceContainer = Color(0xFF1E201C),
    surfaceContainerHigh = Color(0xFF282B26),
    surfaceContainerHighest = Color(0xFF333531),
    inverseSurface = Color(0xFFE3E3DD),
    inverseOnSurface = Color(0xFF2F312D),

    // Outlines
    outline = Color(0xFF8D9387),
    outlineVariant = Color(0xFF43493F),
    scrim = Color(0xFF000000),

    // Fixed accents, identical in both schemes
    primaryFixed = Color(0xFF74FF58),
    primaryFixedDim = Color(0xFF54E23B),
    onPrimaryFixed = Color(0xFF002200),
    onPrimaryFixedVariant = Color(0xFF005300),
    secondaryFixed = Color(0xFFD7E8CC),
    secondaryFixedDim = Color(0xFFBBCBB1),
    onSecondaryFixed = Color(0xFF121F0E),
    onSecondaryFixedVariant = Color(0xFF3D4B37),
    tertiaryFixed = Color(0xFFBCEBEE),
    tertiaryFixedDim = Color(0xFFA0CFD1),
    onTertiaryFixed = Color(0xFF002022),
    onTertiaryFixedVariant = Color(0xFF1E4D50),
)
