package com.zikrcode.counter.ui.screen.about

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.zikrcode.counter.ui.screen.about.component.AboutItemContainer
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zikrcode.counter.BuildConfig
import com.zikrcode.counter.R
import com.zikrcode.counter.ui.composables.AppHorizontalDivider
import com.zikrcode.counter.ui.composables.AppIconButton
import com.zikrcode.counter.ui.composables.AppScreenContent
import com.zikrcode.counter.ui.composables.AppVerticalSpacer
import com.zikrcode.counter.ui.theme.CounterColor
import com.zikrcode.counter.ui.theme.CounterTheme
import com.zikrcode.counter.ui.utils.AppConstants
import com.zikrcode.counter.ui.utils.Dimens

@Composable
fun AboutScreen(
    onNavigateBack: () -> Unit,
    viewModel: AboutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.navTarget) {
        when (uiState.navTarget) {
            AboutNavTarget.NavigateBack -> {
                onNavigateBack.invoke()
            }
            AboutNavTarget.Idle -> {
                // no-op
            }
        }
        viewModel.onEvent(AboutEvent.NavigationHandled)
    }

    AboutScreenContent(viewModel::onEvent)
}

@PreviewLightDark
@Composable
private fun AboutScreenContentPreview() {
    CounterTheme {
        AboutScreenContent { }
    }
}

@Composable
private fun AboutScreenContent(
    onEvent: (AboutEvent) -> Unit
) {
    AppScreenContent(
        title = stringResource(R.string.about),
        topBarStartIcon = {
            AppIconButton(
                onClick = {
                    onEvent.invoke(AboutEvent.GoBack)
                },
                icon = Icons.AutoMirrored.Outlined.ArrowBack,
                iconDescription = stringResource(R.string.go_back)
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingDouble)
        ) {
            AppInfoAboutItem()
            AppHorizontalDivider()
            PrivacyPolicyAboutItem()
            AppHorizontalDivider()
            DeveloperAboutItem()
            AppHorizontalDivider()
            SupportAboutItem()
            AppHorizontalDivider()
            FeedbackAboutItem()
        }
    }
}

@Composable
private fun AppInfoAboutItem() {
    val appVersion = BuildConfig.VERSION_NAME
    AboutItemContainer(label = stringResource(R.string.app_info)) {
        Text(
            text = stringResource(R.string.app_version, appVersion),
            color = CounterTheme.colorScheme.text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun PrivacyPolicyAboutItem() {
    AboutItemContainer(label = stringResource(R.string.privacy_policy)) {
        BasicText(
            text = buildAnnotatedString {
                withLink(
                    LinkAnnotation.Url(
                        url = AppConstants.PRIVACY_POLICY_URL,
                        styles = TextLinkStyles(
                            style = SpanStyle(
                                color = CounterColor.BLUE,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    )
                ) {
                    append(
                        stringResource(R.string.view_privacy_policy)
                    )
                }
            },
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun DeveloperAboutItem() {
    AboutItemContainer(label = stringResource(R.string.developer)) {
        val name = stringResource(id = R.string.developer_name)
        val fullText = stringResource(id = R.string.developer_info, name)

        BasicText(
            text = buildAnnotatedString {
                val start = fullText.indexOf(name)
                val end = start + name.length
                append(fullText)

                addLink(
                    LinkAnnotation.Url(
                        url = AppConstants.LINKEDIN_URL,
                        styles = TextLinkStyles(
                            style = SpanStyle(
                                color = CounterColor.BLUE,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    ),
                    start = start,
                    end = end
                )
            },
            style = MaterialTheme.typography.bodyMedium.copy(
                color = CounterTheme.colorScheme.text
            )
        )
    }
}

@Composable
private fun SupportAboutItem() {
    val localUriHandler = LocalUriHandler.current
    AboutItemContainer(label = stringResource(R.string.support)) {
        Image(
            painter = painterResource(R.drawable.bmc_button),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(.5f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        localUriHandler.openUri(AppConstants.BUY_ME_A_COFFEE_URL)
                    }
                )
        )
    }
}

@Composable
private fun FeedbackAboutItem() {
    val context = LocalContext.current
    val appName = stringResource(R.string.app_name)
    val subject = stringResource(R.string.share_feedback_subject, appName)

    AboutItemContainer(label = stringResource(R.string.feedback)) {
        Text(
            text = stringResource(R.string.share_feedback_description),
            color = CounterTheme.colorScheme.text,
            style = MaterialTheme.typography.bodyMedium
        )
        AppVerticalSpacer(Dimens.SpacingSingle)
        OutlinedButton(
            onClick = {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = AppConstants.MAILTO.toUri()
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(AppConstants.EMAIL))
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                }
                context.startActivity(intent)
            },
            border = BorderStroke(
                width = 1.dp,
                color = CounterTheme.colorScheme.icon
            )
        ) {
            Text(
                text = stringResource(R.string.share_feedback),
                color = CounterTheme.colorScheme.main,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}