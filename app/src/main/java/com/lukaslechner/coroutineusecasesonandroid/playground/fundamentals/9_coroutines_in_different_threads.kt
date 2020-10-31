package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import kotlinx.coroutines.*


fun main() = runBlocking {
    joinAll(
        async { suspendingCoroutine(1,500) },
        async {  suspendingCoroutine(2,300) }
    )

    println("main ends")
}

suspend fun suspendingCoroutine(number:Int,delay:Long){
    println("Coroutine $number starts work on ${Thread.currentThread().name}")
    delay(delay)
    withContext(Dispatchers.Default){
        println("Coroutine $number has finished on ${Thread.currentThread().name}")
    }
}