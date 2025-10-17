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

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.zikrcode.counter.ui.theme.CounterTheme
import com.zikrcode.counter.ui.utils.AppConstants
import com.zikrcode.counter.ui.utils.Dimens

@Composable
fun CircleButton(
    currentValue: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    vibrate: Boolean = false
) {
    val context = LocalContext.current
    val vibrator = context.getSystemService(Vibrator::class.java)

    ElevatedButton(
        onClick = {
            if (vibrate) {
                vibrator.vibrate(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
                    } else {
                        VibrationEffect.createOneShot(
                            AppConstants.VIBRATION_DURATION, VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    }
                )
            }
            onClick.invoke()
        },
        modifier = modifier.aspectRatio(1f),
        border = BorderStroke(
            width = Dimens.SpacingHalf,
            color = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = currentValue.toString(),
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayLarge
        )
    }
}

@PreviewLightDark
@Composable
private fun CircleButtonPreview() {
    CounterTheme {
        CircleButton(
            currentValue = 12345,
            onClick = { }
        )
    }
}
