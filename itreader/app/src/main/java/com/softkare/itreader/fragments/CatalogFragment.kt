package com.softkare.itreader.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R
import com.softkare.itreader.adapter.bookAdapter
import com.softkare.itreader.backend.Documento
import com.softkare.itreader.backend.Libro
import com.softkare.itreader.backend.MyApiEndpointInterface
import com.softkare.itreader.backend.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CatalogFragment : Fragment() {
    lateinit var list:List<Libro>
    lateinit var contexto : Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_catalog,container,false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerBooks)
        contexto = requireContext()
        recyclerView.layoutManager=LinearLayoutManager(context)
        conseguir_lista(recyclerView)
        return view
    }

    private fun conseguir_lista(recyclerView: RecyclerView) {
        // Trailing slash is needed
        val retrofit = Retrofit.Builder()
            .baseUrl(MyApiEndpointInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(MyApiEndpointInterface::class.java)
        service.libroList().enqueue(object : Callback<List<Libro>> {
            override fun onResponse(call: Call<List<Libro>>, response: Response<List<Libro>>) {
                if(response.body() != null){
                    list = response.body()!!
                    for(i in list) {
                        println(i.nombre + "," + i.formato + "," + i.linkDocumento)
                    }
                    recyclerView.adapter = bookAdapter(list)
                }else{
                    showAlert()
                }
            }

            override fun onFailure(call: Call<List<Libro>>, t: Throwable) {
                println("FALLO REGISTRO")
                println("AQUI ESTOY : USUARIO REPETIDO2 EN FALLO")
            }

        })
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(contexto)
        builder.setTitle("Error")
        builder.setMessage("You don't have any books to show")
        builder.setPositiveButton("Ok",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


}