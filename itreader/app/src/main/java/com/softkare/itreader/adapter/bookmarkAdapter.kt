package com.softkare.itreader.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R
import com.softkare.itreader.backend.Libro
import com.softkare.itreader.backend.Marca
import com.softkare.itreader.backend.MyApiEndpointInterface
import com.softkare.itreader.fragments.BookVisualizer
import com.softkare.itreader.fragments.BookmarkListFragment
import com.softkare.itreader.sharedPreferences
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

class bookmarkAdapter(private val list: List<Marca>, val ctx : Context, val book : Libro, val view : View) : RecyclerView.Adapter<bookmarkViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): bookmarkViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return bookmarkViewHolder(layoutInflater.inflate(R.layout.item_bookmark, parent, false))
    }

    override fun onBindViewHolder(holder: bookmarkViewHolder, position: Int) {
        val bm = list[position]
        holder.render(bm)
        holder.buttonDelete.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl(MyApiEndpointInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            println(bm.libro)
            println(bm.nombre)
            val service = retrofit.create(MyApiEndpointInterface::class.java)
            service.deleteMarca(prefs.getUsername(), bm.libro.toInt(), bm.nombre)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.body() != null) {
                            Toast.makeText(ctx, "Marcador eliminado", Toast.LENGTH_SHORT).show()
                            val bundle = Bundle()
                            bundle.putSerializable("book", book)
                            val activity = view.context as AppCompatActivity
                            val transit = BookmarkListFragment()
                            transit.arguments = bundle
                            activity.supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, transit)
                                .addToBackStack(null)
                                .commit()

                        } else {
                            println("MARCA NO ELIMINADA")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        println("ERROR AL ELIMINAR LA MARCA")
                    }
                })
        }

        holder.itemView.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl(MyApiEndpointInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val J = JSONObject()
            J.put("pagina", bm.pagina)
            val body : RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), J.toString())
            val service = retrofit.create(MyApiEndpointInterface::class.java)
            service.updateMarcaAndroid(prefs.getUsername(), book.nombre, body).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.body() != null) {
                        val bundle = Bundle()
                        bundle.putSerializable("book", book)
                        val activity = view.context as AppCompatActivity
                        val transit = BookVisualizer()
                        transit.arguments = bundle
                        activity.supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, transit)
                            .addToBackStack(null)
                            .commit()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println("ERROR AL RECIBIR LA PÁGINA DEL LIBRO")
                }
            })
        }
    }

    override fun getItemCount(): Int = list.size

}