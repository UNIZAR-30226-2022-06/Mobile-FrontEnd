package com.softkare.itreader.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.softkare.itreader.R
import com.softkare.itreader.sharedPreferences.Companion.prefs

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        setup(view)
        return view
    }

    private fun setup(view: View){

        val name = view.findViewById<EditText>(R.id.editTextTextPersonName)
        val username = view.findViewById<EditText>(R.id.editTextTextPersonName2)
        val email = view.findViewById<EditText>(R.id.editTextTextPersonName3)

        name.hint = prefs.getName()
        username.hint = prefs.getUsername()
        email.hint = prefs.getEmail()

    }

}