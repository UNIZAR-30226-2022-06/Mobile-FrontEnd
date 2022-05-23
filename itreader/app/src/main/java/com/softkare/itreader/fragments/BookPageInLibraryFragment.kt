package com.softkare.itreader.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.softkare.itreader.R
import com.softkare.itreader.backend.Libro
import com.softkare.itreader.backend.MyApiEndpointInterface
import com.softkare.itreader.sharedPreferences
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookPageInLibraryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle : Bundle? = this.arguments
        var book : Libro = bundle?.getSerializable("book") as Libro
        val view = inflater.inflate(R.layout.fragment_book_page_in_library, container, false)
        val buttonDelete = view.findViewById<Button>(R.id.buttonDeleteLibrary)
        val buttonRead = view.findViewById<Button>(R.id.buttonRead)

        val retrofit = Retrofit.Builder()
            .baseUrl(MyApiEndpointInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(MyApiEndpointInterface::class.java)

        buttonDelete.setOnClickListener {
            service.deleteDocUsuario(sharedPreferences.prefs.getUsername(), book.nombre).enqueue(object :
                Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Toast.makeText(activity, getString(R.string.book_deleted), Toast.LENGTH_SHORT).show()
                    val activity = view.context as AppCompatActivity
                    val transit = LibraryFragment()
                    transit.arguments = bundle
                    activity.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, transit)
                        .addToBackStack(null)
                        .commit()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    showAlertDelete()
                }
            })
        }

        buttonRead.setOnClickListener {
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

        val bookTitle : TextView = view.findViewById(R.id.book_page_title)
        val bookImage : ImageView = view.findViewById(R.id.book_page_image)
        val author : TextView = view.findViewById(R.id.autor)
        val editorial : TextView = view.findViewById(R.id.editorial)
        val rate : RatingBar = view.findViewById(R.id.rating)
        val buttonRate : Button = view.findViewById(R.id.buttonRate)

        Glide.with(bookImage.context).load(book.linkPortada).into(bookImage)
        bookTitle.text = book.nombre
        author.text = book.autor
        editorial.text = book.editorial
        rate.rating = book.valoracion

        rate.setOnRatingBarChangeListener { ratingBar, _, _ -> ratingBar.rating = book.valoracion }

        buttonRate.setOnClickListener {
            val builder = activity?.let { it1 -> AlertDialog.Builder(it1) }
            val vista = layoutInflater.inflate(R.layout.dialograte, null)
            builder?.setView(vista)
            val dialog = builder?.create()
            dialog?.show()
            val ratingEdit = vista.findViewById<RatingBar>(R.id.edit_rate)
            ratingEdit.setOnRatingBarChangeListener { ratingBar: RatingBar, rating: Float, _ ->
                val MEDIA_TYPE_JSON: MediaType? = MediaType.parse("application/json; charset=utf-8")
                val J = JSONObject()
                J.put("valoracion", rating)
                val body: RequestBody = RequestBody.create(MEDIA_TYPE_JSON, J.toString())
                dialog?.hide()
                service.valorarLibro(book.nombre, body).enqueue(object : Callback<Libro> {
                    override fun onResponse(call: Call<Libro>, response: Response<Libro>) {
                        if (response.body() != null) {
                            book = response.body()!!
                            rate.rating = book.valoracion
                            Toast.makeText(requireContext(), getString(R.string.rating_made) +
                                    ": $rating", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Libro>, t: Throwable) {
                        println("ERROR AL VALORAR EL LIBRO")
                    }
                })
            }
        }
        return view
    }

    private fun showAlertDelete() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Error")
        builder.setMessage(getString(R.string.alert_delete_to_library))
        builder.setPositiveButton("Ok",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}