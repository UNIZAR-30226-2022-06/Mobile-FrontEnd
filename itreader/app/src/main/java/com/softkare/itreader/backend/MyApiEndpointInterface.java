package com.softkare.itreader.backend;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
                             @Body RequestBody nombre,
                             @Body RequestBody nomUsuarios,
                             @Body RequestBody password,
                             @Body RequestBody correo,
                             @Body RequestBody esAdmin
                             );

    @GET("Usuarios")
    Call<List<Usuario>> userList(@Query("nomUsuario") String nomUsuario);
}
