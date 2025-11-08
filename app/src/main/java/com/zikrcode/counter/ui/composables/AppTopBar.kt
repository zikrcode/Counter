package com.zikrcode.counter.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FeaturedPlayList
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.zikrcode.counter.ui.theme.CounterTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    startIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    endIcon: (@Composable () -> Unit)? = null
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = title)
        },
        modifier = modifier,
        navigationIcon = { startIcon.invoke() },
        actions = { endIcon?.invoke() },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = CounterTheme.colorScheme.background,
            titleContentColor = CounterTheme.colorScheme.text,
        )
    )
}

@PreviewLightDark
@Composable
private fun AppTopBarPreview() {
    CounterTheme {
        AppTopBar(
            title = "Counter",
            startIcon = {
                AppIconButton(
                    onClick = { },
                    icon = Icons.Outlined.Settings,
                    iconDescription = ""
                )
            },
            endIcon = {
                AppIconButton(
                    onClick = { },
                    icon = Icons.AutoMirrored.Outlined.FeaturedPlayList,
                    iconDescription = ""
                )
            }
        )
    }
}
