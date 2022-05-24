package com.softkare.itreader.adapter

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R
import com.softkare.itreader.backend.Marca

class bookmarkViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    val name = view.findViewById<TextView>(R.id.item_name)
    val pageNumber = view.findViewById<TextView>(R.id.item_pagenumber)
    val buttonDelete = view.findViewById<ImageButton>(R.id.btnDeleteBookmark)

    fun render(bookmark : Marca) {
        name.text = bookmark.nombre
        pageNumber.text = bookmark.pagina.toString()
    }
}