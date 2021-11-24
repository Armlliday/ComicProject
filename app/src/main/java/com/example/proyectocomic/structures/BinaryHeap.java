package com.example.proyectocomic.structures;
public class BinaryHeap<T extends Comparable<T>> implements Cloneable {
	
	 public int size;
	 protected int capacity;
	 protected Object[] array;
	 

	 public BinaryHeap(int capacity) {
		this.capacity = capacity;
		this.size = 0; 
		this.array = new Object[capacity+1]; //array[0] no tiene datos.
	}
	 
	 public BinaryHeap() {
			this(1);
		}


		public Object clone() throws
			CloneNotSupportedException
	{
		return super.clone();
	}

	public void enlargeArray()
	 {
		 if(size + 1 == capacity){
		 
	         Object[] temp_array = new Object[(capacity * 2)+1];
	         
	         for(int i = 0; i < size; ++i){
	             temp_array[i+1] = array[i+1];
	         }
	         array =  temp_array;
	         capacity = (capacity*2);
		 }
	             
	}

	public void insert(T key)
	{
		enlargeArray();
		int pos = ++size;
		array[pos] = key;
		try {
			percolateUp(pos);
		} catch (Exception e) {System.out.println("Error al momento de insertar a " + key);
                                       System.out.println(e.getMessage());}
		
	}
	
	public T getMax() throws Exception {
		if(isEmpty()) throw new  Exception("No hay elementos en el monticulo");
		return  (T)array[1]; 
	}
	
	public boolean isEmpty()
	{
		return size == 0;
	}
	
	public T extractMax() throws Exception
	{
		if(isEmpty()) throw new  Exception("No hay elementos en el monticulo");
		
		T res = getMax();
		int pos = size--;
		if(size >= 1)
		{
			array[1] = array[pos];
			percolateDown(1);
		}
		else array[1] = null;
		
		return res;
	}
	
	private void percolateDown(int pos) throws Exception
	{
		if(isEmpty()) throw new  Exception("No hay elementos en el monticulo");
		else if(pos > size || pos <= 0) throw new  Exception("No existe un elemento a percolar para esa posici�n");
		
		T temp = (T)array[pos];
		int child;
		while(pos*2 <= size)
		{
			child = pos*2;
			
			if(child < size && ((T)array[child+1]).compareTo((T)array[child]) > 0) ++child;
			if(((T)array[child]).compareTo(temp) > 0)
			{
				array[pos] = array[child];
			}
			else break;
			pos = child;
		}
		array[pos] = temp;
	}
	
	private void percolateUp(int pos) throws Exception
	{
		if(isEmpty()) throw new  Exception("No hay elementos en el monticulo");
		else if(pos > size || pos <= 0) throw new  Exception("No existe un elemento a percolar para esa posici�n");
		
		T temp = (T)array[pos];
		
		while(pos/2 >= 1)
		{
			int father = pos/2;
			
			if(temp.compareTo(((T)array[father])) > 0) 
			{
				array[pos] = array[father];
                               pos = father;
			}
			else break;
			
		}
		array[pos] = temp;
	}
}

