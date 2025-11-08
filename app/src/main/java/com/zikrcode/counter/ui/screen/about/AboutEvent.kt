package com.zikrcode.counter.ui.screen.about

sealed interface AboutEvent {
    data object GoBack : AboutEvent
    data object NavigationHandled : AboutEvent
}
