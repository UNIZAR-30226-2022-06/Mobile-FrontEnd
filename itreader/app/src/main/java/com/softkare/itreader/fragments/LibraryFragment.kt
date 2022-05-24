package com.softkare.itreader.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R
import com.softkare.itreader.adapter.bookAdapter
import com.softkare.itreader.backend.MyApiEndpointInterface
import com.softkare.itreader.backend.Usuario
import com.softkare.itreader.sharedPreferences.Companion.prefs
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LibraryFragment : Fragment() {
    lateinit var user : Usuario
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_library,container,false)
        val recyclerView : RecyclerView = view.findViewById(R.id.recyclerBooks)
        val hidden : TextView = view.findViewById(R.id.hidden_text)
        hidden.visibility = TextView.INVISIBLE
        recyclerView.layoutManager=LinearLayoutManager(context)
        getList(recyclerView, hidden)

        return view
    }

    private fun getList(recyclerView: RecyclerView, textView: TextView) {
        val retrofit = Retrofit.Builder()
            .baseUrl(MyApiEndpointInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(MyApiEndpointInterface::class.java)
        service.getUser(prefs.getUsername()).enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                user = response.body()!!
                recyclerView.adapter = bookAdapter(user.docsAnyadidos)
                if (user.docsAnyadidos.isEmpty()) textView.visibility = TextView.VISIBLE
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                println("ERROR AL RECUPERAR EL USUARIO")
            }
        })
    }
}