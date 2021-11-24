package com.example.proyectocomic.structures;

import com.example.proyectocomic.comics.Comic;

public  class PriorityComic implements Comparable<PriorityComic>{
    public Comic comic;
    public String tomo, escritor, dibujante, precuela;

    public PriorityComic(Comic comic, String tomo, String escritor, String dibujante, String precuela) {
        this.comic = comic;
        this.tomo = tomo;
        this.escritor = escritor;
        this.dibujante = dibujante;
        this.precuela = precuela;
    }

    @Override
    public int compareTo(PriorityComic o) {
        return Integer.compare(this.comic.getAgno_publicacion(),o.comic.getAgno_publicacion());
    }
}