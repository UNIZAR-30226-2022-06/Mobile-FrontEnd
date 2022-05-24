package com.softkare.itreader.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.softkare.itreader.R
import com.softkare.itreader.adapter.documentAdapter
import com.softkare.itreader.backend.Documento
import com.softkare.itreader.backend.MyApiEndpointInterface
import com.softkare.itreader.backend.PaginaLibro
import com.softkare.itreader.sharedPreferences.Companion.prefs
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DocumentPageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle : Bundle? = this.arguments
        val documento : Documento = bundle?.getSerializable("document") as Documento
        val view = inflater.inflate(R.layout.fragment_document_page, container, false)
        val pageTitle : TextView = view.findViewById(R.id.doc_page_title)
        val docName : TextView = view.findViewById(R.id.doc_page_name)
        val username : TextView = view.findViewById(R.id.doc_page_username)
        pageTitle.text = documento.nombre
        docName.text = documento.nombre
        username.text = prefs.getUsername()

        val buttonRead : Button = view.findViewById(R.id.buttonReadDoc)
        val buttonDelete : Button = view.findViewById(R.id.buttonDeleteDocLibrary)

        buttonRead.setOnClickListener() {
            val retrofit = Retrofit.Builder()
                .baseUrl(MyApiEndpointInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(MyApiEndpointInterface::class.java)
            println(documento.nombre+".pdf")
            service.leerLibro(documento.nombre+".pdf" ,1).enqueue(object : Callback<PaginaLibro> {
                override fun onResponse(call: Call<PaginaLibro>, response: Response<PaginaLibro>) {
                    if(response.body() != null) {
                        val link : String? = response.body()!!.contenido
                        if (link != null) {
                            leerLibro(link)
                        }
                    }
                }

                override fun onFailure(call: Call<PaginaLibro>, t: Throwable) {
                    println("ERROR AL RECIBIR LOS DOCUMENTOS DEL USUARIO")
                }

            })
        }

        buttonDelete.setOnClickListener() {
            val retrofit = Retrofit.Builder()
                .baseUrl(MyApiEndpointInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(MyApiEndpointInterface::class.java)
            val J = JSONObject()
            J.put("nomLibro", documento.nombre)
            val body : RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), J.toString())
            service.deleteDocUsuario(prefs.getUsername(), body).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Toast.makeText(requireContext(),getString(R.string.deleted_doc), Toast.LENGTH_SHORT).show()
                    volver()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println("ERROR AL RECIBIR LOS DOCUMENTOS DEL USUARIO")
                }

            })
        }

        return view
    }

    private fun leerLibro(link : String){
        val intent = Intent(requireContext(), WebActivity::class.java).apply {
            putExtra("pdf_url", link)
        }
        startActivity(intent)
    }

    private fun volver(){
        val profFr = MyBooksFragment()
        val transicion = activity?.supportFragmentManager?.beginTransaction()
        transicion?.replace(R.id.fragment_container, profFr)
        //transicion?.addToBackStack(null)
        transicion?.commit()
    }
}