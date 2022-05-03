package com.softkare.itreader

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.softkare.itreader.backend.MyApiEndpointInterface
import com.softkare.itreader.backend.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ResetActivity : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reset_activity)
        setup()
    }

    private fun setup(){

        val email = findViewById<TextInputEditText>(R.id.email_reset)
        val button:Button = findViewById(R.id.button_reset)

        var t = Toast.makeText(this, "Empty email", Toast.LENGTH_SHORT)

        button.setOnClickListener{
            if(email.text.toString().isEmpty()){
                t.show()
            }else{
                t.cancel()
                mandarEmail(email.text.toString());
            }
        }

    }

    private fun mandarEmail(email:String){

        // Trailing slash is needed
        val retrofit = Retrofit.Builder()
            .baseUrl(MyApiEndpointInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(MyApiEndpointInterface::class.java)
        service.checkUser(email).enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                if(response.body() != null){
                    service.enviarCorreo(email).enqueue(object : Callback<Usuario>{
                        override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                            if(response.body() != null){
                                println(email)
                                println("CORREO ENVIADO")
                            }else{
                                println(email)
                                println("CORREO NO ENVIADO 4")
                            }
                        }
                        override fun onFailure(call: Call<Usuario>, t: Throwable) {
                            println("POSIBLE FALLO")
                        }
                    })
                    showToast(email)
                    goLogin()
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

    private fun goLogin(){
        val pantallaLogin = Intent(this, LoginActivity::class.java)
        startActivity(pantallaLogin)
    }

    private fun showToast(email: String) {
        Toast.makeText(this, concatenaStrings(email) , Toast.LENGTH_LONG).show()
    }


    private fun concatenaStrings(email: String):String {
        val builder = StringBuilder()
        builder.append("The password has been sent to") /////////////////////IMPORTANTE
            .append(" ")
            .append(email)
        return builder.toString()
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Reset Password Error")
        builder.setMessage("This email is not registered")
        builder.setPositiveButton("Ok",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }



}