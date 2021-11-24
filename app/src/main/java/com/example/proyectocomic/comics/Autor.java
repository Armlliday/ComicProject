package com.example.proyectocomic.comics;


import android.os.Parcel;
import android.os.Parcelable;

import com.example.proyectocomic.structures.DynamicArray;

public class Autor implements Parcelable {

	private String nombre;
	private int edad;
	private DynamicArray<Categoria> categorias;
	
	public Autor(String nombre, int edad, DynamicArray<Categoria> categorias) {
		super();
		this.nombre = nombre;
		this.edad = edad;
		this.categorias = categorias;
	}

        public Autor(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
        } 
        
        public Autor(String nombre){
            this.nombre = nombre;
        }

	protected Autor(Parcel in) {
		nombre = in.readString();
		edad = in.readInt();
	}

	public static final Creator<Autor> CREATOR = new Creator<Autor>() {
		@Override
		public Autor createFromParcel(Parcel in) {
			return new Autor(in);
		}

		@Override
		public Autor[] newArray(int size) {
			return new Autor[size];
		}
	};

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

        public DynamicArray<Categoria> getCategorias() {
        return categorias;
        }

        public void setCategorias(DynamicArray<Categoria> categorias) {
        this.categorias = categorias;
        }

    @Override
    public boolean equals(Object obj) {
        return this.nombre.equalsIgnoreCase(((Autor)obj).getNombre()) &&
                this.edad == ((Autor)obj).getEdad();
    }


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(nombre);
		dest.writeInt(edad);
	}
}
