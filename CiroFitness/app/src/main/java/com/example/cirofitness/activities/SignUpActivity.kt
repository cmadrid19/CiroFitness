package com.example.cirofitness.activities

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cirofitness.R
import com.example.cirofitness.constants.IP
import com.example.cirofitness.constants.SIGN_UP
import com.example.cirofitness.data.model.User
import com.example.cirofitness.util.DatePickerFragment
import okhttp3.*
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


    }

    /**
     * Comprueba que el nombre este formado solo por letras.
     */

    private fun httpSend(user: User){
        val formBody = FormBody.Builder()
            .add("email", user.email)
            .add("passwd", user.password)
            .add("username", user.username)
            .add("sexo", user.sexo)
            .add("fechaNacimiento", user.fechaNacimiento.toString())
            .build()

        Thread(Runnable {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(IP + SIGN_UP)
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
        })
    }

    private fun enviar(view: View) {

        val etUsername: EditText = findViewById(R.id.cajaUser)
        val etEmail: EditText = findViewById(R.id.cajaEmail)
        val etPass: EditText = findViewById(R.id.cajaPassword1)
        val etFechaNacimiento: EditText = findViewById(R.id.cajaFechaNacimiento)
        val rgSexo: RadioGroup = findViewById(R.id.cajaSexo)

        val radioButton = findViewById<View>(rgSexo.getCheckedRadioButtonId()) as RadioButton
        var sexo = ""

        if(radioButton.id == R.id.H){
            sexo = "H"
        }else if(radioButton.id == R.id.M){
            sexo = "M"
        }

        val strDate = etFechaNacimiento.text.toString()

        if (validarEmail() &&
            validarPass() &&
            validarNombre() &&
            validarSexo())
        {
            //Enviar datos al servidor
            try {
                Log.d("TAG", "formato ET: "+strDate)
                val fechaNacimiento:Date = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(strDate)
                Log.d("TAG", "resultado conversion string date: " + fechaNacimiento)

                val user: User = User(
                    etUsername.text.toString(),
                    etPass.text.toString(),
                    etEmail.text.toString(),
                    fechaNacimiento,
                    sexo
                )

                httpSend(user)
            } catch (e: ParseException) {
                println("ParseException occured: " + e.message)
            }
        }
    }

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
