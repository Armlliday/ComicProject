package com.example.proyectocomic.comics;

public class Categoria {
	
	protected String nombre;
	
	//Getters and Setters de la clase Categoria
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


    //Contructor de la clase categoria
	public Categoria(String nombre) {
		super();
		this.nombre = nombre;
	}
        
        @Override
        public boolean equals(Object categoria){
            return this.nombre.equalsIgnoreCase(((Categoria)categoria).getNombre());
        }

    @Override
    public String toString() {
        return this.nombre;
    }
        
        
}
