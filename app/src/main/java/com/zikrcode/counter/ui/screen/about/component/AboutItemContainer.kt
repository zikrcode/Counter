package com.zikrcode.counter.ui.screen.about.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.zikrcode.counter.ui.composables.AppVerticalSpacer
import com.zikrcode.counter.ui.theme.CounterTheme
import com.zikrcode.counter.ui.utils.Dimens

@Composable
fun AboutItemContainer(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = CounterTheme.colorScheme.text,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        AppVerticalSpacer(Dimens.SpacingSingle)
        content()
    }
}

@PreviewLightDark
@Composable
private fun AboutItemContainerPreview() {
    CounterTheme {
        AboutItemContainer(
            label = "Label",
            content = {
                Box(modifier = Modifier.size(150.dp))
            }
        )
    }
}