package com.softkare.itreader.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R
import com.softkare.itreader.adapter.bookAdapter
import com.softkare.itreader.backend.Libro
import com.softkare.itreader.backend.ListaLibros
import com.softkare.itreader.backend.MyApiEndpointInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CatalogFragment : Fragment() {
    lateinit var list : List<Libro>
    lateinit var list2 : ListaLibros
    var sublist : MutableList<Libro> = mutableListOf()
    var isLoading = false
    var limit = 5
    lateinit var adapter: bookAdapter
    lateinit var layoutManager : LinearLayoutManager


    lateinit var contexto : Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_catalog,container,false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerBooks)
        val pb = view.findViewById<Button>(R.id.progress_bar)
        list = listOf()
        sublist = mutableListOf()
        var page = 1
        contexto = requireContext()
        recyclerView.layoutManager = LinearLayoutManager(context)
        conseguir_lista(recyclerView,pb,view,page)
        pb.setOnClickListener{
            page++
            conseguir_lista(recyclerView,pb,view,page)
        }
        return view
    }



    private fun conseguir_lista(recyclerView: RecyclerView, pb: Button, view: View, page: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl(MyApiEndpointInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(MyApiEndpointInterface::class.java)
        service.libroList(page).enqueue(object : Callback<ListaLibros> {
            override fun onResponse(call: Call<ListaLibros>, response: Response<ListaLibros>) {
                if (response.body() != null) {
                    list2 = response.body()!!
                    sublist.addAll(list2.results)
                    list = sublist
                    recyclerView.adapter = bookAdapter(list)
                    pb.setVisibility(View.VISIBLE);
                } else {
                    pb.setVisibility(View.GONE);
                }
            }

            override fun onFailure(call: Call<ListaLibros>, t: Throwable) {
                println("FALLO REGISTRO")
            }
        })

    }
}