package com.example.proyectocomic.comics;


import com.example.proyectocomic.structures.DynamicArray;

public class Lenguaje {
	
	private String lenguaje;
	private DynamicArray<Tomo> tomos;
	
	public Lenguaje(String lenguaje, DynamicArray<Tomo> tomos) {
		super();
		this.lenguaje = lenguaje;
		this.tomos = tomos;
	}

        public Lenguaje(String lenguaje) {
            this.lenguaje = lenguaje;
        }
        
        

	public String getLenguaje() {
		return lenguaje;
	}

	public void setLenguaje(String lenguaje) {
		this.lenguaje = lenguaje;
	}

        public DynamicArray<Tomo> getTomos() {
        return tomos;
        }

        public void setTomos(DynamicArray<Tomo> tomos) {
        this.tomos = tomos;
        }

	

    @Override
    public boolean equals(Object obj) {
        Lenguaje lenguaje = (Lenguaje)obj;
        return this.lenguaje.equalsIgnoreCase(lenguaje.getLenguaje()) &&
                this.tomos.equals(lenguaje.getTomos());
    }
	
        
}
