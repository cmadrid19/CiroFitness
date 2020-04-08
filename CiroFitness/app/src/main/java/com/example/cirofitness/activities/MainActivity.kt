package com.example.cirofitness.activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.example.cirofitness.R
import com.tapadoo.alerter.Alerter
import com.example.cirofitness.client.requestSignIn

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestSignIn("hola@gmail.com", "caracola")





    }



    fun irRegistrarse(view: View) {
        val intent = Intent(this,SignUpActivity::class.java)
        startActivity(intent)
    }

    /**
     * Alerta: aviso 'Sin conexion a internet'
     */
    fun NetworkAlert(){
        Alerter.create(this)
            .setTitle(getString(R.string.no_conectado))
            .setText(getString(R.string.sin_conexion_internet))
            .setDuration(3500)
            .setIcon(R.drawable.network_off)
            .enableSwipeToDismiss()
            .setOnClickListener(
                View.OnClickListener { startActivityForResult(Intent(Settings.ACTION_WIFI_SETTINGS), 0) })
            .show()
    }
}
