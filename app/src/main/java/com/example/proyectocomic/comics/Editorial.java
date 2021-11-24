package com.example.proyectocomic.comics;


import com.example.proyectocomic.structures.DynamicArray;

public class Editorial {
	
	protected String nombre;
	protected DynamicArray<Tomo> tomos;



//Getters and Setter de la clase Editorial.

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	//Contructor clase Editorial.
     
	public Editorial(String nombre, DynamicArray<Tomo> tomos) {
		super();
		this.nombre = nombre;
		this.tomos = tomos;
	}

    public DynamicArray<Tomo> getTomos() {
        return tomos;
    }

    public void setTomos(DynamicArray<Tomo> tomos) {
        this.tomos = tomos;
    }

    @Override
    public boolean equals(Object obj) {
        Editorial editorial = (Editorial)obj;
        
        return this.nombre.equalsIgnoreCase(editorial.getNombre()) &&
                this.tomos.equals(editorial.getTomos());
    }
    
    

        
}
