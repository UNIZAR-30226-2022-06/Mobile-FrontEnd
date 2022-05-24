package com.softkare.itreader.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R
import com.softkare.itreader.adapter.bookmarkAdapter
import com.softkare.itreader.backend.Libro
import com.softkare.itreader.backend.Marca
import com.softkare.itreader.backend.MyApiEndpointInterface
import com.softkare.itreader.sharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookmarkListFragment : Fragment() {
    lateinit var book : Libro
    lateinit var recyclerView : RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bookmarklist, container, false)
        val arrowBack : ImageView = view.findViewById(R.id.arrow_back)
        val bundle : Bundle? = this.arguments
        book = bundle?.getSerializable("book") as Libro
        recyclerView = view.findViewById(R.id.recyclerBookmarks)
        recyclerView.layoutManager = LinearLayoutManager(context)
        getBookmarkList(view)

        arrowBack.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("book", book)
            val activity = view.context as AppCompatActivity
            val transit = BookVisualizer()
            transit.arguments = bundle
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, transit)
                .addToBackStack(null)
                .commit()
        }
        return view
    }

    private fun getBookmarkList(view : View) {
        val retrofit = Retrofit.Builder()
            .baseUrl(MyApiEndpointInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(MyApiEndpointInterface::class.java)
        service.marcasUsuarioLibro(sharedPreferences.prefs.getUsername(),book.nombre).enqueue(object :
            Callback<List<Marca>> {
            override fun onResponse(call: Call<List<Marca>>, response: Response<List<Marca>>) {
                if(response.body() != null) {
                    val list = response.body()!!
                    recyclerView.adapter = bookmarkAdapter(list, requireContext(), book, view)

                } else {
                    println("MARCAS NO DEVUELTAS")
                }
            }

            override fun onFailure(call: Call<List<Marca>>, t: Throwable) {
                println("ERROR AL LISTAR LA MARCA")
            }
        })
    }
}