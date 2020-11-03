package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase5

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1.UiState.*
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber

class NetworkRequestWithTimeoutViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequest(timeout: Long) {
        withTimeOutOrNull(timeout)
    }

    private fun withTimeOut(timeout: Long) {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val recentAndroidVersions = withTimeout(timeout) {
                    api.getRecentAndroidVersions()
                }
                uiState.value = UiState.Success(recentAndroidVersions)
            } catch (timeout: TimeoutCancellationException) {
                uiState.value = UiState.Error("Network request timed out!")
            } catch (exception: Exception) {
                Timber.e(exception)
                uiState.value = UiState.Error("Network error occurred")
            }
        }
    }

    private fun withTimeOutOrNull(timeout: Long) {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val recentAndroidVersions = withTimeoutOrNull(timeout) {
                    api.getRecentAndroidVersions()
                }
                if(recentAndroidVersions!= null){
                    uiState.value = UiState.Success(recentAndroidVersions)
                }else{
                    uiState.value = UiState.Error("Network request timed out!")
                }
            } catch (exception: Exception) {
                Timber.e(exception)
                uiState.value = UiState.Error("Network error occurred")
            }
        }
    }

}