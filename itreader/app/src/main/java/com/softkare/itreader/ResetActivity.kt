package com.softkare.itreader

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText


class ResetActivity : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reset_activity)
        setup()
    }

    private fun setup(){

        val email = findViewById<TextInputEditText>(R.id.email_reset)
        val button:Button = findViewById(R.id.button_reset)

        var t = Toast.makeText(this, R.string.toast_not_user, Toast.LENGTH_SHORT)


    }

    private fun goLogin(){
        val pantallaLogin = Intent(this, LoginActivity::class.java)
        startActivity(pantallaLogin)
    }


    private fun concatenaStrings(email: String):String {
        val builder = StringBuilder()
        builder.append(R.string.builder_sent) /////////////////////IMPORTANTE
            .append(" ")
            .append(email)
        return builder.toString()
    }



}