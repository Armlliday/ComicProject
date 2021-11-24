package com.example.proyectocomic.structures;


import java.util.EmptyStackException;

public class SinglyLinkedList <T> {
    public Node head;
    protected Node tail;
    protected int size;
    
    public SinglyLinkedList()
    {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }
    
    public boolean empty()
    {
        return(size==0);
    }
    
    public int size()
    {
        return(size);
    }
    
    public void pushFront(T key)
    {
        Node node = new Node(key,head);
        this.head = node;
        if(tail==null){
            this.tail = head;
        }
        this.size++;
    }
    
    public T popFront()
    {
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
    
    public void pushBack(T key)
    {
        Node node = new Node(key,null);
        if(tail==null){
            this.head = node;
            this.tail = node;
        } else{
            this.tail.next = node;
            this.tail = node;
        }
        this.size++;
    }
    
    public T popBack()
    {
        if(size==0){
            throw new EmptyStackException();
        }
        Node oldTail = this.tail;
        if(head==tail){
            this.head = null;
            this.tail = null;
        } else{
            Node node = head;
            while(node.next.next!=null){
                node = node.next;
            }
            node.next = null;
            this.tail = node;
        }
        this.size--;
        return (T)oldTail.key;
    }
    
    public void addAfter(T key, Node node1){
        Node node2 = new Node(key,node1.next);
        node1.next = node2;
        if(tail==node1){
            this.tail = node2;
        }
        this.size++;
    }
    public void addBefore(T key, Node node2){
        Node node1 = new Node(key,node2);
        if(head==node2){
            this.head = node1;
        } else{
            Node node = head;
            while(node.next!=node2){
                node = node.next;
            }
            node.next = node1;
        }
        this.size++;
    }
    public T peekFront(){
        if(size==0){
            throw new EmptyStackException();
        }
        return(T) (this.head.key);
    }
    public T peekBack(){
        if(size==0){
            throw new EmptyStackException();
        }
        return(T) (this.tail.key);
    }
}
