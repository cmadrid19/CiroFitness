package com.example.cirofitness.activities

import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.cirofitness.R
import com.example.cirofitness.client.requestLogIn
import com.example.cirofitness.data.model.User
import java.util.regex.Pattern
import com.example.cirofitness.util.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.*
import java.text.ParseException


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

        val etUsername: EditText = findViewById(R.id.cajaUser)
        val etEmail: EditText = findViewById(R.id.cajaEmail)
        val etPass: EditText = findViewById(R.id.cajaPassword1)
        val etFechaNacimiento: EditText = findViewById(R.id.cajaFechaNacimiento)
        val rgSexo: RadioGroup = findViewById(R.id.cajaSexo)

        val radioButton = findViewById<View>(rgSexo.getCheckedRadioButtonId()) as RadioButton
        val sexo = radioButton.text

        val strDate = etFechaNacimiento.text.toString()



        if (validarEmail() &&
            validarPass() &&
            validarNombre() &&
            validarSexo()

        ) {
            //Enviar datos al servidor
            Log.d("TAG", "formato ET: "+strDate)
            val date:Date = SimpleDateFormat("yyyy MMM dd").parse(strDate)
            Log.d("TAG", "resultado conversion string date: "+date)

            val user: User = User(
                etUsername.text.toString(),
                etEmail.text.toString(),
                etPass.text.toString(),
                date,
                sexo.toString()
                )

            requestLogIn(user)
        }
    }


    fun escogerFecha(view: View) {
        //Para que no se abra el teclado
        try {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            showDatePickerDialog()
        } catch (e: IllegalStateException) {
            e.message
        }
    }


    private fun showDatePickerDialog() {
        val edFechaNacimiento: EditText = findViewById(R.id.cajaFechaNacimiento)
        val newFragment =
            DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                // +1 because january is zero
                val selectedDate = year.toString() + "-" + (month + 1) + "-" + day
                edFechaNacimiento.setText(selectedDate)
            })
        newFragment.show(supportFragmentManager, "datePicker")
    }

    private fun validarSexo(): Boolean {
        val radioSexo: RadioGroup = findViewById(R.id.cajaSexo)

        var elegido: Boolean? = false
        val selectedId = radioSexo.getCheckedRadioButtonId()
        if (selectedId != -1) {
            elegido = true
        } else {
            Toast.makeText(applicationContext, "Debes escoger un sexo", Toast.LENGTH_SHORT).show()
        }
        return elegido!!
    }

    private fun validarFechaNacimiento(): Boolean {
        var fechaEscogida: Boolean? = false
        val edFechaNacimiento: EditText = findViewById(R.id.cajaFechaNacimiento)

        val fecha = edFechaNacimiento.getText().toString()
        if (!fecha.matches("".toRegex())) {
            fechaEscogida = true
        } else {
            Toast.makeText(
                applicationContext,
                "Debes introducir la fecha de nacimiento",
                Toast.LENGTH_SHORT
            ).show()
        }
        return fechaEscogida!!
    }

}
