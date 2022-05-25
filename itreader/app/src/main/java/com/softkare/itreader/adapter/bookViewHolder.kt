package com.softkare.itreader.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.softkare.itreader.R
import com.softkare.itreader.backend.Documento
import com.softkare.itreader.backend.Libro

class bookViewHolder(view:View):RecyclerView.ViewHolder(view) {


    val nameBook = view.findViewById<TextView>(R.id.titulo)
    val autor = view.findViewById<TextView>(R.id.autor)
    val editorial = view.findViewById<TextView>(R.id.editorial)
    val photo = view.findViewById<ImageView>(R.id.imageViewBook)

    fun render(BookModel: Libro){
        nameBook.text = BookModel.nombre
        autor.text = BookModel.autor
        editorial.text = BookModel.editorial
        Glide.with(photo.context).load(BookModel.linkPortada).into(photo)
    }
}