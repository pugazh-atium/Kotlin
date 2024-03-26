package com.example.kotlinapplication.networking

import android.util.Log
import com.example.kotlinapplication.ui.theme.AppConstant
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class RemoteApi {
    val BASE_URL = "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json"
    val Tag = "RemoteApi"

    interface Callback {
        fun onSuccess(response: String)
        fun onError(errorMessage: String)
    }

    fun getFact(callback: Callback) {
        Thread(Runnable {
            val connection = URL(BASE_URL).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")
            connection.setRequestProperty("X_Api_key", AppConstant.API_KEY)
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            connection.doInput = true
            try {
                val reader = InputStreamReader(connection.inputStream)
                reader.use { input ->
                    val response = StringBuilder()
                    val bufferedReader = BufferedReader(input)
                    bufferedReader.forEachLine {
                        response.append(it.trim())
                    }
                    callback.onSuccess(response.toString())
                }
            } catch (e: Exception) {
                callback.onError(e.localizedMessage ?: "Unknown error occurred")
            }
        }).start()
    }
}


//class RemoteApi {
//  val BASE_URL = "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json"
//  val Tag = "RemoteApi"
//
//    fun getFact() {
//      Thread(Runnable {
//         val  connection  = URL(BASE_URL).openConnection() as HttpURLConnection
//         connection.requestMethod = "GET"
//         connection.setRequestProperty("Content-Type" , "application/json")
//         connection.setRequestProperty("Accept" , "application/json")
//         connection.setRequestProperty("X_Api_key" , AppConstant.API_KEY)
//         connection.connectTimeout = 10000
//         connection.readTimeout = 10000
//         connection.doInput = true
//          try {
//            val  reader = InputStreamReader(connection.inputStream)
//             reader.use { input ->
//              val  response = StringBuilder()
//              val bufferedReader = BufferedReader(input)
//
//                bufferedReader.forEachLine {
//                  response.append(it.trim())
//                 }
//
//                 Log.d(Tag , "In Success ${response.toString()}")
//
//              }
//
//          } catch (e : Exception) {
//              Log.d(Tag , "In Success ${e.localizedMessage}")
//          }
//
//      }).start()
//  }
//}