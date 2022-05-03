package com.softkare.itreader.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.softkare.itreader.R
import org.w3c.dom.Text

class BookPageFragment : Fragment() {
    lateinit var book : Book
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lateinit var view: View
        val bundle : Bundle? = this.arguments
        val book = bundle?.getSerializable("book") as Book

        /*
            Si ya está en la biblioteca:
                ----> view = inflater.inflate(R.layout.fragment_user_page_in_library, container, false)
                val buttonDelete = view.findViewById<Button>(R.id.buttonDeleteLibrary)
                buttonDelete.setOnClickListener() {
                // Falta servicio para eliminar de la biblioteca
            }

            Si no está en la biblioteca:
                ----> view = inflater.inflate(R.layout.fragment_user_page, containter, false)
                val buttonAdd = view.findViewById<Button>(R.id.buttonAddLibrary)
         */

        // Valores que se deben rellenar con los datos del libro

        val bookTitle : TextView = view.findViewById(R.id.book_page_title)
        val bookImage : ImageView = view.findViewById(R.id.book_page_image)
        val title : TextView = view.findViewById(R.id.title)
        val author : TextView = view.findViewById(R.id.autor)
        val editorial : TextView = view.findViewById(R.id.editorial)
        val rating : RatingBar = view.findViewById(R.id.rating)



        return view
    }
}