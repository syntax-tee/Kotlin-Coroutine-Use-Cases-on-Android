package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class PerformNetworkRequestsConcurrentlyViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequestsSequentially() {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val oreoFeatures = mockApi.getAndroidVersionFeatures(27)
                val pieFeatures = mockApi.getAndroidVersionFeatures(28)
                val android10Features = mockApi.getAndroidVersionFeatures(29)

                val versionFeatures = listOf(oreoFeatures, pieFeatures, android10Features)
                uiState.value = UiState.Success(versionFeatures)

            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network request failed")
            }
        }
    }


    fun performNetworkRequestsConcurrently() {
        uiState.value = UiState.Loading

        val  deferredOreoFeatures = viewModelScope.async {
             mockApi.getAndroidVersionFeatures(27)
        }


        val  deferredPieFeatures = viewModelScope.async {
              mockApi.getAndroidVersionFeatures(28)
        }


        val   deferredandroid10Features = viewModelScope.async {
             mockApi.getAndroidVersionFeatures(29)
        }

        viewModelScope.launch {

            try {
               //  val oreofeatures =  deferredOreoFeatures.await()
               // val pieFeatures = deferredPieFeatures.await()
               //  val android10Features = deferredandroid10Features.await()
               //  val versionFeatures = listOf(oreofeatures,pieFeatures,android10Features)

                val versionFeatures = awaitAll(deferredOreoFeatures,deferredPieFeatures,deferredandroid10Features)
                uiState.value = UiState.Success(versionFeatures)

            }catch (exception:Exception){
                uiState.value = UiState.Error("Network Request Failed")
            }
        }



    }
}