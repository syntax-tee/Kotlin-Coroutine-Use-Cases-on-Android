package com.lukaslechner.coroutineusecasesonandroid.playground.coroutinebilders

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {

      var startTime = System.currentTimeMillis()

      val deffered1 =   async {
             val result1 = networkCall(1)
             println("result received: $result1 after ${elapsedMillis(startTime)}ms")
            result1
         }

       val deffered2 =  async {
            val result2 = networkCall(2)
           println("result received: $result2 after ${elapsedMillis(startTime)}ms")
           result2
        }

       val resultList = listOf(deffered1.await(),deffered2.await())

    println("Result list: $resultList after ${elapsedMillis(startTime)}ms" )
}

suspend fun networkCall(number:Int):String{
    delay(500)
    return "Result $number"
}

fun elapsedMillis(startTime: Long) = System.currentTimeMillis() - startTime


