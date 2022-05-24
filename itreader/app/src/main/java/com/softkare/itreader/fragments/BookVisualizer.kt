package com.softkare.itreader.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.ScrollingMovementMethod
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R
import com.softkare.itreader.adapter.bookmarkAdapter
import com.softkare.itreader.backend.Libro
import com.softkare.itreader.backend.Marca
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

class BookVisualizer : Fragment() {
    private var style = 1
    private var pageNumber = 1
    private val textSmallSize = 12F
    private val textMediumSize = 16F
    private val textLargeSize = 24F
    private val textVeryLargeSize = 36F
    lateinit var content : TextView
    lateinit var page : PaginaLibro
    lateinit var book : Libro
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle : Bundle? = this.arguments
        val view = inflater.inflate(R.layout.visualizer_fragment, container, false)
        val arrowBack : ImageView = view.findViewById(R.id.arrow_back)
        val arrowForward : ImageView = view.findViewById(R.id.arrow_forward)
        val buttonSize : ImageView = view.findViewById(R.id.buttonSize)
        val buttonColor : ImageView = view.findViewById(R.id.buttonColor)
        val buttonSearch : ImageView = view.findViewById(R.id.buttonSearch)
        val buttonBookmark : ImageView = view.findViewById(R.id.buttonBookmark)
        val buttonFont : ImageView = view.findViewById(R.id.buttonFont)

        val builder = activity?.let { it1 -> AlertDialog.Builder(it1) }
        content = view.findViewById(R.id.content)
        book = bundle?.getSerializable("book") as Libro
        content.textSize = textMediumSize
        content.movementMethod = ScrollingMovementMethod.getInstance()

        // CONSULTAR EL MARCAPÁGINAS, SI EXISTE
        // pageNumber = marcapaginas
        getPage()
        //chargePage()

        arrowBack.setOnClickListener {
            if (pageNumber == 1) {
                // Poner marcapáginas para este libro de este usuario
                val J = JSONObject()
                J.put("nombre", book.nombre)
                J.put("nomLibro", book.nombre)
                J.put("nomLibro", book.nombre)
                val body : RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), J.toString())
                val bundle = Bundle()
                bundle.putSerializable("book", book)
                val activity = view.context as AppCompatActivity
                val transit = BookPageInLibraryFragment()
                transit.arguments = bundle
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, transit)
                    .addToBackStack(null)
                    .commit()
            } else {
                pageNumber--
                chargePage()
            }
        }

        arrowForward.setOnClickListener {
            pageNumber++
            val retrofit = Retrofit.Builder()
                .baseUrl(MyApiEndpointInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val J = JSONObject()
            J.put("pagina", pageNumber)
            val body : RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), J.toString())
            val service = retrofit.create(MyApiEndpointInterface::class.java)
            service.updateMarcaAndroid(prefs.getUsername(), book.nombre, body).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.body() != null) {
                        println("MARCADOR ACTUALIZADO")
                    } else {
                        println("MARCADOR NO ACTUALIZADO")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println("ERROR AL RECIBIR LA PÁGINA DEL LIBRO")
                }
            })
            chargePage()
        }

        buttonSize.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), buttonSize)
            popupMenu.menuInflater.inflate(R.menu.menu_size, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener{ item ->
                when (item.itemId) {
                    R.id.small  -> content.textSize = textSmallSize
                    R.id.medium -> content.textSize = textMediumSize
                    R.id.large  -> content.textSize = textLargeSize
                    R.id.very_large -> content.textSize = textVeryLargeSize
                }
                true
            }
            popupMenu.show()
        }

        buttonColor.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), buttonColor)
            popupMenu.menuInflater.inflate(R.menu.menu_style, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {item ->
                when (item.itemId) {
                    R.id.bow -> {
                        style = 1
                        content.setTextColor(resources.getColor(R.color.black))
                        content.setBackgroundColor(resources.getColor(R.color.white))
                    }
                    R.id.wob -> {
                        style = 2
                        content.setTextColor(resources.getColor(R.color.white))
                        content.setBackgroundColor(resources.getColor(R.color.black))
                    }
                    R.id.warm -> {
                        style = 3
                        content.setTextColor(resources.getColor(R.color.BLUEBACK))
                        content.setBackgroundColor(resources.getColor(R.color.light_orange))
                    }
                }
                true
            }
            popupMenu.show()
        }

        buttonSearch.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), buttonSearch)
            popupMenu.menuInflater.inflate(R.menu.menu_visualizer_search, popupMenu.menu)
            val vista = layoutInflater.inflate(R.layout.dialog_textsearch, null)
            builder?.setView(vista)
            val dialog = builder?.create()
            val searchText: EditText = vista.findViewById(R.id.searchEditText)
            val buttonConfirmSearch: Button = vista.findViewById(R.id.buttonConfirmSearch)
            val label : TextView = vista.findViewById(R.id.search_visualizer_label)
            popupMenu.setOnMenuItemClickListener {item ->
                when (item.itemId) {
                    R.id.search_words -> {
                        label.text = getString(R.string.textsearch_label_word)
                        dialog?.show()
                        buttonConfirmSearch.setOnClickListener {
                            dialog?.hide()
                            content.text = page.contenido
                            val mSpannableString = SpannableString(content.text)
                            var index = content.text.indexOf(searchText.text.toString(), 0, true)
                            if (index >= 0) {
                                var nFound = 1
                                mSpannableString.setSpan(
                                    BackgroundColorSpan(resources.getColor(R.color.green)), index,
                                    index + searchText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                                while (index >= 0) {
                                    index = content.text.indexOf(searchText.text.toString(), index+1, true)
                                    if (index >= 0) {
                                        mSpannableString.setSpan(
                                            BackgroundColorSpan(resources.getColor(R.color.green)), index,
                                            index + searchText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                                        nFound++
                                    }
                                }
                                content.text = mSpannableString
                                val textResults : String = nFound.toString() + " " + getString(R.string.results)
                                Toast.makeText(requireContext(), textResults, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(requireContext(), getString(R.string.no_results), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    R.id.search_page -> {
                        label.text = getString(R.string.textsearch_label_page)
                        dialog?.show()
                        buttonConfirmSearch.setOnClickListener {
                            dialog?.hide()
                            if (searchText.text.isDigitsOnly()) {
                                println(searchText.text.isDigitsOnly())
                                println(searchText.text.toString())
                                pageNumber = Integer.parseInt(searchText.text.toString())
                                chargePage()
                            }
                        }
                    }
                }
                true
            }
            popupMenu.show()
        }

        buttonBookmark.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), buttonBookmark)
            popupMenu.menuInflater.inflate(R.menu.menu_bookmarks, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {item ->
                when (item.itemId) {
                    R.id.create_bookmark -> {
                        // Interactividad para crear nombre
                        createBookmark()
                    }
                    R.id.list_bookmarks  -> {
                        val vista = layoutInflater.inflate(R.layout.dialog_bookmarklist, null)
                        builder?.setView(vista)
                        val dialog = builder?.create()
                        dialog?.show()
                        val recyclerView : RecyclerView = vista.findViewById(R.id.recyclerBookmarks)
                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        getBookmarkList(recyclerView)
                    }
                }
                true
            }
            popupMenu.show()
        }

        buttonFont.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), buttonFont)
            popupMenu.menuInflater.inflate(R.menu.menu_font, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.sans_serif -> content.typeface = Typeface.SANS_SERIF
                    R.id.monospace -> content.typeface = Typeface.MONOSPACE
                    R.id.serif -> content.typeface = Typeface.SERIF
                    R.id.align_start -> content.gravity = 0
                    R.id.align_center -> content.gravity = 1
                }
                true
            }
            popupMenu.show()
        }

        return view
    }



    private fun getPage(){
        val retrofit = Retrofit.Builder()
            .baseUrl(MyApiEndpointInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        println(book.nombre)
        val service = retrofit.create(MyApiEndpointInterface::class.java)
        service.MarcaPaginas(prefs.getUsername(), book.nombre).enqueue(object : Callback<List<Marca>> {
            override fun onResponse(call: Call<List<Marca>>, response: Response<List<Marca>>) {
                if (response.body() != null) {
                    var marcador  = response.body()!!
                    pageNumber = marcador[0].pagina
                    chargePage()
                    println(pageNumber)
                } else {
                    println("PAGINA VACIA")
                }
            }

            override fun onFailure(call: Call<List<Marca>>, t: Throwable) {
                println("ERROR AL RECIBIR LA PÁGINA DEL LIBRO")
            }
        })
    }



    private fun chargePage() {
        val retrofit = Retrofit.Builder()
            .baseUrl(MyApiEndpointInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(MyApiEndpointInterface::class.java)
        service.leerLibro(book.nombre, pageNumber).enqueue(object : Callback<PaginaLibro> {
            override fun onResponse(call: Call<PaginaLibro>, response: Response<PaginaLibro>) {
                if (response.body() != null) {
                    page = response.body()!!
                    pageNumber = page.pagina
                    //TODO ACTUALIZAR MARCADOR GENERAL
                    content.text = page.contenido
                } else {
                    println("PAGINA VACIA")
                }
            }

            override fun onFailure(call: Call<PaginaLibro>, t: Throwable) {
                println("ERROR AL RECIBIR LA PÁGINA DEL LIBRO")
            }
        })
    }

    private fun getBookmarkList(recyclerView: RecyclerView) {
        val retrofit = Retrofit.Builder()
            .baseUrl(MyApiEndpointInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(MyApiEndpointInterface::class.java)
        service.marcasUsuarioLibro(prefs.getUsername(),book.nombre).enqueue(object : Callback<List<Marca>> {
            override fun onResponse(call: Call<List<Marca>>, response: Response<List<Marca>>) {
                if(response.body() != null){
                    var list = response.body()!!
                    recyclerView.adapter = bookmarkAdapter(list, requireContext())
                }else{
                    println("MARCAS NO DEVUELTAS")
                }

            }

            override fun onFailure(call: Call<List<Marca>>, t: Throwable) {
                println("ERROR AL LISTAR LA MARCA")
            }
        })
        //val list : List<String> = listOf("Inicio","Sorpresa","Tragedia","Mequetrefe","Distorsión","Zopenco","Legislatura","Tragedia","Mequetrefe","Distorsión")
        //recyclerView.adapter = bookmarkAdapter(list, requireContext())
    }

    private fun createBookmark() {
        val J = JSONObject()
        J.put("usuario", prefs.getUsername())
        J.put("libro", book.nombre)
        J.put("nombre", "MARCADÑÑ")
        J.put("pagina", pageNumber)
        J.put("esUlt", 0)
        val body : RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), J.toString())
        val retrofit = Retrofit.Builder()
            .baseUrl(MyApiEndpointInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(MyApiEndpointInterface::class.java)
        service.createMarca(body).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.body() != null){
                    println("MARCA CREADA")
                }else{
                    println("MARCA NO CREADA")
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println("ERROR AL CREAR LA MARCA")
            }
        })

    }

}