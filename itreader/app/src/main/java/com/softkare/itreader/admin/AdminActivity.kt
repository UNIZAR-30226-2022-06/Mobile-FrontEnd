package com.softkare.itreader.admin

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R
import com.softkare.itreader.adapter.catalogAdapter
import com.softkare.itreader.backend.Libro
import com.softkare.itreader.backend.ListaLibros
import com.softkare.itreader.backend.MyApiEndpointInterface
import com.softkare.itreader.sharedPreferences
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class AdminActivity : AppCompatActivity() {
    lateinit var list : List<Libro>
    lateinit var list2 : ListaLibros
    lateinit var sublist : MutableList<Libro>
    lateinit var sublist2 : MutableList<Libro>
    var page = 1
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        val buttonUpload : ImageButton = findViewById(R.id.btnUploadAdmin)
        val mRecyclerView : RecyclerView = findViewById(R.id.recyclerCatalogAdmin)
        val searchView : SearchView = findViewById(R.id.searchviewAdmin)
        val btnCharge : Button = findViewById(R.id.cargar)
        val btnReset : Button = findViewById(R.id.buttonReset)

        getCatalog(mRecyclerView, btnCharge)
        list = listOf()
        sublist = mutableListOf()
        sublist2 = mutableListOf()

        btnCharge.setOnClickListener {
            page++
            getCatalog(mRecyclerView, btnCharge)
            searchView.setQuery("",false)
            searchView.clearFocus()
        }

        btnReset.setOnClickListener {
            searchView.setQuery("",false)
            searchView.clearFocus()
            btnCharge.visibility = View.VISIBLE
            sublist = mutableListOf()
            sublist2 = mutableListOf()
            page = 1
            getCatalog(mRecyclerView, btnCharge)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchView.clearFocus()
                sublist.clear()
                for (b in list) {
                    if (b.nombre.lowercase().contains(p0.toString().lowercase())) {
                        sublist.add(b)
                    }
                    mRecyclerView.adapter = catalogAdapter(sublist, this@AdminActivity)
                    btnCharge.visibility = View.GONE
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean = false
        })

        verifyStoragePermissions()
        buttonUpload.setOnClickListener {
            verifyStoragePermissions()

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            val mimeTypes = "PDF"
            intent.type = "application/pdf"
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(intent,102)
        }
    }

    private fun getCatalog(recyclerView: RecyclerView, btn : Button) {
        val retrofit = Retrofit.Builder()
            .baseUrl(MyApiEndpointInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(MyApiEndpointInterface::class.java)
        service.libroList(page).enqueue(object : Callback<ListaLibros> {
            override fun onResponse(call: Call<ListaLibros>, response: Response<ListaLibros>) {
                if(response.body() != null) {
                    list2 = response.body()!!
                    sublist2.addAll(list2.results)
                    list = sublist2
                    recyclerView.adapter = catalogAdapter(list, this@AdminActivity)
                } else {
                    Toast.makeText(this@AdminActivity, "No hay más libros que mostrar", Toast.LENGTH_SHORT).show()
                    btn.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<ListaLibros>, t: Throwable) {
                println("ERROR AL RECIBIR EL CATÁLOGO DE LIBROS")
            }
        })
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == 102 && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            var uri = resultData?.data
            var path = getRealPathFromUri(this,uri)
            val file = File(path)
            Toast.makeText(this, "La ruta "+path+" no es accesible en este dispositivo", Toast.LENGTH_SHORT).show()
            //upload(file,uri)
        }
    }

    fun getRealPathFromUri(ctx: Context, uri: Uri?): String? {
        var picturePath = ""
        val filePathColumn = arrayOf(MediaStore.Files.FileColumns.DATA)
        val cursor: Cursor? = ctx.contentResolver.query(
            uri!!, filePathColumn,
            null, null, null
        )
        if (cursor != null) {
            cursor.moveToFirst()
            val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
            picturePath = cursor.getString(columnIndex)
            Log.e("", "picturePath : $picturePath")
            cursor.close()
        }
        return picturePath
    }

    fun verifyStoragePermissions() {
        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val permission2 = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                this,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
        if (permission2 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                this,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    private fun upload(file: File,fileUri: Uri?) {
        var reqUsuario = RequestBody.create(MediaType.parse("text/plain"), sharedPreferences.prefs.getUsername())
        var reqFile = RequestBody.create(MediaType.parse(this.contentResolver?.getType(fileUri!!)), file)
        val multipartFile = MultipartBody.Part.createFormData("file", file.name, reqFile)

        val retrofit = Retrofit.Builder()
            .baseUrl(MyApiEndpointInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(MyApiEndpointInterface::class.java)

        println(this.contentResolver?.getType(fileUri!!))
        println(file.name)
        service.subirLibro(reqUsuario,multipartFile).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.body() != null){
                    println("LIBRO SUBIDO")
                }else{
                    println("LIBRO NO SUBIDO")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println("ERROR AL SUBIR LIBRO")
                println(t.message)
                println(t.cause)
                println(t.localizedMessage)
                println(t.stackTrace)
            }
        })
    }
}