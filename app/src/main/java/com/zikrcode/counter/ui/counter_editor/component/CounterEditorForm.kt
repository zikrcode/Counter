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

package com.zikrcode.counter.ui.counter_editor.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.text.isDigitsOnly
import com.zikrcode.counter.R
import com.zikrcode.counter.ui.utils.Dimens
import com.zikrcode.counter.ui.utils.AppConstants

@Composable
fun AddEditCounterForm(
    modifier: Modifier = Modifier,
    counterNameState: State<String>,
    onCounterNameChange: (String) -> Unit,
    counterDescriptionState: State<String>,
    onCounterDescriptionChange: (String) -> Unit,
    counterValueState: State<String>,
    onCounterValueChange: (String) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = Dimens.SpacingSingle,
                    top = Dimens.SpacingSingle,
                    end = Dimens.SpacingSingle
                )
        ) {
            OutlinedTextField(
                value = counterNameState.value,
                onValueChange = {
                    onCounterNameChange(it)
                },
                modifier = Modifier.weight(1f),
                label = {
                    Text(text = stringResource(R.string.counter_name))
                },
                maxLines = 1,
                minLines = 1
            )
            Spacer(Modifier.width(Dimens.SpacingDouble))
            OutlinedTextField(
                value = counterValueState.value,
                onValueChange = {
                    if (it.isDigitsOnly() && it.length <= AppConstants.MAX_COUNTER_VALUE_LENGTH) {
                        onCounterValueChange(it)
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
                maxLines = 1,
                minLines = 1
            )
        }
        OutlinedTextField(
            value = counterDescriptionState.value,
            onValueChange = {
                onCounterDescriptionChange(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingSingle),
            label = {
                Text(text = stringResource(R.string.counter_description))
            },
            maxLines = 6,
            minLines = 6
        )
    }
}

@Preview
@Composable
fun AddCounterFormPreview() {
    val state = remember { mutableStateOf("") }
    AddEditCounterForm(
        counterNameState = state,
        onCounterNameChange = { },
        counterDescriptionState = state,
        onCounterDescriptionChange = { },
        counterValueState = state,
        onCounterValueChange = { }
    )
}