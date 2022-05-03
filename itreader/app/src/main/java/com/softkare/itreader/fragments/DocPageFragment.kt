package com.softkare.itreader.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.softkare.itreader.R
import com.softkare.itreader.backend.Documento
import com.softkare.itreader.sharedPreferences.Companion.prefs

class DocPageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle : Bundle? = this.arguments
        val documento : Documento = bundle?.getSerializable("doc") as Documento
        val view = inflater.inflate(R.layout.fragment_document_page, container, false)

        val pageTitle : TextView = view.findViewById(R.id.doc_page_title)
        val docName : TextView = view.findViewById(R.id.doc_page_name)
        val username : TextView = view.findViewById(R.id.doc_page_username)
        pageTitle.setText(documento.nombre)
        docName.setText(documento.nombre)
        username.setText(prefs.getUsername())

        val buttonRead : Button = view.findViewById(R.id.buttonReadDoc)
        val buttonDelete : Button = view.findViewById(R.id.buttonDeleteDocLibrary)

        buttonRead.setOnClickListener() {
            //TODO: Reenviar al fragmento de visualizacion del documento
        }

        buttonDelete.setOnClickListener() {
            //TODO: Eliminar el documento del sistema
        }

        return view
    }
}