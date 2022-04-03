package com.softkare.itreader.fragments

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R
import com.softkare.itreader.adapter.bookAdapter


class CatalogFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_catalog,container,false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerBooks)
        recyclerView.layoutManager=LinearLayoutManager(context)
        recyclerView.adapter = bookAdapter(BookProvider.bookList)
        return view
    }


}