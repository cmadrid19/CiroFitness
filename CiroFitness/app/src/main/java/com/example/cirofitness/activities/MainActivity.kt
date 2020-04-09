package com.example.cirofitness.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.example.cirofitness.R
import com.tapadoo.alerter.Alerter
import com.example.cirofitness.client.requestSignIn
import com.example.cirofitness.constants.PASS_FORGOTTEN
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    fun irRegistrarse(view: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    /**
     * Alerta: aviso 'Sin conexion a internet'
     */
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
        requestSignIn(inEmail.text.toString(), inPassword.text.toString())

        //Si esta bien, hacer intent

    }

    fun passForgotten(view: View) {
        //Intent nuevo activity
        

    }


}
