package com.softkare.itreader.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.softkare.itreader.R
import com.softkare.itreader.backend.Documento
import com.softkare.itreader.backend.Libro
import com.softkare.itreader.backend.MyApiEndpointInterface
import com.softkare.itreader.backend.Usuario
import com.softkare.itreader.sharedPreferences.Companion.prefs
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookPageFragment : Fragment() {
    lateinit var user : Usuario
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lateinit var view: View
        val bundle : Bundle? = this.arguments
        val book : Libro = bundle?.getSerializable("book") as Libro

        val retrofit = Retrofit.Builder()
            .baseUrl(MyApiEndpointInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(MyApiEndpointInterface::class.java)

        //  NO NOS SIRVE PORQUE NO TRAE LA LISTA DE AÑADIDOS DEL USUARIO
        service.getUser(prefs.getUsername()).enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                user = response.body()!!
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                println("ERROR AL RECUPERAR EL USUARIO")
            }
        })

        if (user.docsAnyadidos.contains(book)) {
            view = inflater.inflate(R.layout.fragment_book_page_in_library, container, false)
        } else {
            view = inflater.inflate(R.layout.fragment_book_page, container, false)
        }

        val buttonAdd = view.findViewById<Button>(R.id.buttonAddLibrary)
        val buttonDelete = view.findViewById<Button>(R.id.buttonDeleteLibrary)
        val buttonRead = view.findViewById<Button>(R.id.buttonRead)

        buttonAdd.setOnClickListener {
            val J = JSONObject()
            J.put("nomLibro", book.nombre)
            val body : RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), J.toString())
            service.addDocsUser(prefs.getUsername(), body).enqueue(object : Callback<Usuario>{
                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                    val t = Toast.makeText(activity, "Book added to library", Toast.LENGTH_SHORT)
                    t.show()
                    val activity = view.context as AppCompatActivity
                    val transit = LibraryFragment()
                    transit.arguments = bundle
                    activity.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, transit)
                        .addToBackStack(null)
                        .commit()
                }

                override fun onFailure(call: Call<Usuario>, t: Throwable) {
                    showAlertAdd()
                }
            })
        }

        buttonDelete.setOnClickListener {
            service.deleteDocUser(prefs.getUsername(), book.nombre).enqueue(object : Callback<Usuario>{
                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                    val t = Toast.makeText(activity, "Book removed from library", Toast.LENGTH_SHORT)
                    t.show()
                    val activity = view.context as AppCompatActivity
                    val transit = LibraryFragment()
                    transit.arguments = bundle
                    activity.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, transit)
                        .addToBackStack(null)
                        .commit()
                }

                override fun onFailure(call: Call<Usuario>, t: Throwable) {
                    showAlertDelete()
                }
            })
        }

        buttonRead.setOnClickListener {
            //TODO: Saltar a la pantalla de visualización del libro
        }

        val bookTitle : TextView = view.findViewById(R.id.book_page_title)
        val bookImage : ImageView = view.findViewById(R.id.book_page_image)
        val author : TextView = view.findViewById(R.id.autor)
        val editorial : TextView = view.findViewById(R.id.editorial)
        val rating : RatingBar = view.findViewById(R.id.rating)

        Glide.with(bookImage.context).load(book.linkPortada).into(bookImage)
        bookTitle.setText(book.nombre)
        author.setText(book.autor)
        editorial.setText("Edelvives")
        rating.rating = 3.5F

        return view
    }

    private fun showAlertDelete(){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Error")
        builder.setMessage("Could not remove book from library")
        builder.setPositiveButton("Ok",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertAdd(){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Error")
        builder.setMessage("Could not add book from library")
        builder.setPositiveButton("Ok",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}