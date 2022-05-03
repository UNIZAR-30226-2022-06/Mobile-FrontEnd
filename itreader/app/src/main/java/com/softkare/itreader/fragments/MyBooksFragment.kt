package com.softkare.itreader.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R
import com.softkare.itreader.adapter.DocAdapter
import com.softkare.itreader.backend.Documento

class MyBooksFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_books, container, false)
        val recyclerView : RecyclerView = view.findViewById(R.id.recyclerDocs)
        val buttonUpload : ImageButton = view.findViewById(R.id.btnUpload)

        // Esta lista es solo para comprobar el funcionamiento del RecyclerView
        val lista : List<Documento> = listOf(Documento(1, "miLibro_1", "pdf", ""),
                                        Documento(2,"Titantes", "pdf", ""),
                                        Documento(3, "Mi diario", "pdf", ""))

        recyclerView.layoutManager= LinearLayoutManager(context)
        recyclerView.adapter = DocAdapter(lista)

        buttonUpload.setOnClickListener() {
            //TODO: Saltar a fragmento de añadir documento o mostrar opcion de añadir documento
        }

        return view
    }

}