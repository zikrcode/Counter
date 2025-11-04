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

package com.zikrcode.counter.ui.screen.counter.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.zikrcode.counter.R
import com.zikrcode.counter.ui.theme.CounterTheme
import com.zikrcode.counter.ui.utils.Dimens

@Composable
fun NoCounterAvailable() {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors().copy(
            containerColor = CounterTheme.colorScheme.container,
            contentColor = CounterTheme.colorScheme.text,
        )
    ) {
        Text(
            text = stringResource(R.string.no_counter_selected),
            modifier = Modifier.padding(Dimens.SpacingDouble),
            textAlign = TextAlign.Center
        )
    }
}

@PreviewLightDark
@Composable
private fun NoCounterAvailablePreview() {
    CounterTheme {
        NoCounterAvailable()
    }
}
