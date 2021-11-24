package com.example.proyectocomic.structures;


public class Node <T> {
    public T key;
    
    public Node next;
    
    public Node(T key, Node next)
    {
        this.key = key;
        this.next = next;
    }
    
    public Node(T key)
    {
        this(key, null);
    }
    
    public Node()
    {
        this(null, null);
    }
}
