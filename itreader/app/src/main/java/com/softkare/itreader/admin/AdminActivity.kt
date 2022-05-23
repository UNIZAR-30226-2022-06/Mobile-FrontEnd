package com.softkare.itreader.admin

import android.os.Bundle
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R
import com.softkare.itreader.adapter.bookAdapter
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
    lateinit var sublist : MutableList<Libro>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        val buttonUpload : ImageView = findViewById(R.id.buttonUploadAdmin)
        val mRecyclerView : RecyclerView = findViewById(R.id.recyclerCatalogAdmin)
        val searchView : SearchView = findViewById(R.id.searchviewAdmin)

        getCatalog(mRecyclerView)
        list = listOf()
        sublist = mutableListOf()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchView.clearFocus()
                sublist.clear()
                for (b in list) {
                    if (b.autor.lowercase().contains(p0.toString().lowercase())) {
                        sublist.add(b)
                    }
                    mRecyclerView.adapter = catalogAdapter(sublist, this@AdminActivity)
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean = false
        })

        buttonUpload.setOnClickListener {
            //TODO: Añadir funcionalidad para subir un nuevo libro al catálogo
            Toast.makeText(this, "Función por implementar", Toast.LENGTH_SHORT).show()
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
                if(response.body() != null) {
                    list = response.body()!!
                    recyclerView.adapter = catalogAdapter(list, this@AdminActivity)
                }
            }

            override fun onFailure(call: Call<List<Libro>>, t: Throwable) {
                println("ERROR AL RECIBIR EL CATÁLOGO DE LIBROS")
            }
        })
    }
}