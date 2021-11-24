package com.example.proyectocomic.structures;

public class DynamicArray<T>{

    public Object[] array;
    public int size;
    public int capacity;
    
    public DynamicArray(){
        array = new Object[2];
        size = 0;
        capacity = 2;
    }
    
    public T get(int i){
        if(i >= size || i < 0) throw new IndexOutOfBoundsException("El numero supera el tamaño del arreglo");
        return (T)array[i];
    }
    
    public void set(int i, T value){
         if(i >= size || i < 0) throw new IndexOutOfBoundsException("El numero supera el tamaño del arreglo");
         array[i] = value;
    }
    
    public void pushBack(T value)
    {
        if(size == capacity){
            Object[] temp_array = new Object[capacity * 2];
            
            for(int i = 0; i < size; ++i){
                temp_array[i] = array[i];
            }
            array =  temp_array;
            capacity *=2;
                
        }
        array[size] = value;
        ++size;
        
    }
    
    public boolean pushBackCheck(T value) //Revisa si lo que quiere agregar ya existe o no
    {
        boolean existe = false;
        
        for(int i = 0; i < size; ++i){
            if(((T)array[i]).equals(value)){
                existe = true;
                break;
            }
        }
        if(!existe) pushBack(value);
        
        return existe;
    }
    
    public void remove(int i){
        if(i >= size || i < 0) throw new IndexOutOfBoundsException("El número supera el tamaño del arreglo");
        
        for(int j = i; j < size - 1; ++j){
            array[j] = array[j+1];
        }
        --size;
    }
    
    public int getSize(){
        return size;
    }

    @Override
    public boolean equals(Object obj) {
       DynamicArray<T> array = (DynamicArray<T>)obj;
       if(array.getSize() != this.size) return false;
       else{
           for(int i = 0; i < this.size; ++i){
               if(!this.get(i).equals(array.get(i))) return false;
           }
           return true;
       }
    }
    
    @Override
    public String toString() {
    	String res = "";
    	if(this.size > 0)
    	{
    		for(int i = 0; i < this.size;++i)
        	{
        		res += this.get(i).toString() + " ";
        	}
    	}
    	
    	return res;
    }


    public void clear() {
        this.array = new Object[2];
        this.size = 0;
        this.capacity = 20;
    }

    public void addAll(DynamicArray<T> items) {
        for(int i=0;i<items.getSize();i++){
            this.pushBack((T)items.get(i));
        }
    }
}
