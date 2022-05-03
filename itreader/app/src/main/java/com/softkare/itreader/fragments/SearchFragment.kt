package com.softkare.itreader.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R
import com.softkare.itreader.adapter.bookAdapter

class SearchFragment : Fragment() {
    lateinit var list : MutableList<Book>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val searchView = view.findViewById<SearchView>(R.id.searchview)
        val recyclerView : RecyclerView = view.findViewById(R.id.recyclerSearch)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = bookAdapter(BookProvider.bookList)
        list = mutableListOf()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchView.clearFocus()
                list.clear()
                for (b in list) {
                    // Se busca por el autor del libro
                    if (b.author.toLowerCase().contains(p0.toString().toLowerCase())) {
                        list.add(b)
                    }
                    recyclerView.adapter = bookAdapter(list)
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                searchView.clearFocus()
                list.clear()
                for (b in list) {
                    if (b.author.toLowerCase().contains(p0.toString().toLowerCase())) {
                        list.add(b)
                    }
                    recyclerView.adapter = bookAdapter(list)
                }
                return false
            }
        })

        return view
    }



}