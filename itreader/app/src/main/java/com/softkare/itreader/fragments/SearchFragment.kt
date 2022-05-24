package com.softkare.itreader.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
    lateinit var sublist : MutableList<Libro>
    var searchType = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val searchView : SearchView = view.findViewById(R.id.searchview)
        val btnFilter : ImageView = view.findViewById(R.id.btnFilter)
        val recyclerView : RecyclerView = view.findViewById(R.id.recyclerSearch)
        recyclerView.layoutManager = LinearLayoutManager(context)
        list = listOf()
        sublist = mutableListOf()
        searchType = 0
        getList(recyclerView)

        btnFilter.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), btnFilter)
            popupMenu.menuInflater.inflate(R.menu.menu_search, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.search_by_title -> {
                        searchType = 0
                        searchView.queryHint = getString(R.string.search_by_title)
                    }
                    R.id.search_by_author -> {
                        searchType = 1
                        searchView.queryHint = getString(R.string.search_by_author)
                    }

                    R.id.search_by_editorial -> {
                        searchType = 2
                        searchView.queryHint = getString(R.string.search_by_editorial)
                    }
                }
                true
            }
            popupMenu.show()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchView.setQuery("",false)
                searchView.clearFocus()
                sublist.clear()
                for (b in list) {
                    if (searchType == 0 && b.nombre.lowercase().contains(p0.toString().lowercase())) {
                        sublist.add(b)
                    } else if (searchType == 1 && b.autor.lowercase().contains(p0.toString().lowercase())) {
                        sublist.add(b)
                    } else if (searchType == 2 && b.editorial.lowercase().contains(p0.toString().lowercase())) {
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

    private fun getList(recyclerView: RecyclerView) {
        val retrofit = Retrofit.Builder()
            .baseUrl(MyApiEndpointInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(MyApiEndpointInterface::class.java)
        service.libros().enqueue(object : Callback<List<Libro>> {
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