package com.softkare.itreader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.softkare.itreader.backend.MyApiEndpointInterface
import com.softkare.itreader.backend.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)
        setup()
    }

    private fun setup() {

        val goLog: Button = findViewById(R.id.btnLogRegister)
        val name = findViewById<TextInputEditText>(R.id.nombre_usuario)
        val email = findViewById<TextInputEditText>(R.id.email_usuario)
        val username = findViewById<TextInputEditText>(R.id.username_ususario)
        val password = findViewById<TextInputEditText>(R.id.password_usuario)
        val validar: Button = findViewById(R.id.btnValUsuario)

        var t = Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT)

        validar.setOnClickListener {
            if(email.text?.isEmpty() == true || username.text?.isEmpty() == true || password.text?.isEmpty() == true || name.text?.isEmpty() == true) {
                t.cancel()
                t = Toast.makeText(this, "Invalid Data", Toast.LENGTH_SHORT)
                t.show()
            }else if(!checkPass(password.text.toString())) {
                showAlertPassword()
            }else if(!checkEmail(email.text.toString())) {
                showAlertEmail()
            }else{
                t.cancel()
                comprobarRegistro(name.text.toString(), username.text.toString(), password.text.toString(), email.text.toString()  )
            }
        }

        goLog.setOnClickListener {
            onBackPressed()
        }

    }

    private fun comprobarRegistro(
        name: String,
        username: String,
        password: String,
        email: String
    ) {
        // Trailing slash is needed
        val retrofit = Retrofit.Builder()
            .baseUrl(MyApiEndpointInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var u : Usuario?
        val service = retrofit.create(MyApiEndpointInterface::class.java)
        val user2 = Usuario(name,username,password,email,false)
        service.createUser(user2).enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                if(response.body() != null){
                    u = response.body()
                    showToast(u!!.nomUsuario)
                    onBackPressed()
                }else{
                    showAlert()
                }

            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                println("FALLO REGISTRO")
                println("AQUI ESTOY : USUARIO REPETIDO2 EN FALLO")
            }
        })
    }

    private fun showToast(username: String) {
        Toast.makeText(this, concatenaStrings(username) , Toast.LENGTH_SHORT).show()
    }

    private fun concatenaStrings(username: String):String {
        val builder = StringBuilder()
        builder.append(username)
            .append(" ")
            .append("has been registered")
        return builder.toString()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right)
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Sign Up Error")
        builder.setMessage("This user or email is already registered")
        builder.setPositiveButton("Ok",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun checkPass(password: String): Boolean {
        val passwordREGEX = Pattern.compile("^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                ".{8,}" +               //at least 8 characters
                "$");
        println(password)
        var a = passwordREGEX.matcher(password).matches()
        println(a)
        return passwordREGEX.matcher(password).matches()
    }

    private fun checkEmail(email: String): Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private fun showAlertPassword(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Password Error")
        builder.setMessage("Password correct format: 8 characters with at least 1 uppercase and lowercase and 1 number")
        builder.setPositiveButton("Ok",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertEmail(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Email Error")
        builder.setMessage("This format email is incorrect")
        builder.setPositiveButton("Ok",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}