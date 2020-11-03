package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase7

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.*
import timber.log.Timber

class TimeoutAndRetryViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequest() {
        val numberOfTries = 2
        val timeout = 1000L

        val oreoVersionDeferred = viewModelScope.async {
            retry(numberOfTries, timeout) {
                api.getAndroidVersionFeatures(27)
            }
        }

        val pieVersionDeferred = viewModelScope.async {
            retryWithTimeOut(numberOfTries, timeout) {
                api.getAndroidVersionFeatures(28)
            }
        }

        viewModelScope.launch {

            try {
                val versionFeatures = listOf(oreoVersionDeferred, pieVersionDeferred).awaitAll()
                UiState.Success(versionFeatures)
            } catch (exception: Exception) {
                Timber.e(exception)
                uiState.value = UiState.Error("Network error occurred")
            }
        }
    }


    suspend fun <T> retryWithTimeOut(
        numberOfRetries: Int,
        timeout: Long,
        block: suspend () -> T
    ) = retry(numberOfRetries) {
        withTimeout(timeout) {
            block()
        }
    }


    private suspend fun <T> retry(
        numberOfRetries: Int,
        delayBetweenRetries: Long = 100,
        block: suspend () -> T
    ): T {
        repeat(numberOfRetries) {
            try {
                return block()
            } catch (exception: java.lang.Exception) {
                Timber.e(exception)
            }
            delay(delayBetweenRetries)
        }
        return block()
    }

}
