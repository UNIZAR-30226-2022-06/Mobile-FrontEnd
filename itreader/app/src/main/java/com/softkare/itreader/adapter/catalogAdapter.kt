package com.softkare.itreader.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R
import com.softkare.itreader.backend.Documento
import com.softkare.itreader.backend.Libro
import com.softkare.itreader.fragments.BookPageFragment
import kotlin.properties.Delegates

class catalogAdapter(private val bookCatalog: List<Libro>) : RecyclerView.Adapter<catalogViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): catalogViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return catalogViewHolder(layoutInflater.inflate(R.layout.item_book, parent, false))
    }

    override fun onBindViewHolder(holder: catalogViewHolder, position: Int) {
        val item = bookCatalog[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return bookCatalog.size
    }

}