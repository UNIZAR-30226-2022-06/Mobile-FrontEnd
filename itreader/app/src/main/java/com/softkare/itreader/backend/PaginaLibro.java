package com.softkare.itreader.backend;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PaginaLibro {

    @SerializedName("libro")
    @Expose
    private String libro;
    @SerializedName("pagina")
    @Expose
    private Integer pagina;
    @SerializedName("contenido")
    @Expose
    private String contenido;


    public PaginaLibro(String libro, Integer pagina, String contenido) {
        this.libro = libro;
        this.pagina = pagina;
        this.contenido = contenido;
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public Integer getPagina() {
        return pagina;
    }

    public void setPagina(Integer pagina) {
        this.pagina = pagina;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

}
