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

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.compose) apply false

    // KSP (Kotlin Symbol Processing)
    alias(libs.plugins.devtools.ksp) apply false

    // Hilt
    alias(libs.plugins.hilt.android) apply false

    // Kotlin Serialization
    alias(libs.plugins.jetbrains.kotlin.serialization) apply false

    // Google Services
    alias(libs.plugins.google.services.plugin) apply false

    // Firebase Crashlytics
    alias(libs.plugins.firebase.crashlytics.plugin) apply false
}