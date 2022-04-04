package com.softkare.itreader.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R
import com.softkare.itreader.backend.Documento

class bookAdapter(private val bookList: List<Documento>) : RecyclerView.Adapter<bookViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): bookViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return bookViewHolder(layoutInflater.inflate(R.layout.item_book, parent, false))
    }

    override fun onBindViewHolder(holder: bookViewHolder, position: Int) {
        val item = bookList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

}