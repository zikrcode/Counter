package com.zikrcode.counter.ui.screen.about

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class AboutUiState(
    val navTarget: AboutNavTarget = AboutNavTarget.Idle
)

sealed interface AboutNavTarget {
    data object NavigateBack : AboutNavTarget
    data object Idle : AboutNavTarget
}

@HiltViewModel
class AboutViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AboutUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: AboutEvent) = when (event) {
        AboutEvent.GoBack -> {
            _uiState.update { state ->
                state.copy(navTarget = AboutNavTarget.NavigateBack)
            }
        }
        AboutEvent.NavigationHandled -> {
            _uiState.update { state ->
                state.copy(navTarget = AboutNavTarget.Idle)
            }
        }
    }
}
