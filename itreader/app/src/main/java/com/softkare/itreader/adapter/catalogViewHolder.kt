package com.softkare.itreader.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.softkare.itreader.R
import com.softkare.itreader.backend.Libro

class catalogViewHolder(view:View) : RecyclerView.ViewHolder(view) {

    val title = view.findViewById<TextView>(R.id.titulo)
    val autor = view.findViewById<TextView>(R.id.autor)
    val photo = view.findViewById<ImageView>(R.id.imageViewBook)
    val delete = view.findViewById<ImageView>(R.id.deleteButton)

    fun render(book: Libro){
        title.text = book.nombre
        autor.text = book.autor
        Glide.with(photo.context).load(book.linkPortada).into(photo)
    }
}