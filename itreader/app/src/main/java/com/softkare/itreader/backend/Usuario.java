package com.softkare.itreader.backend;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Usuario {

    @SerializedName("pk")
    @Expose
    private Integer pk;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("nomUsuario")
    @Expose
    private String nomUsuario;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("correo")
    @Expose
    private String correo;


    public Usuario(String nombre, String nomUsuario, String password,
                     String email) {
        this.nombre = nombre;
        this.nomUsuario = nomUsuario;
        this.password = password;
        this.correo = email;
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNomUsuario() {
        return nomUsuario;
    }

    public void setNomUsuario(String nomUsuario) {
        this.nomUsuario = nomUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

}
