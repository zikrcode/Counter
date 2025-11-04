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

package com.zikrcode.counter.ui.screen.counter_editor.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.zikrcode.counter.R
import com.zikrcode.counter.ui.theme.CounterTheme
import com.zikrcode.counter.ui.utils.Dimens
import com.zikrcode.counter.ui.utils.AppConstants

@Composable
fun CounterEditorForm(
    counterName: String,
    onCounterNameChange: (String) -> Unit,
    counterDescription: String,
    onCounterDescriptionChange: (String) -> Unit,
    counterValue: Int,
    onCounterValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = counterName,
                onValueChange = onCounterNameChange,
                modifier = Modifier.weight(1f),
                label = {
                    Text(text = stringResource(R.string.counter_name))
                },
                singleLine = true,
                maxLines = 1,
                colors = editorTextFieldColors()
            )
            Spacer(Modifier.width(Dimens.SpacingDouble))
            OutlinedTextField(
                value = counterValue.toString(),
                onValueChange = { value ->
                    val counterValue = value.toIntOrNull()
                    if (counterValue in AppConstants.COUNTER_VALUE_RANGE) {
                        onCounterValueChange(counterValue!!)
                    }
                },
                modifier = Modifier.width(
                    Dimens.SpacingDouble * AppConstants.MAX_COUNTER_VALUE_LENGTH
                ),
                textStyle = TextStyle(textAlign = TextAlign.Center),
                label = {
                    Text(text = stringResource(R.string.counter_value))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                maxLines = 1,
                colors = editorTextFieldColors()
            )
        }
        OutlinedTextField(
            value = counterDescription,
            onValueChange = onCounterDescriptionChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimens.SpacingSingle),
            label = {
                Text(text = stringResource(R.string.counter_description))
            },
            maxLines = 4,
            minLines = 4,
            colors = editorTextFieldColors()
        )
    }
}

@Composable
fun editorTextFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors().copy(
    focusedTextColor = CounterTheme.colorScheme.text,
    unfocusedTextColor = CounterTheme.colorScheme.text,
    cursorColor = CounterTheme.colorScheme.main,
    textSelectionColors = TextSelectionColors(
        handleColor = CounterTheme.colorScheme.main,
        backgroundColor = CounterTheme.colorScheme.mainVariant,
    ),
    focusedIndicatorColor = CounterTheme.colorScheme.main,
    unfocusedIndicatorColor = CounterTheme.colorScheme.text,
    focusedLabelColor = CounterTheme.colorScheme.main,
    unfocusedLabelColor = CounterTheme.colorScheme.text
)

@PreviewLightDark
@Composable
private fun CounterEditorFormPreview() {
    CounterTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CounterTheme.colorScheme.background)
        ) {
            CounterEditorForm(
                counterName = "Sample Counter",
                onCounterNameChange = { },
                counterDescription = "",
                onCounterDescriptionChange = { },
                counterValue = 23,
                onCounterValueChange = { }
            )
        }
    }
}
