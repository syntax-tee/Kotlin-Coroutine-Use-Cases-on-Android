package com.lukaslechner.coroutineusecasesonandroid.playground.coroutinebilders

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {
     val job =   launch(start = CoroutineStart.LAZY) {
            networkRequest()
            println("result received")
        }
    delay(200)
    job.start()
    println("end of runBlocking")
}

suspend fun  networkRequest():String{
   delay(500)
    return "Result"
}
