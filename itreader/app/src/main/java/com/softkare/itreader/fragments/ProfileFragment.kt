package com.softkare.itreader.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.softkare.itreader.LoginActivity
import com.softkare.itreader.R
import com.softkare.itreader.backend.MyApiEndpointInterface
import com.softkare.itreader.backend.Usuario
import com.softkare.itreader.sharedPreferences.Companion.prefs
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        setup(view)
        return view
    }

    private fun setup(view: View){

        val name = view.findViewById<EditText>(R.id.editTextTextPersonName)
        val username = view.findViewById<EditText>(R.id.editTextTextPersonName2)
        val email = view.findViewById<EditText>(R.id.editTextTextPersonName3)
        val btn: Button = view.findViewById(R.id.save_changes)
        val btnEliminar: Button = view.findViewById(R.id.button4)

        name.setText(prefs.getName())
        username.setText(prefs.getUsername())
        email.setText(prefs.getEmail())

        btn.setOnClickListener{
            val MEDIA_TYPE_JSON : MediaType? = MediaType.parse("application/json; charset=utf-8")
            val retrofit = Retrofit.Builder()
                .baseUrl(MyApiEndpointInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(MyApiEndpointInterface::class.java)
            val J = JSONObject()
            J.put("nombre", name.text.toString())
            J.put("nomUsuario", username.text.toString())
            J.put("correo", email.text.toString())
            J.put("password", "tere4")
            println("json: "+J.toString())
            val body : RequestBody = RequestBody.create(MEDIA_TYPE_JSON,J.toString())
            var u : Usuario? = null
            service.updateUser("tere4",body).enqueue(object :
                Callback<Usuario> {
                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                    println("USUARIO REGISTRADO")
                    if (response.body() != null) {
                        u = response.body()!!
                        //actualizar sharedPreferences
                        println("usuario "+ u?.nombre+" id:"+u?.id)
                    }
                    //Toast.makeText(this@SignUp, "error", Toast.LENGTH_SHORT)
                }

                override fun onFailure(call: Call<Usuario>, t: Throwable) {
                    println("FALLO REGISTRO")
                }
            })
        }
        


        btnEliminar.setOnClickListener{

            val retrofit = Retrofit.Builder()
                .baseUrl(MyApiEndpointInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(MyApiEndpointInterface::class.java)

            service.deleteUser(prefs.getUsername()).enqueue(object : Callback<ResponseBody> {

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    println("USUARIO BORRADO")
                    goLogin()
                    activity?.finish()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println("FALLO BORRADO")
                }

            })

        }

    }

    private fun goLogin(){
        val pantallaLogin = Intent(context, LoginActivity::class.java)
        startActivity(pantallaLogin)
    }

}