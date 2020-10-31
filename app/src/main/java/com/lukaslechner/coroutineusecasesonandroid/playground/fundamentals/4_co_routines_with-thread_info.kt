package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    joinAll(
        async { threadSwitchingCoroutine(1,500) },
        async {  threadSwitchingCoroutine(2,300) },
        async{
            repeat(5){
                println("other task is working on ${Thread.currentThread().name}")
                delay(100)
            }
        }
    )

    println("main ends")
}

suspend fun threadSwitchingCoroutine(number:Int,delay:Long){
    println("Coroutine $number starts work on ${Thread.currentThread().name}")
    delay(delay)
    println("Coroutine $number has finished on ${Thread.currentThread().name}")
}