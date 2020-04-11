package com.example.cirofitness.activities

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cirofitness.R
import com.example.cirofitness.constants.IP
import com.example.cirofitness.constants.LOGIN
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.tapadoo.alerter.Alerter
import okhttp3.*
import java.io.IOException
import java.net.URL


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun irRegistrarse(view: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    fun NetworkAlert() {
        Alerter.create(this)
            .setTitle(getString(R.string.no_conectado))
            .setText(getString(R.string.sin_conexion_internet))
            .setDuration(3500)
            .setIcon(R.drawable.network_off)
            .enableSwipeToDismiss()
            .setOnClickListener(
                View.OnClickListener {
                    startActivityForResult(
                        Intent(Settings.ACTION_WIFI_SETTINGS),
                        0
                    )
                })
            .show()
    }

    fun entrar(view: View) {
        val inEmail: TextInputEditText = findViewById(R.id.cajaEmail)
        val inPassword: TextInputEditText = findViewById(R.id.cajaPassword)
        val url = URL(IP + LOGIN)

        Thread(Runnable {
            val formBody = FormBody.Builder()
                .add("email", inEmail.text.toString())
                .add("passwd", inPassword.text.toString())
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
                        Toast.makeText(this@MainActivity, "Código de error: " + response.code, Toast.LENGTH_LONG)
                            .show()
                    } else {
                        val resp = response.body?.string()

                        val gson = Gson()
                        val rootElem : JsonElement = gson.toJsonTree(resp)
                        val rootObj : JsonObject = rootElem.asJsonObject

                        if(rootObj["status"].asBoolean){
                            Toast.makeText(this@MainActivity, rootObj["msg"].asString, Toast.LENGTH_LONG)
                                .show()
                        }else{
                            Toast.makeText(this@MainActivity, rootObj["msg"].asString, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            })
        }).start()
    }

    fun passForgotten(view: View) {
        //Intent nuevo activity

    }
}
