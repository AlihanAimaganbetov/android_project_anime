package com.example.anime_application

import java.net.HttpURLConnection
import java.net.URL

class ConnectionChecker {
    fun checkConnection(): Boolean {
        val url = URL("http://localhost:5000/api/Anime")
        return try {
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000 // Установка таймаута подключения в 5 секунд
            val responseCode = connection.responseCode
            responseCode == HttpURLConnection.HTTP_OK
        } catch (e: Exception) {
            // Обработка исключения, например, вывод сообщения об ошибке
            e.printStackTrace()
            false
        }
    }
}

fun main() {
    val connectionChecker = ConnectionChecker()
    val isConnected = connectionChecker.checkConnection()
    if (isConnected) {
        println("Соединение с http://localhost:5000/api/Anime установлено")
    } else {
        println("Не удалось установить соединение с http://localhost:5000/api/Anime")
    }
}
