package com.example.proyectocomic.structures;

import java.util.EmptyStackException;

public class Queue<T> implements Cloneable {

	protected Node<T> head;
    protected Node<T> tail;
    public int size;

    public Queue()
    {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

	public Object clone() throws
			CloneNotSupportedException
	{
		return super.clone();
	}


    public boolean empty()
    {
       return(size==0);
    }


    public T top(){
        if(this.empty()){
            throw new EmptyStackException();
        }
        return(this.head.key);
    }

    public void enqueue(T key){
    	Node node = new Node(key,null);
    	Node temp = this.head;
    	if(temp == null)
    	{
    		this.head = node;
    	}
    	else {
    		while(temp.next != null) temp = temp.next;
        	temp.next = node;
    	}
    	
    	this.size++;
	}

    public T pop(){
        	if(size==0){
       		 throw new EmptyStackException();
    	}
    
    	Node oldHead = this.head;
    	this.head = head.next;
    	if(head==null){
       		this.tail = null;
   		}
    	this.size--;
    	return (T)oldHead.key;
   	}
}
