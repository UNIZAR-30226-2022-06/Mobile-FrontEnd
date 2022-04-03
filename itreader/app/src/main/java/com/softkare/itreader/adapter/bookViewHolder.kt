package com.softkare.itreader.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.softkare.itreader.R
import com.softkare.itreader.fragments.Book

class bookViewHolder(view:View):RecyclerView.ViewHolder(view) {


    val nameBook = view.findViewById<TextView>(R.id.titulo)
    val autor = view.findViewById<TextView>(R.id.autor)
    val photo = view.findViewById<ImageView>(R.id.imageViewBook)

    fun render(BookModel: Book){
        nameBook.text = BookModel.name
        autor.text = BookModel.author
        Glide.with(photo.context).load(BookModel.photo).into(photo)

        var t = Toast.makeText(photo.context, autor.text.toString(), Toast.LENGTH_SHORT)
        var z = Toast.makeText(photo.context, nameBook.text.toString(), Toast.LENGTH_SHORT)

        photo.setOnClickListener {
            t.cancel()
            z.show()
        }

        itemView.setOnClickListener {
            z.cancel()
            t.show()
        }

    }
}