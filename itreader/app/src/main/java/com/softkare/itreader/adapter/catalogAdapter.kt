package com.softkare.itreader.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R
import com.softkare.itreader.backend.Libro
import com.softkare.itreader.sharedPreferences

class catalogAdapter(private val bookCatalog: List<Libro>, val mCtx : Context) : RecyclerView.Adapter<catalogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): catalogViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return catalogViewHolder(layoutInflater.inflate(R.layout.item_book_admin, parent, false))
    }

    override fun onBindViewHolder(holder: catalogViewHolder, position: Int) {
        val item = bookCatalog[position]
        holder.render(item)
        holder.edit.setOnClickListener {
            //TODO: Funcionalidad de editar un libro del catálogo
            Toast.makeText(mCtx, "Función por implementar", Toast.LENGTH_SHORT).show()
        }
        holder.delete.setOnClickListener {
            //TODO: Llamar al servicio de eliminar libro del catálogo
        }
    }

    override fun getItemCount(): Int = bookCatalog.size
}