package com.softkare.itreader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.softkare.itreader.backend.Preferencias
import com.softkare.itreader.fragments.*

class HomeActivity : AppCompatActivity() {

    private val libraryFragment = LibraryFragment()
    private val catalogFragment = CatalogFragment()
    private val profileFragment = ProfileFragment()
    private val myBooksFragment = MyBooksFragment()
    private val searchFragment = SearchFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val bottomNav:BottomNavigationView = findViewById(R.id.bottom_navigation)
        replaceFragment(libraryFragment)//,UsernameFL)
        bottomNav.setOnItemSelectedListener{
            when(it.itemId){
                R.id.Library -> replaceFragment(libraryFragment)
                R.id.MyBooks -> replaceFragment(myBooksFragment)
                R.id.Catalog -> replaceFragment(catalogFragment)
                R.id.Profile -> replaceFragment(profileFragment)
                R.id.Search -> replaceFragment(searchFragment)
            }
            true
            
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

}
