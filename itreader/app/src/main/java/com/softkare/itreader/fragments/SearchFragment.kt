package com.softkare.itreader.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R
import com.softkare.itreader.adapter.bookAdapter
import com.softkare.itreader.backend.Libro
import com.softkare.itreader.backend.MyApiEndpointInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchFragment : Fragment() {
    lateinit var list : List<Libro>
    lateinit var sublist : MutableList<Libro>
    lateinit var mCtx : Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val searchView : SearchView = view.findViewById(R.id.searchview)
        val recyclerView : RecyclerView = view.findViewById(R.id.recyclerSearch)
        recyclerView.layoutManager = LinearLayoutManager(context)
        getList(recyclerView)
        list = listOf()
        sublist = mutableListOf()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchView.clearFocus()
                sublist.clear()
                for (b in list) {
                    if (b.autor.toLowerCase().contains(p0.toString().toLowerCase())) {
                        sublist.add(b)
                    }
                    recyclerView.adapter = bookAdapter(sublist)
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                searchView.clearFocus()
                sublist.clear()
                for (b in list) {
                    if (b.autor.toLowerCase().contains(p0.toString().toLowerCase())) {
                        sublist.add(b)
                    }
                    recyclerView.adapter = bookAdapter(sublist)
                }
                return false
            }
        })

        return view
    }

    private fun getList(recyclerView : RecyclerView) {
        val retrofit = Retrofit.Builder()
            .baseUrl(MyApiEndpointInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(MyApiEndpointInterface::class.java)
        service.libroList().enqueue(object : Callback<List<Libro>> {
            override fun onResponse(call: Call<List<Libro>>, response: Response<List<Libro>>) {
                if(response.body() != null){
                    list = response.body()!!
                    recyclerView.adapter = bookAdapter(list)
                }
            }

            override fun onFailure(call: Call<List<Libro>>, t: Throwable) {
                println("ERROR AL RECIBIR EL CATALOGO")
            }
        })
    }
}