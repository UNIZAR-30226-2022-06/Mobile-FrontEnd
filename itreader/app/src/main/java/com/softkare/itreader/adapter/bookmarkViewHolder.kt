package com.softkare.itreader.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R

class bookmarkViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    val name = view.findViewById<TextView>(R.id.item_name)
    val pageNumber = view.findViewById<TextView>(R.id.item_pagenumber)
    val buttonDelete = view.findViewById<ImageView>(R.id.btnDeleteBookmark)

    fun render(bookmark : String) {
        name.text = bookmark
        pageNumber.text = "Página X"
    }
}