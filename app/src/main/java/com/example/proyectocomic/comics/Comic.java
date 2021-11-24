package com.example.proyectocomic.comics;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.HashMap;
import java.util.Objects;


public class Comic implements Parcelable, Comparable<Comic> {

    public static Hashmap<String, Comic> successor = new Hashmap<String, Comic>();
    public static Hashmap<String, Comic> predecessor = new Hashmap<String, Comic>();
    private String nombre;
    private int agno_publicacion;
    private String descripcion;
    private Comic secuela;
    private Comic precuela;
    private Autor escritor;
    private Autor dibujante;
    private Lenguaje lenguaje;
    public int foto;
    public String nombre_Tom;
    public String categorias;


    //Contructor de la clase Comic.

    public Comic(String nombre, int agno_publicacion, Autor escritor, Autor dibujante, String descripcion, int foto) {
        this.nombre = nombre;
        this.agno_publicacion = agno_publicacion;
        this.descripcion = descripcion;
        this.escritor = escritor;
        this.dibujante = dibujante;
        this.foto=foto;
    }

    public Comic(String nombre, int agno_publicacion, Autor escritor, Autor dibujante, String descripcion, int foto, String nombre_Tom, String categorias) {
        this.nombre = nombre;
        this.agno_publicacion = agno_publicacion;
        this.descripcion = descripcion;
        this.escritor = escritor;
        this.dibujante = dibujante;
        this.foto=foto;
        this.nombre_Tom = nombre_Tom;
        this.categorias = categorias;
    }

    public Comic(String nombre, int agno_publicacion, Autor escritor, Autor dibujante) {
        this.nombre = nombre;
        this.agno_publicacion = agno_publicacion;
        this.escritor = escritor;
        this.dibujante = dibujante;
    }


    protected Comic(Parcel in) {
        nombre = in.readString();
        agno_publicacion = in.readInt();
        descripcion = in.readString();
        foto = in.readInt();
        nombre_Tom = in.readString();
        categorias = in.readString();
        long startTime = System.nanoTime();
        if(successor.hasKey(nombre)){secuela = successor.get(nombre);}
        if(predecessor.hasKey(nombre)){precuela = predecessor.get(nombre);}
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);
        Log.d("Huevo1",""+duration);
    }


    public static final Creator<Comic> CREATOR = new Creator<Comic>() {
        @Override
        public Comic createFromParcel(Parcel in) {
            return new Comic(in);
        }

        @Override
        public Comic[] newArray(int size) {
            return new Comic[size];
        }
    };

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAgno_publicacion() {
        return agno_publicacion;
    }

    public Autor getEscritor() {
        return escritor;
    }

    public void setEscritor(Autor escritor) {
        this.escritor = escritor;
    }

    public Autor getDibujante() {
        return dibujante;
    }

    public void setDibujante(Autor dibujante) {
        this.dibujante = dibujante;
    }

    public void setAgno_publicacion(int agno_publicacion) {
        this.agno_publicacion = agno_publicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Comic getSecuela() {
        return secuela;
    }

    public void setSecuela(Comic secuela) {
        this.secuela = secuela;
    }

    public Comic getPrecuela() {
        return precuela;
    }

    public void setPrecuela(Comic precuela) {
        this.precuela = precuela;
    }

    public Lenguaje getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(Lenguaje lenguaje) {
        this.lenguaje = lenguaje;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        Comic comic = (Comic)obj;
        return this.agno_publicacion == comic.getAgno_publicacion() &&
                this.dibujante.equals(comic.getDibujante())         &&
                this.escritor.equals(comic.getEscritor())           &&
                this.nombre.equalsIgnoreCase(comic.getNombre())     &&
                this.foto == comic.getFoto();
    }

    @Override
    public String toString() {
        return this.nombre + " " +  this.getAgno_publicacion()+ " " + this.getEscritor().getNombre() + " " +
                this.getDibujante().getNombre()+" "+hayDescripcion();
    }

    public boolean hayDescripcion(){
        return descripcion!=null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeInt(agno_publicacion);
        dest.writeString(descripcion);
        dest.writeInt(foto);
        dest.writeString(nombre_Tom);
        dest.writeString(categorias);
        long startTime = System.nanoTime();
        successor.set(nombre,secuela);
        predecessor.set(nombre,precuela);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        Log.d("Huevo2",""+duration);

    }


    @Override
    public int compareTo(Comic o) {
        return this.getNombre().compareTo(o.getNombre());
    }
}