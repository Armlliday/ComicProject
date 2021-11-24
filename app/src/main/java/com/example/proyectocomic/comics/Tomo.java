package com.example.proyectocomic.comics;


import com.example.proyectocomic.structures.DynamicArray;


import java.util.EmptyStackException;



public class Tomo {
	
	private String nombre;
	private Autor escritor;
    private Autor dibujante;
	private int agno_publicacion;
	private String descripcion;
	private Comic cabeza;
	private Comic cola;
	private int tamano;
	private boolean finalizado;
	private DynamicArray<Categoria> categorias;
	private Lenguaje lenguaje;
	private Editorial editorial;
	
	
	//Contructor clase Tomo
        

	  public Tomo(String nombre, Autor escritor, Autor dibujante, int agno_publicacion, boolean finalizado, DynamicArray<Categoria> categorias)
	    {
            this.nombre = nombre;
            this.escritor = escritor;
            this.dibujante = dibujante;
            this.agno_publicacion = agno_publicacion;
            this.finalizado = finalizado;
            this.categorias = categorias;
            this.cabeza = null;
            this.cola = null;
	    }

        public boolean vacio() {
        return(tamano==0);
        }
	    
	    public int tamano()
	    {
	        return(tamano);
	    }
	    
	    public void agregarFrente(Comic comic)
	    {
	        comic.setSecuela(cabeza);
	        comic.setPrecuela(cabeza.getPrecuela());
	        this.cabeza.setPrecuela(comic);
	        this.cabeza = comic;
	        if(cola==null){
	            this.cola = cabeza;
	        }
	        this.tamano++;
	    }
	    
	    public Comic quitarFrente()
	    {
	        if(tamano==0){
	            throw new EmptyStackException();
	        }
	        Comic viejaCabeza = this.cabeza;
	        this.cabeza = cabeza.getSecuela();
	        if(cabeza!=null && cabeza.getSecuela() != null) {
	        	this.cabeza.getSecuela().setPrecuela(cabeza);
	        }
	        if(cabeza==null){
	            this.cola = null;
	        }
	        this.tamano--;
	        return viejaCabeza;
	    }
	    
	    public void agregarAtras(Comic comic)
	    {
	        comic.setSecuela(null);
	        if(cola==null){
	            this.cabeza = comic;
	            this.cola = comic;
	            comic.setPrecuela(null);
	        } else{
	            this.cola.setSecuela(comic);
	            comic.setPrecuela(cola);
	            this.cola = comic;
	        }
	        this.tamano++;
	    }
	    
	    public Comic quitarAtras()
	    {
	        if(tamano==0){
	            throw new EmptyStackException();
	        }
	        Comic viejaCola = this.cola;
	        if(cabeza==cola){
	            this.cabeza = null;
	            this.cola = null;
	        } else{
	        	this.cola = cola.getPrecuela();
	        	this.cola.setSecuela(null);
	        }
	        this.tamano--;
	        return viejaCola;
	    }
	    
	    public void agregarDespues(Comic comicSiendoAgregado, Comic comic){
	    	comicSiendoAgregado.setSecuela(comic.getSecuela());
	    	comicSiendoAgregado.setPrecuela(comic);
	        comic.setSecuela(comicSiendoAgregado);
	        if(comicSiendoAgregado.getPrecuela()!=null){
	            comicSiendoAgregado.getPrecuela().setSecuela(comicSiendoAgregado);
	        }
	        if(cola==comic){
	            this.cola = comicSiendoAgregado;
	        }
	        this.tamano++;
	    }
	    public void agregarAntes(Comic comicSiendoAgregado, Comic comic){
	    	comicSiendoAgregado.setSecuela(comic);
	    	comicSiendoAgregado.setPrecuela(comic.getPrecuela());
	        if(comicSiendoAgregado.getPrecuela()!=null) {
	        	comicSiendoAgregado.getPrecuela().setSecuela(comicSiendoAgregado);
	        }
	    	if(cabeza==comic){
	            this.cabeza = comicSiendoAgregado;
	        } 
	        this.tamano++;
	    }
	    public Comic picoFrente(){
	        if(tamano==0){
	            throw new EmptyStackException();
	        }
	        return(this.cabeza);
	    }
	    public Comic picoAtras(){
	        if(tamano==0){
	            throw new EmptyStackException();
	        }
	        return(this.cola);
	    }
            
            public Comic buscar(String comic){
                Comic temp = this.cabeza;
                for(int i = 0; i < this.tamano; ++i){
                    if(comic.equalsIgnoreCase(temp.getNombre())) return temp;
                    temp = temp.getSecuela();
                }
                return null;
            }
            
            public void borrar(Comic comic) throws Exception{
                if(this.getTamano() == 0) throw new Exception("Tomo no tiene comics");
                else{
                    Comic temp = this.cabeza;
                    int posicion = -1;
                    for(int i = 0; i < this.getTamano(); ++i){
                        if(comic.equals(temp)){
                            posicion = i;
                        }
                    }
                    if(posicion == -1) throw new Exception("El comic que intenta borrar no pertenece a tomo");
                    else{
                        if(this.getTamano() == 1 && posicion == 1){
                            this.cabeza = null;
                            this.cola = null;
                        }
                        else if(posicion == 1){
                            this.cabeza = this.getCabeza().getSecuela();
                            this.cabeza.setPrecuela(null);
                        }
                        else if(posicion == this.tamano-1){
                            this.cola = this.cola.getPrecuela();   
                            this.cola.setSecuela(null);
                        }
                        else{
                            comic.getPrecuela().setSecuela(comic.getSecuela());
                            comic.getSecuela().setPrecuela(comic.getPrecuela());
                        }
                    }
                    
                }
                
            }

        public Editorial getEditorial() {
        return editorial;
        }

        public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
        }	   
	
	public String getNombre() {
		return nombre;
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
        
        

	public DynamicArray<Categoria> getCategorias() {
		return categorias;
	}


	public void setCategorias(DynamicArray<Categoria> categorias) {
		this.categorias = categorias;
	}


	public Lenguaje getLenguaje() {
		return lenguaje;
	}


	public void setLenguaje(Lenguaje lenguaje) {
		this.lenguaje = lenguaje;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
        

	public int getAgno_publicacion() {
		return agno_publicacion;
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

	public Comic getCabeza() {
		return cabeza;
	}

	public void setCabeza(Comic cabeza) {
		this.cabeza = cabeza;
	}

	public Comic getCola() {
		return cola;
	}

	public void setCola(Comic cola) {
		this.cola = cola;
	}

	public boolean isFinalizado() {
		return finalizado;
	}
        
	public void setFinalizado(boolean finalizado) {
		this.finalizado = finalizado;
	}

        public int getTamano() {
            return tamano;
        }
  
        @Override
        public boolean equals(Object obj) {
            Tomo tomo = (Tomo)obj;
            if(tomo.tamano != this.tamano) return false;
            else{
                Comic this_comic = this.cabeza;
                Comic tomo_comic = tomo.cabeza;
                for(int i = 0; i < tamano; ++i){
                    if(!this_comic.equals(tomo_comic)) return false;
                    this_comic = this_comic.getSecuela();
                    tomo_comic = tomo_comic.getSecuela();
                }
            }
            return  this.escritor.equals(tomo.getEscritor())            &&
                    this.dibujante.equals(tomo.dibujante)               &&
                    this.nombre.equalsIgnoreCase(tomo.getNombre())      &&
                    this.agno_publicacion == tomo.getAgno_publicacion() &&
                    this.categorias.equals(tomo.getCategorias());
        }		

    @Override
    public String toString() {
        return this.getNombre() + " " + this.getAgno_publicacion() + " " + this.getEscritor().getNombre() + " " +
                this.getDibujante().getNombre() + " " +  this.isFinalizado()+ " " +hayDescripcion();
    }
        

    public boolean hayDescripcion(){
	  	return descripcion!=null;
	}
}
