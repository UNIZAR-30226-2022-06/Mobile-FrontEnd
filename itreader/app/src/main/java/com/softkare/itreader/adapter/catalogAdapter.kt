package com.softkare.itreader.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R
import com.softkare.itreader.admin.AdminActivity
import com.softkare.itreader.backend.Libro
import com.softkare.itreader.backend.MyApiEndpointInterface
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class catalogAdapter(private val bookCatalog: List<Libro>, val mCtx : Context) : RecyclerView.Adapter<catalogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): catalogViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return catalogViewHolder(layoutInflater.inflate(R.layout.item_book_admin, parent, false))
    }

    override fun onBindViewHolder(holder: catalogViewHolder, position: Int) {
        val item = bookCatalog[position]
        holder.render(item)
        holder.delete.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v : View){
                val retrofit = Retrofit.Builder()
                    .baseUrl(MyApiEndpointInterface.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val service = retrofit.create(MyApiEndpointInterface::class.java)
                println(item.nombre)
                service.deleteLibro(item.nombre).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        val activity = v.context as AppCompatActivity
                        activity.recreate()
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        println("ERROR AL ELIMINAR LIBRO DEL CATÁLOGO")
                    }
                })
            }
        })
    }

    override fun getItemCount(): Int = bookCatalog.size

}