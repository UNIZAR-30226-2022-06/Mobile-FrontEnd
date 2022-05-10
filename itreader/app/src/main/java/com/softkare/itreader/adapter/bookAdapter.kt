package com.softkare.itreader.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R
import com.softkare.itreader.backend.Documento
import com.softkare.itreader.backend.Libro
import com.softkare.itreader.fragments.BookPageFragment

class bookAdapter(private val bookList: List<Libro>) : RecyclerView.Adapter<bookViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): bookViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return bookViewHolder(layoutInflater.inflate(R.layout.item_book, parent, false))
    }

    override fun onBindViewHolder(holder: bookViewHolder, position: Int) {
        val item = bookList[position]
        holder.render(item)
        holder.itemView.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v : View) {
                val bundle = Bundle()
                bundle.putSerializable("book", item)
                val activity = v.context as AppCompatActivity
                val transit = BookPageFragment()
                transit.arguments = bundle
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, transit)
                    .addToBackStack(null)
                    .commit()
            }
        })
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

}