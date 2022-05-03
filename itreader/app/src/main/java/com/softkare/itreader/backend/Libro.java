package com.softkare.itreader.backend;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Libro implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("titulo")
    @Expose
    private String titulo;
    @SerializedName("autor")
    @Expose
    private String autor;
    @SerializedName("foto")
    @Expose
    private String foto;
    @SerializedName("editorial")
    @Expose
    private String editorial;
    @SerializedName("genero")
    @Expose
    private String genero;
    @SerializedName("valoracion")
    @Expose
    private Float valoracion;

    public Libro(String titulo, String autor, String foto, String genero, String editorial, Float valoracion) {
        this.titulo = titulo;
        this.autor = autor;
        this.foto = foto;
        this.genero = genero;
        this.editorial = editorial;
        this.valoracion = valoracion;
    }

    public Integer getPk() {
        return id;
    }

    public void setPk(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public Float getValoracion() {
        return valoracion;
    }

    public void setValoracion(Float valoracion) {
        this.valoracion = valoracion;
    }
}
