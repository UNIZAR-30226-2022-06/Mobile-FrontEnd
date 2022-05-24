package com.softkare.itreader.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
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

class SearchFragment : Fragment() {
    lateinit var list : List<Libro>
    lateinit var list2 : ListaLibros
    lateinit var sublist : MutableList<Libro>
    lateinit var sublist2 : MutableList<Libro>
    var page = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val searchView : SearchView = view.findViewById(R.id.searchview)
        val btn : Button = view.findViewById(R.id.cargar)
        val recyclerView : RecyclerView = view.findViewById(R.id.recyclerSearch)
        recyclerView.layoutManager = LinearLayoutManager(context)
        list = listOf()
        sublist = mutableListOf()
        sublist2 = mutableListOf()
        page = 1
        getList(recyclerView,btn,view)
        btn.setOnClickListener{
            page++
            getList(recyclerView, btn, view)
            searchView.clearFocus()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchView.setQuery("",false)
                searchView.clearFocus()
                sublist.clear()
                for (b in list) {
                    if (b.nombre.lowercase().contains(p0.toString().lowercase())) {
                        sublist.add(b)
                    }
                }
                recyclerView.adapter = bookAdapter(sublist)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean = false
        })

        return view
    }

    private fun getList(recyclerView: RecyclerView, btn: Button, view: View) {
        val retrofit = Retrofit.Builder()
            .baseUrl(MyApiEndpointInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(MyApiEndpointInterface::class.java)
        service.libroList(page).enqueue(object : Callback<ListaLibros> {
            override fun onResponse(call: Call<ListaLibros>, response: Response<ListaLibros>) {
                if(response.body() != null){
                    list2 = response.body()!!
                    sublist2.addAll(list2.results)
                    list = sublist2
                    recyclerView.adapter = bookAdapter(list)
                }else{
                    Toast.makeText(requireContext(), "No hay más libros que mostrar", Toast.LENGTH_SHORT).show()
                    btn.setVisibility(View.GONE);
                }
            }

            override fun onFailure(call: Call<ListaLibros>, t: Throwable) {
                println("ERROR AL RECIBIR EL CATALOGO")
            }
        })
    }
}