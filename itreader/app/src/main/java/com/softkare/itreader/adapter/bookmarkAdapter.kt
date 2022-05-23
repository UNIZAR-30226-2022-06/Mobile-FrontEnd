package com.softkare.itreader.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R

class bookmarkAdapter(private val list: List<String>, val ctx : Context) : RecyclerView.Adapter<bookmarkViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): bookmarkViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return bookmarkViewHolder(layoutInflater.inflate(R.layout.item_bookmark, parent, false))
    }

    override fun onBindViewHolder(holder: bookmarkViewHolder, position: Int) {
        val bm = list[position]
        holder.render(bm)
        holder.buttonDelete.setOnClickListener {
            //TODO: Llamar al servicio de eliminar bookmark
            Toast.makeText(ctx, "Marcador eliminado", Toast.LENGTH_SHORT)
        }

        holder.itemView.setOnClickListener {
            //TODO: Saltar a la página correspondiente
        }
    }

    override fun getItemCount(): Int = list.size

}