package com.example.cirofitness.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.cirofitness.R
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)



    }

    /**
     * Comprueba que el nombre este formado solo por letras.
     */
    private fun validarNombre(): Boolean {
        var datosCorrectos = true
        val nombre: EditText = findViewById(R.id.cajaUser)
        val n: String = nombre.text.toString()
        val chars: CharArray = n.toCharArray()

        chars.forEach { c ->
            if (!c.isLetter()) {
                datosCorrectos = false
                return@forEach // identico al continue en java
            }
        }

        return datosCorrectos
    }

    /**
     * Compara las contraseñas
     */
    private fun validarPass(): Boolean {
        var correcto: Boolean = false

        val pass1Box: EditText = findViewById(R.id.cajaPassword1)
        val pass2Box: EditText = findViewById(R.id.cajaPassword2)

        val pass_1: String = pass1Box.text.toString().trim()
        val pass_2: String = pass2Box.text.toString().trim()

        if (validarLongitudPass()) {
            if (pass_1.equals(pass_2)) {
                correcto = true
            }
        }

        return correcto
    }

    /**
     * Devuelve true si el fromato del email es correcto
     */
    private fun validarEmail(): Boolean {
        var emailCheck: Boolean = false

        val emailBox: EditText = findViewById(R.id.cajaEmail)
        val email: String = emailBox.text.toString().trim()

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

    private fun validarLongitudPass(): Boolean {
        var correcta: Boolean = false

        val pass1Box: EditText = findViewById(R.id.cajaPassword1)
        val pass2Box: EditText = findViewById(R.id.cajaPassword2)

        val pass_1: String = pass1Box.text.toString().trim()
        val pass_2: String = pass2Box.text.toString().trim()

        if (pass_1.length >= 8 && pass_2.length >= 8) {
            correcta = true
        }
        return correcta
    }


    fun enviar(view: View) {
        if (validarEmail() &&
            validarPass() &&
            validarNombre()
        ) {

            //Enviar datos al servidor


        }
    }


}
