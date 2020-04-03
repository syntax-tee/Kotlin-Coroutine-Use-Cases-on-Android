package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase9

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigInteger

class CalculationInBackgroundViewModel(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    fun performCalculation(factorialOf: Int) {
        viewModelScope.launch {
            uiState.value = UiState.Loading
            try {
                val computationStart = System.currentTimeMillis()
                val result = calculateFactorialOf(factorialOf)
                val computationDuration = System.currentTimeMillis() - computationStart

                val stringConversionStart = System.currentTimeMillis()
                val resultString = convertToString(result)
                val stringConversionDuration = System.currentTimeMillis() - stringConversionStart

                uiState.value =
                    UiState.Success(resultString, computationDuration, stringConversionDuration)
            } catch (exception: Exception) {
                    UiState.Error("Error while calculating result")
            }
        }
    }

    // factorial of n (n!) = 1 * 2 * 3 * 4 * ... * n
    private suspend fun calculateFactorialOf(number: Int): BigInteger =
        withContext(defaultDispatcher) {
            var factorial = BigInteger.ONE
            for (i in 1..number) {
                factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
            }
            factorial
        }

    private suspend fun convertToString(number: BigInteger): String =
        withContext(defaultDispatcher) {
            number.toString()
        }

    fun uiState(): LiveData<UiState> = uiState

    private val uiState: MutableLiveData<UiState> = MutableLiveData()

    sealed class UiState {
        object Loading : UiState()
        data class Success(
            val result: String,
            val computationDuration: Long,
            val stringConversionDuration: Long
        ) : UiState()
        data class Error(val message: String) : UiState()
    }
}