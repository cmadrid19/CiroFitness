package com.example.cirofitness.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cirofitness.R
import com.example.cirofitness.constants.IP
import com.example.cirofitness.constants.LOGIN
import com.example.cirofitness.util.ConectionUtil
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.tapadoo.alerter.Alerter
import okhttp3.*
import java.io.IOException
import java.net.URL


private const val TAG = "Login"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkUsuarioFichero()

    }

    fun irRegistrarse(view: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    fun networkAlert() {
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
        if (ConectionUtil.checkConnection(this) == false) { // TODO estaría mejor tener un listener ??
            networkAlert()
            return
        }


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
                        Toast.makeText(
                            this@MainActivity,
                            "Código de error: " + response.code,
                            Toast.LENGTH_LONG
                        )
                            .show()
                    } else {
                        val resp = response.body?.string()
                        Log.d(TAG, "Valor de: "+resp)
                        val rootObj: JsonObject = JsonParser.parseString(resp).asJsonObject

                        this@MainActivity.runOnUiThread(Runnable {
                            if (rootObj["status"].asBoolean) {
                                Toast.makeText(
                                    this@MainActivity,
                                    rootObj["msg"].asString,
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    rootObj["msg"].asString,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })
                        rememberMe()
                    }
                }
            })
        }).start()
    }

    fun rememberMe() {
        val cbRemember: CheckBox = findViewById(R.id.chRememberMe)
        if (cbRemember.isChecked){
            val etEmail: EditText = findViewById(R.id.cajaEmail)
            val email = etEmail.text.toString()
            val prefs: SharedPreferences
            prefs = getSharedPreferences("RememberMe", Context.MODE_PRIVATE)
            val edit = prefs.edit()
            edit.putString("email", email)
            Log.d(
                TAG,
                "Se ha guardado el usuario: "+ email+ " para proximas sesiones."
            )
            edit.commit()
        }
    }

    open fun checkUsuarioFichero(){
        val prefs: SharedPreferences = getSharedPreferences("RememberMe", Context.MODE_PRIVATE)
        val email = prefs.getString("email", "")
        Log.d(
            TAG,
            "Email encontrado: $email"
        )

        val edText:EditText = findViewById(R.id.cajaEmail)
        if (!email.equals("")) {
            edText.setText(email)
            val cbRemember: CheckBox = findViewById(R.id.chRememberMe)
            cbRemember.isChecked = true
        }
    }

    fun passForgotten(view: View) {
        //Intent nuevo activity

    }
}
