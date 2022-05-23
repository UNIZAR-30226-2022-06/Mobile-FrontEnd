package com.softkare.itreader.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
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
    var sizeOption = 1
    var colorOption = 1
    var fontOption = 1
    val textSmallSize = 20F
    val textMediumSize = 30F
    val textLargeSize = 40F
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
        val title : TextView = view.findViewById(R.id.book_page_title)
        val content : TextView = view.findViewById(R.id.page_content)
        val buttonSize : ImageView = view.findViewById(R.id.buttonSize)
        val buttonColor : ImageView = view.findViewById(R.id.buttonColor)
        val buttonSearch : ImageView = view.findViewById(R.id.buttonSearch)
        val buttonBookmark : ImageView = view.findViewById(R.id.buttonBookmark)
        val buttonFont : ImageView = view.findViewById(R.id.buttonFont)

        title.text = book.nombre
        val builder = activity?.let { it1 -> AlertDialog.Builder(it1) }

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

        // FUNCIONALIDAD DE CAMBIAR TAMAÑO DE TEXTO
        buttonSize.setOnClickListener {
            val vista = layoutInflater.inflate(R.layout.dialog_textsize, null)
            if (builder != null) { builder.setView(vista) }
            val dialog = builder?.create()
            if (dialog != null) { dialog.show() }
            val buttonSmall : ImageView = vista.findViewById(R.id.buttonSmall)
            val buttonMedium : ImageView = vista.findViewById(R.id.buttonMedium)
            val buttonLarge : ImageView = vista.findViewById(R.id.buttonLarge)
            buttonSmall.setOnClickListener {
                if (dialog != null) { dialog.hide() }
                if (sizeOption != 1) {
                    buttonSmall.setImageResource(R.drawable.ic_baseline_radio_button_checked_24)
                    buttonMedium.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
                    buttonLarge.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
                    content.textSize = textSmallSize
                    sizeOption = 1
                }
            }

            buttonMedium.setOnClickListener {
                if (dialog != null) { dialog.hide() }
                if (sizeOption != 2) {
                    buttonSmall.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
                    buttonMedium.setImageResource(R.drawable.ic_baseline_radio_button_checked_24)
                    buttonLarge.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
                    content.textSize = textMediumSize
                    sizeOption = 2
                }
            }

            buttonLarge.setOnClickListener {
                if (dialog != null) { dialog.hide() }
                if (sizeOption != 3) {
                    buttonSmall.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
                    buttonMedium.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
                    buttonLarge.setImageResource(R.drawable.ic_baseline_radio_button_checked_24)
                    content.textSize = textLargeSize
                    sizeOption = 3
                }
            }
        }

        // FUNCIONALIDAD DE CAMBIAR COLOR DE FONDO Y TEXTO
        buttonColor.setOnClickListener {
            val vista = layoutInflater.inflate(R.layout.dialog_backgroundcolor, null)
            if (builder != null) { builder.setView(vista) }
            val dialog = builder?.create()
            if (dialog != null) { dialog.show() }
            val buttonBoW : ImageView = vista.findViewById(R.id.buttonBoW)
            val buttonWoB : ImageView = vista.findViewById(R.id.buttonWoB)
            val buttonWarm : ImageView = vista.findViewById(R.id.buttonWarm)
            buttonBoW.setOnClickListener {
                if (dialog != null) { dialog.hide() }
                if (colorOption != 1) {
                    buttonBoW.setImageResource(R.drawable.ic_baseline_radio_button_checked_24)
                    buttonWoB.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
                    buttonWarm.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
                    content.setTextColor(resources.getColor(R.color.black))
                    content.setBackgroundColor(resources.getColor(R.color.white))
                    colorOption = 1
                }
            }

            buttonWoB.setOnClickListener {
                if (dialog != null) { dialog.hide() }
                if (colorOption != 2) {
                    buttonBoW.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
                    buttonWoB.setImageResource(R.drawable.ic_baseline_radio_button_checked_24)
                    buttonWarm.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
                    content.setTextColor(resources.getColor(R.color.white))
                    content.setBackgroundColor(resources.getColor(R.color.black))
                    colorOption = 2
                }
            }

            buttonWarm.setOnClickListener {
                if (dialog != null) { dialog.hide() }
                if (colorOption != 3) {
                    buttonBoW.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
                    buttonWoB.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
                    buttonWarm.setImageResource(R.drawable.ic_baseline_radio_button_checked_24)
                    content.setTextColor(resources.getColor(R.color.BLUEBACK))
                    content.setBackgroundColor(resources.getColor(R.color.light_orange))
                    colorOption = 3
                }
            }
        }

        // FUNCIONALIDAD DE BUSCAR PALABRAS EN TEXTO
        buttonSearch.setOnClickListener {
            val vista = layoutInflater.inflate(R.layout.dialog_textsearch, null)
            if (builder != null) { builder.setView(vista) }
            val dialog = builder?.create()
            if (dialog != null) { dialog.show() }
            val searchText : EditText = vista.findViewById(R.id.searchEditText)
            val buttonConfirmSearch : Button = vista.findViewById(R.id.buttonConfirmSearch)
            buttonConfirmSearch.setOnClickListener {
                if (dialog != null) { dialog.hide() }
                resetSearchVisualizer(content)
                val spannableString = SpannableString(content.text)
                val index = content.text.indexOf(searchText.text.toString(), 0, false)
                if (index >= 0) {
                    spannableString.setSpan(BackgroundColorSpan(resources.getColor(R.color.green)), index,
                        index + content.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    content.text = spannableString
                } else {
                    Toast.makeText(requireContext(), getString(R.string.no_occurrences), Toast.LENGTH_SHORT).show()
                }
            }
        }

        // FUNCIONALIDAD DE CREAR BOOKMARKS
        buttonBookmark.setOnClickListener {
            //TODO: Gestion de Boorkmarks (por página)
            Toast.makeText(requireContext(), "Función por implementar", Toast.LENGTH_SHORT).show()
        }

        // FUNCIONALIDAD DE CAMBIAR LA FUENTE
        buttonFont.setOnClickListener {
            val vista = layoutInflater.inflate(R.layout.dialog_textsearch, null)
            if (builder != null) { builder.setView(vista) }
            val dialog = builder?.create()
            if (dialog != null) { dialog.show() }
            val buttonSansSerif : ImageView = vista.findViewById(R.id.buttonSansSerif)
            val buttonMonospace : ImageView = vista.findViewById(R.id.buttonMonospace)
            val buttonSerif : ImageView = vista.findViewById(R.id.buttonSerif)
            buttonSansSerif.setOnClickListener {
                if (dialog != null) { dialog.hide() }
                if (fontOption != 1) {
                    buttonSansSerif.setImageResource(R.drawable.ic_baseline_radio_button_checked_24)
                    buttonMonospace.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
                    buttonSerif.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
                    content.typeface = Typeface.SANS_SERIF
                    fontOption = 1
                }
            }

            buttonMonospace.setOnClickListener {
                if (dialog != null) { dialog.hide() }
                if (fontOption != 2) {
                    buttonSansSerif.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
                    buttonMonospace.setImageResource(R.drawable.ic_baseline_radio_button_checked_24)
                    buttonSerif.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
                    content.typeface = Typeface.MONOSPACE
                    fontOption = 2
                }
            }

            buttonSerif.setOnClickListener {
                if (dialog != null) { dialog.hide() }
                if (fontOption != 3) {
                    buttonSansSerif.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
                    buttonMonospace.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
                    buttonSerif.setImageResource(R.drawable.ic_baseline_radio_button_checked_24)
                    content.typeface = Typeface.SERIF
                    fontOption = 3
                }
            }
        }

        return view
    }

    private fun resetSearchVisualizer(content : TextView) {
        val spannableString = SpannableString(content.text)
        when (colorOption) {
            1 -> spannableString.setSpan(BackgroundColorSpan(resources.getColor(R.color.white)),
                0, 1000, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            2 -> spannableString.setSpan(BackgroundColorSpan(resources.getColor(R.color.black)),
                0, 1000, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            3 -> spannableString.setSpan(BackgroundColorSpan(resources.getColor(R.color.light_orange)),
                0, 1000, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
}