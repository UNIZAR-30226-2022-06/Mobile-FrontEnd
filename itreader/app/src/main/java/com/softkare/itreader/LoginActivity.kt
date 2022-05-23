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
import com.softkare.itreader.sharedPreferences.Companion.prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(2000)
        setTheme(R.style.Theme_Itreader)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        setup()
    }

    private fun setup(){

        val goReg:Button = findViewById(R.id.btnRegLogin)
        val goRes:Button = findViewById(R.id.btnRes)
        val goHome:Button = findViewById(R.id.button)
        val username = findViewById<TextInputEditText>(R.id.username_login)
        val password = findViewById<TextInputEditText>(R.id.password_login)

        var t = Toast.makeText(this, getString(R.string.empty_field), Toast.LENGTH_SHORT)

        goReg.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left)
        }

        goRes.setOnClickListener {
            startActivity(Intent(this, ResetActivity::class.java))
        }

        goHome.setOnClickListener {
            if(username.text.toString().isEmpty() || password.text.toString().isEmpty()){
                t.cancel()
                t = Toast.makeText(this, getString(R.string.empty_field), Toast.LENGTH_SHORT)
                t.show()
            }else{
                t.cancel()
                prefs.saveUsername(username.text.toString())
                comprobar(username.text.toString() , password.text.toString())
            }
        }
    }

    private fun comprobar(username: String , password: String){

        val retrofit = Retrofit.Builder()
            .baseUrl(MyApiEndpointInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var u: Usuario?
        val service = retrofit.create(MyApiEndpointInterface::class.java)


        service.getUser(username).enqueue(object : Callback<Usuario> {

            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {

                if(response.body() != null){
                    u = response.body()
                    if(u!!.password.toString() == password){
                        prefs.saveEmail(u!!.correo)
                        prefs.saveName(u!!.nombre)
                        prefs.saveUsername(u!!.nomUsuario)
                        prefs.savePass(u!!.password)
                        showGeneral()
                        finish()
                    }else{
                        showAlert()
                    }
                }else{
                    showAlert2()
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                showAlert2()
            }

        })

    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.login_error)
        builder.setMessage(getString(R.string.error_password))
        builder.setPositiveButton("Ok",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlert2(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.login_error)
        builder.setMessage(getString(R.string.error_user))
        builder.setPositiveButton("Ok",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showGeneral() {
        val pantallaGeneral = Intent(this, HomeActivity::class.java)
        startActivity(pantallaGeneral)
    }


}

