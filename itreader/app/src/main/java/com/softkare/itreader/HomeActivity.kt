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
        //val UsernameFL=intent.getStringExtra("Username_fromLogin").toString()
        //println(UsernameFL)
        replaceFragment(libraryFragment)//,UsernameFL)
        bottomNav.setOnItemSelectedListener{
            when(it.itemId){
                R.id.Library -> replaceFragment(libraryFragment)//,UsernameFL)
                R.id.MyBooks -> replaceFragment(myBooksFragment)//,UsernameFL)
                R.id.Catalog -> replaceFragment(catalogFragment)//,UsernameFL)
                R.id.Profile -> replaceFragment(profileFragment)//,UsernameFL)
                R.id.Search -> replaceFragment(searchFragment)//,UsernameFL)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){//, username:String){
        //val bundle = Bundle()//.apply { putString("username",username) }
        //fragment.arguments = bundle
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

}