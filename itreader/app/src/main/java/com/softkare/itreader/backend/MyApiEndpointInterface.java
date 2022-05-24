package com.softkare.itreader.backend;

import android.net.http.HttpResponseCache;

import java.util.List;


import com.softkare.itreader.backend.Usuario;
import com.softkare.itreader.backend.Documento;
import com.softkare.itreader.backend.Libro;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface MyApiEndpointInterface {

    public static final String BASE_URL = "https://db-itreader-unizar.herokuapp.com/itreaderApp/";

    // Request method and URL specified in the annotation

    @GET("Usuarios/{nomUsuario}")
    Call<Usuario> getUser(@Path("nomUsuario") String nomUsuario);

    @POST("createUsuario/")
    Call<Usuario> createUser(@Body Usuario user);

    //@PUT("update/{userId}/")
    //Call<Usuario> updateUser(@Path("userId") int userId, @Body Usuario user);
    //@Multipart
    @PUT("updateUsuario/{nomUsuario}/")
    Call<Usuario> updateUser(@Path("nomUsuario") String nomUsuarioDestino,
                             @Body RequestBody body/*,
                             @Body RequestBody nomUsuarios,
                             @Body RequestBody password,
                             @Body RequestBody correo,
                             @Body RequestBody esAdmin*/
                            );

    @PUT("addDocsUsuario/{nomUsuario}/")
    Call<Usuario> addDocsUser(@Path("nomUsuario") String nomUsuarioDestino, @Body RequestBody body);

    @PUT("deleteDocUsuario/{nomUsuario}/")
    Call<ResponseBody> deleteDocUsuario(@Path("nomUsuario") String nomUsuarioDestino, @Body RequestBody body);

    @PUT("deleteLibroUsuario/{nomUsuario}/")
    Call<ResponseBody> deleteLibroUsuario(@Path("nomUsuario") String nomUsuarioDestino, @Body RequestBody body);

    @DELETE("deleteUsuario/{nomUsuario}/")
    Call<ResponseBody> deleteUser(@Path("nomUsuario") String nomUsuario);

    @GET("Usuarios/")
    Call<List<Usuario>> userList();

    @GET("Documentos/{nomUsuario}")
    Call<Documento> getDocumento(@Path("idDoc") String idDoc);

    @POST("createDocumento/")
    Call<Documento> createDocumento(@Body Documento doc);

    @POST("createLibro/")
    Call<Libro> createLibro(@Body Libro libro);

    @PUT("updateDocumento/{idDocDestino}/")
    Call<Documento> updateDocumento(@Path("idDoc") Integer idDocDestino, @Body RequestBody body);

    @GET("Documentos/")
    Call<List<Documento>> documentoList(/*@Query("page") Integer page*/);

    @GET("DocumentosUser/{usuario}/")
    Call<List<Documento>> documentoUser(@Path("usuario") String usuario);

    @GET("LibrosPage/")
    Call<ListaLibros> libroList(@Query("page") Integer pagina);

    @GET("Libros/")
    Call<List<Libro>> libros();

    @GET("UsuariosCorreo/{correo}")
    Call<Usuario> checkUser(@Path("correo")String correo);

    @GET("enviarCorreo/{correo}")
    Call<Usuario> enviarCorreo(@Path("correo")String correo);

    @PUT("valorarLibro/{nombre}/")
    Call<Libro> valorarLibro(@Path("nombre") String nomLibro, @Body RequestBody body);

    @DELETE("deleteLibro/{nomLibro}")
    Call<ResponseBody> deleteLibro(@Path("nomLibro") String nomLibro);

    @Multipart
    @POST("subirLibro/")
    Call<ResponseBody> subirLibro(@Part("usuario") RequestBody usuario, @Part MultipartBody.Part cover);

    @GET("leerLibro/{nombre}/{pagina}")
    Call<PaginaLibro> leerLibro(@Path("nombre") String nombre, @Path("pagina") Integer pagina);

    @POST("createMarca/")
    Call<ResponseBody> createMarca(@Body RequestBody body);

    @GET("MarcasUsuarioLibro/{nomUsuario}/{nomLibro}/")
    Call<List<Marca>> marcasUsuarioLibro(@Path("nomUsuario") String nomUsuario, @Path("nomLibro") String nomLibro);

    @DELETE("deleteMarca/{nomUsuario}/{idLibro}/{nomMarca}/")
    Call<ResponseBody> deleteMarca(@Path("nomUsuario") String nomUsuario,@Path("idLibro") Integer idLibro,@Path("nomMarca") String nomMarca );

    @PUT("updateMarcaAndroid/{nomUsuario}/{nomLibro}/")
    Call<ResponseBody> updateMarcaAndroid(@Path("nomUsuario") String nomUsuario,@Path("nomLibro") String nomLibro,@Body RequestBody body);

    @GET("MarcaPaginas/{nomUsuario}/{nomLibro}/")
    Call<List<Marca>> MarcaPaginas(@Path("nomUsuario") String nomUsuario,@Path("nomLibro") String nomLibro);

    @GET("compartirLibro/{nomUsuario}/{nomLibro}/{email}/")
    Call<Usuario> compartirLibro(@Path("nomUsuario")String nomUsuario,@Path("nomLibro")String nomLibro,@Path("email")String email );


}
