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

package com.zikrcode.counter.ui.counter_home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.zikrcode.counter.R
import com.zikrcode.counter.domain.model.Counter
import com.zikrcode.counter.ui.utils.Dimens

@Composable
fun CounterItem(
    counter: Counter,
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit,
    onResetClick: () -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = Dimens.SpacingDouble,
                    top = Dimens.SpacingSingle,
                    end = Dimens.SpacingDouble,
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onResetClick,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(Dimens.SpacingQuintuple)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_reset),
                    contentDescription = stringResource(R.string.reset)
                )
            }
            Text(
                text = counter.counterName,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = Dimens.SpacingSingle),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge
            )
            IconButton(
                onClick = onEditClick,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(Dimens.SpacingQuintuple)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = stringResource(R.string.edit)
                )
            }
        }
        Text(
            text = counter.counterDescription,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingSingle),
            textAlign = TextAlign.Center,
            maxLines = 6,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
fun CounterItemPreview() {
    CounterItem(
        counter = Counter.instance(),
        onEditClick = { },
        onResetClick = { }
    )
}