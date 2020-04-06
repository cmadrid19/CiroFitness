package com.example.cirofitness.Activities

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.example.cirofitness.R
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)







    }


    /**
     * Alerta: aviso 'Sino conexion a internet'
     */
    fun NetworkAlert(){
        Alerter.create(this)
            .setTitle("No conectado")
            .setText("No hay conexion a intenet")
            .setDuration(3500)
            .setIcon(R.drawable.network_off)
            .enableSwipeToDismiss()
            .setOnClickListener(
                View.OnClickListener { startActivityForResult(Intent(Settings.ACTION_WIFI_SETTINGS), 0) })
            .show()
    }
}
