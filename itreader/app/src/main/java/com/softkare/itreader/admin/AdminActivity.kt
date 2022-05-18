package com.softkare.itreader.admin

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R
import com.softkare.itreader.adapter.catalogAdapter
import com.softkare.itreader.backend.Libro
import com.softkare.itreader.backend.MyApiEndpointInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AdminActivity : AppCompatActivity() {
    lateinit var list : List<Libro>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        val buttonUpload : ImageView = findViewById(R.id.admin_btnUpload)
        val mRecyclerView : RecyclerView = findViewById(R.id.admin_recyclerCatalog)

        getCatalog(mRecyclerView)
        registerForContextMenu(mRecyclerView)

        buttonUpload.setOnClickListener {
            //TODO: Añadir funcionalidad para subir un nuevo libro al catálogo
        }

    }

    private fun getCatalog(recyclerView: RecyclerView) {
        val retrofit = Retrofit.Builder()
            .baseUrl(MyApiEndpointInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(MyApiEndpointInterface::class.java)
        service.libroList().enqueue(object : Callback<List<Libro>> {
            override fun onResponse(call: Call<List<Libro>>, response: Response<List<Libro>>) {
                if(response.body() != null){
                    list = response.body()!!
                    recyclerView.adapter = catalogAdapter(list)
                }
            }

            override fun onFailure(call: Call<List<Libro>>, t: Throwable) {
                println("ERROR AL RECIBIR EL CATÁLOGO DE LIBROS")
            }
        })
    }
}