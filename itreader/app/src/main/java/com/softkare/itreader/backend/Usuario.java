package com.softkare.itreader.backend;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Usuario {

    @SerializedName("id")
    private Integer id;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("nomUsuario")
    private String nomUsuario;
    @SerializedName("password")
    private String password;
    @SerializedName("correo")
    private String correo;
    @SerializedName("esAdmin")
    private Boolean esAdmin;
    @SerializedName("docsAnyadidos")
    private List<Libro> docsAnyadidos;
    @SerializedName("docsSubidos")
    private List<Documento> docsSubidos;
    @SerializedName("letraSize")
    private String letraSize;
    @SerializedName("letraStyle")
    private String letraStyle;
    @SerializedName("colorFondo")
    private String colorFondo;

    public Usuario(String nombre, String nomUsuario, String password, String correo, Boolean esAdmin) {
        this.nombre = nombre;
        this.nomUsuario = nomUsuario;
        this.password = password;
        this.correo = correo;
        this.esAdmin = esAdmin;
        this.docsAnyadidos = new ArrayList();
        this.docsSubidos = new ArrayList();
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

    public Boolean getEsAdmin() {
        return esAdmin;
    }

    public void setEsAdmin(Boolean esAdmin) {
        this.esAdmin = esAdmin;
    }

    public List<Libro> getDocsAnyadidos() {
        return docsAnyadidos;
    }

    public void setDocsAnyadidos(List<Libro> docsAnyadidos) {
        this.docsAnyadidos = docsAnyadidos;
    }

    public List<Documento> getDocsSubidos() {
        return docsSubidos;
    }

    public void setDocsSubidos(List<Documento> docsSubidos) {
        this.docsSubidos = docsSubidos;
    }

    public String getLetraSize() {
        return letraSize;
    }

    public void setLetraSize(String letraSize) {
        this.letraSize = letraSize;
    }

    public String getLetraStyle() {
        return letraStyle;
    }

    public void setLetraStyle(String letraStyle) {
        this.letraStyle = letraStyle;
    }

    public String getColorFondo() {
        return colorFondo;
    }

    public void setColorFondo(String colorFondo) {
        this.colorFondo = colorFondo;
    }
}
