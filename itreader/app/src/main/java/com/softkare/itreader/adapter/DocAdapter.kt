package com.softkare.itreader.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R
import com.softkare.itreader.backend.Documento
import com.softkare.itreader.fragments.DocPageFragment

class DocAdapter(private val docList : List<Documento>) : RecyclerView.Adapter<DocViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DocViewHolder(layoutInflater.inflate(R.layout.item_doc, parent, false))
    }

    override fun onBindViewHolder(holder: DocViewHolder, position: Int) {
        val document = docList[position]
        holder.render(document)
        /*
        //TODO: ENVIAR A LA PANTALLA DEL DOCUMENTO AL CLICKAR SOBRE EL
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val bundle = Bundle()
                //bundle.putSerializable("doc", document)
                val activity = v.context as AppCompatActivity
                val transit = DocPageFragment()
                transit.arguments = bundle
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, transit)
                    .addToBackStack(null)
                    .commit()
            }
        }) */
    }

    override fun getItemCount(): Int = docList.size
}