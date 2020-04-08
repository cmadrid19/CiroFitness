package com.example.cirofitness.client

import android.util.Log
import com.example.cirofitness.constants.IP
import com.example.cirofitness.constants.PORT
import com.example.cirofitness.constants.SIGN_UP
import java.net.URL
import okhttp3.OkHttpClient
import okhttp3.FormBody
import okhttp3.Request

/**
 * HTTP POST
 * Envio los datos en el body
 * Me reponde con json
 */
fun requestSignIn(email: String, pass: String) {
    val url = URL("http://"+ IP + PORT + SIGN_UP)
/*
    val connection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "POST"
    connection.connectTimeout = 150000
    connection.doOutput = true
    connection.doInput = true
   */

    val formBody = FormBody.Builder()
        .add("email", email)
        .add("passwd", pass)
        .build()

    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .post(formBody)
        .build()

    val response = client.newCall(request).execute()
    Log.d("TAG", response.body.toString())

}