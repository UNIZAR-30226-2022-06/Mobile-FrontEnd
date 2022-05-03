package com.softkare.itreader.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.softkare.itreader.R
import com.softkare.itreader.backend.Documento
import com.softkare.itreader.sharedPreferences.Companion.prefs

class DocViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    val name : TextView = view.findViewById(R.id.nombre_doc)
    val user : TextView = view.findViewById(R.id.usuario_doc)

    fun render(document : Documento) {
        name.text = document.nombre
        user.text = prefs.getUsername()
    }
}