package com.softkare.itreader.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R
import com.softkare.itreader.backend.Documento
import com.softkare.itreader.backend.Libro
import com.softkare.itreader.backend.MyApiEndpointInterface
import com.softkare.itreader.backend.Usuario
import com.softkare.itreader.fragments.BookPageFragment
import com.softkare.itreader.fragments.BookPageInLibraryFragment
import com.softkare.itreader.sharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class bookAdapter(private val bookList: List<Libro>) : RecyclerView.Adapter<bookViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): bookViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return bookViewHolder(layoutInflater.inflate(R.layout.item_book, parent, false))
    }

    override fun onBindViewHolder(holder: bookViewHolder, position: Int) {
        val item = bookList[position]
        holder.render(item)
        holder.itemView.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v : View) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(MyApiEndpointInterface.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val service = retrofit.create(MyApiEndpointInterface::class.java)
                service.getUser(sharedPreferences.prefs.getUsername()).enqueue(object :
                    Callback<Usuario> {
                    override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                        val user : Usuario = response.body()!!
                        val check = user.docsAnyadidos.find { it.nombre == item.nombre }
                        if (check != null) goBookPageInLibrary(v, item)
                        else goBookPage(v, item)
                    }

                    override fun onFailure(call: Call<Usuario>, t: Throwable) {
                        println("ERROR AL RECUPERAR EL USUARIO")
                    }
                })
            }
        })
    }

    override fun getItemCount() : Int = bookList.size

    private fun goBookPage(v : View, item : Libro) {
        val bundle = Bundle()
        bundle.putSerializable("book", item)
        val activity = v.context as AppCompatActivity
        val transit = BookPageFragment()
        transit.arguments = bundle
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, transit)
            .addToBackStack(null)
            .commit()
    }

    private fun goBookPageInLibrary(v : View, item : Libro) {
        val bundle = Bundle()
        bundle.putSerializable("book", item)
        val activity = v.context as AppCompatActivity
        val transit = BookPageInLibraryFragment()
        transit.arguments = bundle
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, transit)
            .addToBackStack(null)
            .commit()
    }

}