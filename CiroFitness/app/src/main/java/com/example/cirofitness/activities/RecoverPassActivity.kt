package com.example.cirofitness.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.cirofitness.R
import java.util.regex.Pattern

class RecoverPassActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover_pass)





    }

    fun reestablecerContraseña(view: View) {

        if (validarEmail()){
            //Hacer peticion


        }
    }

    /**
     * Devuelve true si el fromato del email es correcto
     */
    private fun validarEmail(): Boolean {
        val emailBox:EditText =  findViewById(R.id.edEmail)
        val email = emailBox.text.toString()
        var emailCheck: Boolean = false
        if (email.isEmpty()) {
            emailBox.setError("El campo es obligatorio.")
        } else {
            val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(email)
            if (matcher.matches()) {
                emailCheck = true
            } else {
                emailBox.setError("Email no valido.")
            }
        }
        return emailCheck

    }


}
