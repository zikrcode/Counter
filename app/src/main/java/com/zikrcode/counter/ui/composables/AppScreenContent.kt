package com.zikrcode.counter.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FeaturedPlayList
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.zikrcode.counter.ui.theme.CounterTheme
import com.zikrcode.counter.ui.utils.Dimens

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppScreenContent(
    title: String,
    topBarStartIcon: @Composable () -> Unit,
    topBarEndIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    floatingActionButton: @Composable () -> Unit = { },
    snackbarMessage: String? = null,
    onSnackbarShown: () -> Unit = { },
    content: @Composable () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message = message)
            onSnackbarShown.invoke()
        }
    }

    Scaffold(
        topBar = {
            Column {
                AppTopBar(
                    title = title,
                    startIcon = topBarStartIcon,
                    endIcon = topBarEndIcon
                )
                HorizontalDivider()
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = floatingActionButton
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Dimens.SpacingDouble),
            contentAlignment = Alignment.Center
        ) {
            if (loading) {
                CircularWavyProgressIndicator()
            } else {
                content()
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun AppScreenContentPreview() {
    CounterTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AppScreenContent(
                title = "Counter",
                topBarStartIcon = {
                    AppIconButton(
                        onClick = { },
                        icon = Icons.Outlined.Settings,
                        iconDescription = ""
                    )
                },
                topBarEndIcon = {
                    AppIconButton(
                        onClick = { },
                        icon = Icons.AutoMirrored.Outlined.FeaturedPlayList,
                        iconDescription = ""
                    )
                },
                loading = true,
                content = { }
            )
        }
    }
}
