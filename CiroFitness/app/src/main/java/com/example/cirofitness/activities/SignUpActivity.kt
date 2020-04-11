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


private const val TAG: String = "SignUpActivity"

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


    }

    /**
     * Comprueba que el nombre este formado solo por letras.
     */

    private fun httpSend(user: User) {
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
                        this@SignUpActivity.runOnUiThread(Runnable {
                            Toast.makeText(this@SignUpActivity, resp, Toast.LENGTH_SHORT).show()
                        })
                        Log.d("TAG", resp);
                    }
                }
            })
        }).start()
    }

    fun enviar(view: View) {
        val etUsername: EditText = findViewById(R.id.cajaUser)
        val etEmail: EditText = findViewById(R.id.cajaEmail)
        val etPass: EditText = findViewById(R.id.cajaPassword1)
        val etFechaNacimiento: EditText = findViewById(R.id.cajaFechaNacimiento)
        val rgSexo: RadioGroup = findViewById(R.id.cajaSexo)

        val radioButton = findViewById<View>(rgSexo.getCheckedRadioButtonId()) as RadioButton
        var sexo = ""

        if (radioButton.id == R.id.H) {
            sexo = "H"
        } else if (radioButton.id == R.id.M) {
            sexo = "M"
        }

        val strDate = etFechaNacimiento.text.toString()

        if (validarEmail() &&
            validarPass() &&
            validarNombre() &&
            validarSexo()
        ) {
            //Enviar datos al servidor
            try {
                val fechaNacimiento: Date =
                    SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(strDate)
                val user: User = User(
                    etUsername.text.toString(),
                    etPass.text.toString(),
                    etEmail.text.toString(),
                    fechaNacimiento,
                    sexo
                )
                Log.d("TAG", "llega hasta aqui1")
                httpSend(user)
            } catch (e: ParseException) {
                println("Exepcion al intentar parsear: " + e.message)
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
        if (datosCorrectos == false) {
            nombre.setError(getString(R.string.solo_letras))
        } else {
            Log.d(TAG, "Sexo validado correctamente")
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

        if (validarLongitudPass(pass1Box) && validarLongitudPass(pass2Box)) {
            if (pass_1.equals(pass_2)) {
                correcto = true
                Log.d(TAG, "Contraseñas  validadas correctamente")
            } else {
                pass2Box.setError(getString(R.string.contrasenha_no_igual))
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
            emailBox.setError(getString(R.string.campo_obligatorio))
        } else {
            val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(email)
            if (matcher.matches()) {
                emailCheck = true
                Log.d(TAG, "Email validado correctamente")
            } else {
                emailBox.setError(getString(R.string.email_invalido))
            }
        }
        return emailCheck

    }

    private fun validarLongitudPass(edText: EditText): Boolean {
        var correcta: Boolean = false
        val edText: EditText = findViewById(R.id.cajaPassword1)
        val pass_1: String = edText.text.toString().trim()

        if (pass_1.length >= 8) {
            correcta = true
            Log.d(TAG, "Longitud de Pass validada correctamente")
        } else {
            edText.setError(getString(R.string.contrasenha_corta))
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
            Log.d(TAG, "Sexo validado correctamente")
        } else {
            Toast.makeText(applicationContext, "Debes escoger un sexo", Toast.LENGTH_SHORT).show()
        }
        return elegido!!

    }

    private fun validarFechaNacimiento(): Boolean {
        var fechaEscogida: Boolean = false
        val edFechaNacimiento: EditText = findViewById(R.id.cajaFechaNacimiento)

        val fecha = edFechaNacimiento.getText().toString()
        if (!fecha.matches("".toRegex())) {
            fechaEscogida = true
            Log.d(TAG, "FechaNacimiento validado correctamente")
        } else {
            Toast.makeText(
                applicationContext,
                "Debes introducir la fecha de nacimiento",
                Toast.LENGTH_SHORT
            ).show()
        }
        return fechaEscogida
    }


}
