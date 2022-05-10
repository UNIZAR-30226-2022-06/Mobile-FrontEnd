package com.softkare.itreader.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R
import com.softkare.itreader.backend.Documento
import com.softkare.itreader.fragments.DocumentPageFragment

class documentAdapter(private val docList : List<Documento>) : RecyclerView.Adapter<documentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): documentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return documentViewHolder(layoutInflater.inflate(R.layout.item_doc, parent, false))
    }

    override fun onBindViewHolder(holder: documentViewHolder, position: Int) {
        val document = docList[position]
        holder.render(document)
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val bundle = Bundle()
                bundle.putSerializable("document", document)
                val activity = v.context as AppCompatActivity
                val transit = DocumentPageFragment()
                transit.arguments = bundle
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, transit)
                    .addToBackStack(null)
                    .commit()
            }
        })
    }

    override fun getItemCount(): Int = docList.size
}