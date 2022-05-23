package com.softkare.itreader.backend;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.Serializable;

public class Libro implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("linkDocumento")
    @Expose
    private String linkDocumento;
    @SerializedName("linkPortada")
    @Expose
    private String linkPortada;
    @SerializedName("ISBN")
    @Expose
    private String ISBN;
    @SerializedName("autor")
    @Expose
    private String autor;
    @SerializedName("editorial")
    @Expose
    private String editorial;
    @SerializedName("coverLib")
    @Expose
    private String coverLib;
    @SerializedName("valoracion")
    @Expose
    private Float valoracion;
    @SerializedName("numValoraciones")
    @Expose
    private int numValoraciones;

    public Libro(String nombre, String linkDocumento, String linkPortada, String ISBN, String autor,
                 String editorial, String coverLib, Float valoracion, int numValoraciones) {
        this.nombre = nombre;
        this.linkDocumento = linkDocumento;
        this.linkPortada = linkPortada;
        this.ISBN = ISBN;
        this.autor = autor;
        this.editorial = editorial;
        this.coverLib = coverLib;
        this.valoracion = valoracion;
        this.numValoraciones = numValoraciones;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLinkDocumento() {
        return linkDocumento;
    }

    public void setLinkDocumento(String linkDocumento) {
        this.linkDocumento = linkDocumento;
    }

    public String getLinkPortada() {
        return linkPortada;
    }

    public void setLinkPortada(String linkPortada) {
        this.linkPortada = linkPortada;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getCoverLib() {
        return coverLib;
    }

    public void setCoverLib(String coverLib) {
        this.coverLib = coverLib;
    }

    public Float getValoracion() {
        return valoracion;
    }

    public void setValoracion(Float valoracion) {
        this.valoracion = valoracion;
    }

    public int getNumValoraciones() {
        return numValoraciones;
    }

    public void setNumValoraciones(int numValoraciones) {
        this.numValoraciones = numValoraciones;
    }
}
