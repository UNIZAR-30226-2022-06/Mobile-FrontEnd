package com.softkare.itreader

import android.content.Intent
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

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)
        setup()
    }

    private fun setup(){

        val goLog: Button = findViewById(R.id.btnLogRegister)
        val name = findViewById<TextInputEditText>(R.id.nombre_usuario)
        val email = findViewById<TextInputEditText>(R.id.email_usuario)
        val username = findViewById<TextInputEditText>(R.id.username_ususario)
        val password = findViewById<TextInputEditText>(R.id.password_usuario)
        val validar: Button = findViewById(R.id.btnValUsuario)

        var t = Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT)

        validar.setOnClickListener {
            if (email.text.toString().isEmpty() || username.text.toString()
                    .isEmpty() || password.text.toString().isEmpty() || name.text.toString()
                    .isEmpty()
            ) {
                t.cancel()
                t = Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT)
                t.show()
            } else {
                t.cancel()
                registro(name.text.toString(),username.text.toString(),password.text.toString(),email.text.toString())
                Toast.makeText(this, concatenaStrings(username.text.toString()) , Toast.LENGTH_SHORT).show()
            }
        }


        goLog.setOnClickListener {
            onBackPressed()
        }

    }

    private fun registro(
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
        //var u : Usuario?
        val service = retrofit.create(MyApiEndpointInterface::class.java)
        val user2 = Usuario(name,username,password,email)
        service.createUser(user2).enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                println("USUARIO REGISTRADO")
                //u = response.body()!!
                onBackPressed()
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                println("FALLO REGISTRO")
            }
        })
    }


    private fun concatenaStrings(username: String):String {
        val builder = StringBuilder()
        builder.append(username)
            .append(" ")
            .append("ha sido creado")
        return builder.toString()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right)
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Login Error")
        builder.setMessage("Invalid password")
        builder.setPositiveButton("Ok",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}