package com.example.cirofitness.client

import android.util.Log
import com.example.cirofitness.constants.IP
import com.example.cirofitness.constants.SIGN_UP
import okhttp3.*
import java.net.URL
import java.io.IOException

/**
 * HTTP POST
 * Envio los datos en el body
 * Me reponde con json
 */
fun requestSignIn(email: String, pass: String) {
    val url = URL(IP + SIGN_UP)


    Thread(Runnable {
        val formBody = FormBody.Builder()
            .add("email", email)
            .add("passwd", pass)
            .build()

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback { //EVENTO DE LLAMADA FINALIZADA
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code " + response)
                } else {
                    val resp = response.body?.string();

                    Log.d("TAG", resp);
                }
            }
        })
    }).start()
}