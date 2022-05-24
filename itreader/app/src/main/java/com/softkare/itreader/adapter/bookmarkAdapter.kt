package com.softkare.itreader.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R
import com.softkare.itreader.backend.Marca
import com.softkare.itreader.backend.MyApiEndpointInterface
import com.softkare.itreader.sharedPreferences
import com.softkare.itreader.sharedPreferences.Companion.prefs
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class bookmarkAdapter(private val list: List<Marca>, val ctx : Context) : RecyclerView.Adapter<bookmarkViewHolder>() {
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
            service.deleteMarca(prefs.getUsername(),bm.libro.toInt(), bm.nombre).enqueue(object :
                Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.body() != null){
                        println("MARCA ELIMINADA")
                        Toast.makeText(ctx, "Marcador eliminado", Toast.LENGTH_SHORT).show()
                    }else{
                        println("MARCAS NO DEVUELTAS")
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println("ERROR AL LISTAR LA MARCA")
                }
            })

        }

        holder.itemView.setOnClickListener {
            //TODO: Saltar a la página correspondiente bundle null(venir de aqui) o no null
        }
    }

    override fun getItemCount(): Int = list.size

}