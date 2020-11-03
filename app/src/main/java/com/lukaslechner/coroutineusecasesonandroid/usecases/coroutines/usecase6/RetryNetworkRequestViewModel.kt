package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase6

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1.UiState.*
import kotlinx.coroutines.launch
import timber.log.Timber

class RetryNetworkRequestViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequest() {
        uiState.value = UiState.Loading

        viewModelScope.launch {

            val numberOfRetries = 2
            try {
               retry(numberOfRetries){
                       loadRecentAndroidVersions()
               }
            } catch (exception: Exception) {
                Timber.e(exception)
                uiState.value = UiState.Error("Network error occurred")
            }
        }
    }

    private suspend fun <T> retry(numberOfTries:Int, block: suspend () -> T):T{
        repeat(numberOfTries) {
            try {
                return block()
            } catch (exception: Exception) {
                Timber.e(exception)
            }
        }
        return  block()
    }

    private suspend fun loadRecentAndroidVersions() {
        val recentAndroidVersions = api.getRecentAndroidVersions()
        uiState.value = UiState.Success(recentAndroidVersions)
    }

}