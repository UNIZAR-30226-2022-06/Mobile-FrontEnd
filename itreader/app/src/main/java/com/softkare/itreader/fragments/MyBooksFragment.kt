package com.softkare.itreader.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R
import com.softkare.itreader.adapter.documentAdapter
import com.softkare.itreader.backend.Documento
import com.softkare.itreader.backend.MyApiEndpointInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyBooksFragment : Fragment() {
    lateinit var list : List<Documento>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_books, container, false)
        val recyclerView : RecyclerView = view.findViewById(R.id.recyclerDocs)
        val buttonUpload : ImageButton = view.findViewById(R.id.btnUpload)

        recyclerView.layoutManager = LinearLayoutManager(context)
        getList(recyclerView)

        buttonUpload.setOnClickListener {
            //TODO: Llamar a servicio --> subirDocumento
        }

        return view
    }

    private fun getList(recyclerView: RecyclerView) {
        val retrofit = Retrofit.Builder()
            .baseUrl(MyApiEndpointInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(MyApiEndpointInterface::class.java)
        service.documentoList().enqueue(object : Callback<List<Documento>> {
            override fun onResponse(call: Call<List<Documento>>, response: Response<List<Documento>>) {
                if(response.body() != null){
                    list = response.body()!!
                    recyclerView.adapter = documentAdapter(list)
                }
            }

            override fun onFailure(call: Call<List<Documento>>, t: Throwable) {
                println("ERROR AL RECIBIR LOS DOCUMENTOS DEL USUARIO")
            }
        })
    }
}