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
import androidx.fragment.app.Fragment
import com.softkare.itreader.R
import com.softkare.itreader.backend.Libro

class BookVisualizer : Fragment() {
    private var style = 1
    private val textSmallSize = 12F
    private val textMediumSize = 16F
    private val textLargeSize = 24F
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle : Bundle? = this.arguments
        val book : Libro = bundle?.getSerializable("book") as Libro
        val view = inflater.inflate(R.layout.visualizer_fragment, container, false)
        val arrowBack : ImageView = view.findViewById(R.id.arrow_back)
        val arrowForward : ImageView = view.findViewById(R.id.arrow_forward)
        val content : TextView = view.findViewById(R.id.content)
        val buttonSize : ImageView = view.findViewById(R.id.buttonSize)
        val buttonColor : ImageView = view.findViewById(R.id.buttonColor)
        val buttonSearch : ImageView = view.findViewById(R.id.buttonSearch)
        val buttonBookmark : ImageView = view.findViewById(R.id.buttonBookmark)
        val buttonFont : ImageView = view.findViewById(R.id.buttonFont)

        val builder = activity?.let { it1 -> AlertDialog.Builder(it1) }
        content.textSize = textMediumSize
        content.movementMethod = ScrollingMovementMethod.getInstance()
        content.text = "Aquí irá el contenido de la página recibida"

        arrowBack.setOnClickListener {
            //TODO: Pasar a página anterior o, si es la primera, volver a BookPageFragment
            val bundle = Bundle()
            bundle.putSerializable("book", book)
            val activity = view.context as AppCompatActivity
            val transit = BookPageFragment()
            transit.arguments = bundle
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, transit)
                .addToBackStack(null)
                .commit()
        }

        arrowForward.setOnClickListener {
            //TODO: Pasar a página siguiente si existe
            Toast.makeText(activity, getString(R.string.no_next_page), Toast.LENGTH_SHORT).show()
        }

        buttonSize.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), buttonSize)
            popupMenu.menuInflater.inflate(R.menu.menu_size, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener{ item ->
                when (item.itemId) {
                    R.id.small  -> content.textSize = textSmallSize
                    R.id.medium -> content.textSize = textMediumSize
                    R.id.large  -> content.textSize = textLargeSize
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
            val vista = layoutInflater.inflate(R.layout.dialog_textsearch, null)
            if (builder != null) {
                builder.setView(vista)
            }
            val dialog = builder?.create()
            if (dialog != null) {
                dialog.show()
            }
            val searchText: EditText = vista.findViewById(R.id.searchEditText)
            val buttonConfirmSearch: Button = vista.findViewById(R.id.buttonConfirmSearch)
            buttonConfirmSearch.setOnClickListener {
                if (dialog != null) {
                    dialog.hide()
                }
                resetSearchVisualizer(content)
                val spannableString = SpannableString(content.text)
                var index = content.text.indexOf(searchText.text.toString(), 0, true)
                if (index >= 0) {
                    var nFound = 1
                    spannableString.setSpan(
                        BackgroundColorSpan(resources.getColor(R.color.green)), index,
                        index + searchText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    while (index >= 0) {
                        index = content.text.indexOf(searchText.text.toString(), index+1, true)
                        if (index >= 0) {
                            spannableString.setSpan(
                                BackgroundColorSpan(resources.getColor(R.color.green)), index,
                                index + searchText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                            nFound++
                        }
                    }
                    content.text = spannableString
                    val textResults : String = nFound.toString() + " " + "resultados"
                    Toast.makeText(requireContext(), textResults, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "No hay resultados", Toast.LENGTH_SHORT).show()
                }
            }
        }

        buttonBookmark.setOnClickListener {
            //TODO: Gestion de Boorkmarks (por página)
            Toast.makeText(requireContext(), "Función por implementar", Toast.LENGTH_SHORT).show()
        }

        // FUNCIONALIDAD DE CAMBIAR LA FUENTE
        buttonFont.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), buttonFont)
            popupMenu.menuInflater.inflate(R.menu.menu_font, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.sans_serif -> content.typeface = Typeface.SANS_SERIF
                    R.id.monospace -> content.typeface = Typeface.MONOSPACE
                    R.id.serif -> content.typeface = Typeface.SERIF
                }
                true
            }
            popupMenu.show()
        }

        return view
    }

    private fun resetSearchVisualizer(content : TextView) {
        val spannableString = SpannableString(content.text)
        when (style) {
            1 -> spannableString.setSpan(BackgroundColorSpan(resources.getColor(R.color.white)),
                0, 1000, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            2 -> spannableString.setSpan(BackgroundColorSpan(resources.getColor(R.color.black)),
                0, 1000, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            3 -> spannableString.setSpan(BackgroundColorSpan(resources.getColor(R.color.light_orange)),
                0, 1000, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
}