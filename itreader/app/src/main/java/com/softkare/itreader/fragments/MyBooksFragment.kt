package com.softkare.itreader.fragments


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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softkare.itreader.R
import com.softkare.itreader.adapter.documentAdapter
import com.softkare.itreader.backend.Documento
import com.softkare.itreader.backend.MyApiEndpointInterface
import com.softkare.itreader.sharedPreferences.Companion.prefs
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


class MyBooksFragment : Fragment() {
    lateinit var list : List<Documento>
    lateinit var mCtx : Context
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_books, container, false)
        val recyclerView : RecyclerView = view.findViewById(R.id.recyclerDocs)
        val buttonUpload : ImageButton = view.findViewById(R.id.btnUpload)
        recyclerView.layoutManager= LinearLayoutManager(context)
        getList(recyclerView)
        verifyStoragePermissions()

        buttonUpload.setOnClickListener{
            verifyStoragePermissions()

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            val mimeTypes = "PDF"
            intent.type = "application/pdf"
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(intent,102)

        }
        return view
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int, resultData: Intent?) {
        if (requestCode == 102 && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            var uri = resultData?.data
            var path = getRealPathFromUri(requireContext(),uri)
            println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
            println(path)
            val file = File(path)
            println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
            upload(file,uri)
        }
    }


    fun verifyStoragePermissions() {
        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val permission2 = ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                requireActivity(),
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
        if (permission2 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                requireActivity(),
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }

    }

    private fun upload(file: File,fileUri: Uri?) {
        var reqUsuario = RequestBody.create(MediaType.parse("text/plain"), prefs.getUsername())
        var reqFile = RequestBody.create(MediaType.parse(context?.contentResolver?.getType(fileUri!!)), file)
        val multipartFile = MultipartBody.Part.createFormData("file", file.name, reqFile)

        val retrofit = Retrofit.Builder()
            .baseUrl(MyApiEndpointInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(MyApiEndpointInterface::class.java)

        println(context?.contentResolver?.getType(fileUri!!))
        println(file.name)
        service.subirLibro(reqUsuario,multipartFile).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.body() != null){
                    println("LIBRO SUBIDO")
                }else{
                    showAlert()
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

    private fun getList(recyclerView: RecyclerView) {
        val retrofit = Retrofit.Builder()
            .baseUrl(MyApiEndpointInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(MyApiEndpointInterface::class.java)
        // SE DEBE LLAMAR AL SERVICIO DE LOS DOCUMENTOS SUBIDOS POR EL USUARIO
        service.documentoUser(prefs.getUsername()).enqueue(object : Callback<List<Documento>> {
            override fun onResponse(call: Call<List<Documento>>, response: Response<List<Documento>>) {
                if(response.body() != null){
                    list = response.body()!!
                    recyclerView.adapter = documentAdapter(list)
                }else{
                    showAlert()
                }
            }

            override fun onFailure(call: Call<List<Documento>>, t: Throwable) {
                println("ERROR AL RECIBIR LOS DOCUMENTOS DEL USUARIO")
            }

        })
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
        builder.setMessage(getString(R.string.no_books))
        builder.setPositiveButton("Ok",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
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



}