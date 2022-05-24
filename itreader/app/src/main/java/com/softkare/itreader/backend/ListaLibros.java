package com.softkare.itreader.backend;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListaLibros {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("next")
    @Expose
    private String next;
    @SerializedName("previous")
    @Expose
    private String previous;
    @SerializedName("results")
    @Expose
    private List<Libro> results;

    public ListaLibros(Integer count, String next, String previous, List<Libro> results){
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
    }

    public List<Libro> getResults() {
        return results;
    }

}
