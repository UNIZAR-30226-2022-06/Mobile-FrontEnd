package com.softkare.itreader.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R
import com.softkare.itreader.adapter.bookAdapter
import com.softkare.itreader.backend.Libro
import com.softkare.itreader.backend.MyApiEndpointInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CatalogFragment : Fragment() {
    lateinit var list : List<Libro>
    lateinit var sublist : MutableList<Libro>
    var page = 1
    var isLoading = false
    var limit = 20
    lateinit var adapter: bookAdapter
    lateinit var layoutManager : LinearLayoutManager


    lateinit var contexto : Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_catalog,container,false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerBooks)
        val pb = view.findViewById<ProgressBar>(R.id.progress_bar)
        contexto = requireContext()
        recyclerView.layoutManager = LinearLayoutManager(context)
        list = listOf()
        sublist = mutableListOf()
        conseguir_lista(recyclerView)
        println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")


        //getPage(pb,list,sublist)

        /*recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (dy > 0){
                    val visibleItemCount = layoutManager.childCount
                    val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                    val total = adapter.itemCount
                    if(isLoading == false){

                        if((visibleItemCount + pastVisibleItem) >= total ){
                            page++
                            getPage(pb, list, sublist)
                        }

                    }
                }

                super.onScrolled(recyclerView, dx, dy)
            }
        })*/

        return view
    }

    fun getPage(pb: ProgressBar, list: List<Libro>, sublist: MutableList<Libro>) {
        isLoading = true
        pb.visibility = View.VISIBLE
        val start = (page-1)*limit
        val end = page*limit

        Handler().postDelayed({
                              if(::adapter.isInitialized){
                                  adapter.notifyDataSetChanged()
                              }else{
                                  adapter = bookAdapter(list)
                              }
            isLoading = false
            pb.visibility = View.GONE
        },5000)


    }

    private fun conseguir_lista(recyclerView : RecyclerView) {
        val retrofit = Retrofit.Builder()
            .baseUrl(MyApiEndpointInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(MyApiEndpointInterface::class.java)
        service.libroList().enqueue(object : Callback<List<Libro>> {
            override fun onResponse(call: Call<List<Libro>>, response: Response<List<Libro>>) {
                if (response.body() != null) {
                    list = response.body()!!
                    //recyclerView.adapter = bookAdapter(list)
                }
            }

            override fun onFailure(call: Call<List<Libro>>, t: Throwable) {
                println("FALLO REGISTRO")
            }
        })

    }
}