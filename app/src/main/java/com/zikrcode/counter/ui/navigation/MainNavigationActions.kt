package com.zikrcode.counter.ui.navigation

import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable

// Routes  nested graph
@Serializable object CounterHome
@Serializable object CounterList
@Serializable object CounterSettings
@Serializable data class CounterEditor(val counterId: Int?)

class MainNavigationActions(private val navController: NavHostController) {

    fun navigateBack() {
        navController.navigateUp()
    }

    fun navigateToCounterEditor(counterId: Int?) {
        navController.navigate(
            CounterEditor(counterId)
        )
    }

    fun navigateToCounterHome() {
        navController.navigate(CounterHome) {
            popUpTo(CounterHome) {
                saveState = true
            }
            restoreState = true
        }
    }
}