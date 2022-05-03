package com.softkare.itreader.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.softkare.itreader.LoginActivity
import com.softkare.itreader.R
import com.softkare.itreader.backend.MyApiEndpointInterface
import com.softkare.itreader.backend.Usuario
import com.softkare.itreader.sharedPreferences.Companion.prefs
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern

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

        name.hint = prefs.getName()
        username.hint = prefs.getUsername()
        email.hint = prefs.getEmail()

        val newPass = view.findViewById<EditText>(R.id.editTextTextPersonName5)
        val btn: Button = view.findViewById(R.id.save_changes)
        val btnEliminar: Button = view.findViewById(R.id.button4)
        val btnSalir: Button = view.findViewById(R.id.button3)

        var nombre:String=""
        var nombreUsuario:String=""
        var correo:String = ""
        var nuevaPass:String =""
        var comprobarPass:Boolean = false
        var comprobarEmail:Boolean = false

        var t = Toast.makeText(activity, "No has rellenado ningún campo", Toast.LENGTH_SHORT)

        btn.setOnClickListener {

            if (email.text.isEmpty() && username.text.isEmpty() && newPass.text.isEmpty() && name.text.isEmpty()) {
                t.cancel()
                t = Toast.makeText(activity, "No data to change", Toast.LENGTH_SHORT)
                t.show()
            } else {
                t.cancel()

                if (name.text.isEmpty()) {
                    nombre = prefs.getName()
                } else {
                    nombre = name.text.toString()
                }

                if (username.text.isEmpty()) {
                    nombreUsuario = prefs.getUsername()
                } else {
                    nombreUsuario = username.text.toString()
                }

                if (email.text.isEmpty()) {
                    correo = prefs.getEmail()
                    comprobarEmail = true
                } else {
                    if (checkEmail(email.text.toString())) {
                        correo = email.text.toString()
                        comprobarEmail = true
                    } else {
                        comprobarEmail = false
                    }
                }

                if (newPass.text.isEmpty()) {
                    nuevaPass = prefs.getPass()
                    comprobarPass = true
                } else {
                    if (checkPass(newPass.text.toString())) {
                        nuevaPass = newPass.text.toString()
                        comprobarPass = true
                    } else {
                        comprobarPass = false
                    }
                }

                if (comprobarEmail && comprobarPass) {
                    val builder = AlertDialog.Builder(requireActivity())
                    val vista = layoutInflater.inflate(R.layout.dialogpassword, null)
                    builder.setView(vista)
                    val dialog = builder.create()
                    dialog.show()


                    val cajaPassword = vista.findViewById<EditText>(R.id.password)
                    val btnConf: Button = vista.findViewById(R.id.btnConfirmar)
                    var z = Toast.makeText(activity, "Incorrect Password", Toast.LENGTH_SHORT)

                    btnConf.setOnClickListener {
                        z.cancel()
                        if (cajaPassword.text.toString() == prefs.getPass()) {
                            println("llego hasta aqui 1")
                            guardarCambios(nombre, nombreUsuario, correo, nuevaPass)
                            dialog.hide()
                        } else {
                            //z.cancel()
                            z = Toast.makeText(activity, "Incorrect Password", Toast.LENGTH_SHORT)
                            z.show()
                        }
                    }
                } else if (!comprobarEmail) {
                    showAlert1()
                } else if (!comprobarPass) {
                    showAlert2()
                }
            }
        }

        btnEliminar.setOnClickListener{

            val builder = AlertDialog.Builder(requireActivity())
            val vista = layoutInflater.inflate(R.layout.dialogpassword, null)
            builder.setView(vista)
            val dialog = builder.create()
            dialog.show()


            val cajaPassword = vista.findViewById<EditText>(R.id.password)
            val btnConf: Button = vista.findViewById(R.id.btnConfirmar)
            var z = Toast.makeText(activity, "Incorrect Password", Toast.LENGTH_SHORT)

            btnConf.setOnClickListener {
                z.cancel()
                if (cajaPassword.text.toString() == prefs.getPass()) {
                    println("llego hasta aqui 1")
                    eliminarCuenta()
                    dialog.hide()
                } else {
                    z = Toast.makeText(activity, "Incorrect Password", Toast.LENGTH_SHORT)
                    z.show()
                }
            }

        }

        btnSalir.setOnClickListener {
            goLogin()
            activity?.finish()
        }

    }

    private fun eliminarCuenta() {

        var progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Loading")
        progressDialog.setMessage("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()

        val retrofit = Retrofit.Builder()
            .baseUrl(MyApiEndpointInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(MyApiEndpointInterface::class.java)

        service.deleteUser(prefs.getUsername()).enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                println("USUARIO BORRADO")
                progressDialog.cancel()
                goLogin2()
                activity?.finish()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressDialog.cancel()
                println("FALLO BORRADO")
            }
        })
    }

    private fun goLogin(){
        val pantallaLogin = Intent(context, LoginActivity::class.java)
        startActivity(pantallaLogin)
    }

    private fun goLogin2(){
        Toast.makeText(activity, prefs.getUsername() + " has been deleted", Toast.LENGTH_LONG).show()
        val pantallaLogin = Intent(context, LoginActivity::class.java)
        startActivity(pantallaLogin)
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

    private fun showAlert2(){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Password Error")
        builder.setMessage("Password correct format: 8 characters with at least 1 uppercase and lowercase and 1 number")
        builder.setPositiveButton("Ok",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlert1(){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Email Error")
        builder.setMessage("This format email is incorrect")
        builder.setPositiveButton("Ok",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun guardarCambios(name: String, username: String, email: String, newPass: String) {
        var progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Loading")
        progressDialog.setMessage("Wait please")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
        val MEDIA_TYPE_JSON: MediaType? = MediaType.parse("application/json; charset=utf-8")
        val retrofit = Retrofit.Builder()
            .baseUrl(MyApiEndpointInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(MyApiEndpointInterface::class.java)
        val J = JSONObject()
        J.put("nombre", name)
        J.put("username", username)
        J.put("correo", email)
        J.put("password", newPass)
        println("json: " + J.toString())
        val body: RequestBody = RequestBody.create(MEDIA_TYPE_JSON, J.toString())
        var u: Usuario
        service.updateUser(prefs.getUsername(), body).enqueue(object :
            Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                println("USUARIO REGISTRADO")
                if (response.body() != null) {
                    u = response.body()!!
                    prefs.saveEmail(u.correo)
                    prefs.saveUsername(u.nomUsuario)
                    prefs.saveName(u.nombre)
                    prefs.savePass(u.password)
                    progressDialog.cancel()
                    Toast.makeText(activity, "Changes saved", Toast.LENGTH_LONG).show()
                    goProfile()
                } else {
                    progressDialog.cancel()
                    showAlert()
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                progressDialog.cancel()
                println("FALLO REGISTRO")
            }
        })
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Error")
        builder.setMessage("This username or email already exists")
        builder.setPositiveButton("Ok",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun goProfile(){
        val profFr = ProfileFragment()
        val transicion = activity?.supportFragmentManager?.beginTransaction()
        transicion?.replace(R.id.fragment_container, profFr)
        transicion?.addToBackStack(null)
        transicion?.commit()
    }

}