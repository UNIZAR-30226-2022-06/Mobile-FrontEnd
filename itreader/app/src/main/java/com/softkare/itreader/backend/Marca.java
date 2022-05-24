package com.softkare.itreader.backend;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Marca {

    @SerializedName("usuario")
    @Expose
    private String usuario;
    @SerializedName("libro")
    @Expose
    private String libro;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("pagina")
    @Expose
    private Integer pagina;

    public Marca(String usuario, String libro, String nombre, Integer pagina) {
        this.usuario = usuario;
        this.libro = libro;
        this.nombre = nombre;
        this.pagina = pagina;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getPagina() {
        return  pagina;
    }

    public void setPagina(Integer pagina) {
        this.pagina = pagina;
    }
}
