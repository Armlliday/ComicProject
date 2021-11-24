package com.example.proyectocomic.comics;


import com.example.proyectocomic.structures.Node;
import com.example.proyectocomic.structures.SinglyLinkedList;

public class Hashmap<String,V> {
    public SinglyLinkedList<Object[]>[] mapa;
    public int n=3;
    public Hashmap(){
        this.mapa = new SinglyLinkedList[n];
        for(int i=0;i<n;i++){
            mapa[i] = new SinglyLinkedList<>();
        }
    }
    public boolean hasKey(java.lang.String O){
        SinglyLinkedList L = this.mapa[h(O)];
        Object[] temp = null;
        Node tempNode = null;
        if(!L.empty()){
            tempNode = L.head;
            temp = (Object[]) tempNode.key;
            String ss = (String)temp[0];
            if(ss.equals((String) O)) return true;
        }

        for(int j = 0; j<L.size()-1;j++){
            tempNode = tempNode.next;
            temp = (Object[]) tempNode.key;
            String s = (String)temp[0];
            if(s.equals((String) O)) return true;
        }
        return false;
    }

    public V get(java.lang.String O){
        SinglyLinkedList L = this.mapa[h(O)];
        Object[] temp = null;
        Node tempNode = null;
        if(!L.empty()){
            tempNode = L.head;
            temp = (Object[]) tempNode.key;
            String ss = (String)temp[0];
            if(ss.equals((String) O)) return (V) temp[1];
        }


        for(int j = 0; j<L.size()-1;j++){
            tempNode = tempNode.next;
            temp = (Object[]) tempNode.key;
            String s = (String)temp[0];
            if(s.equals((String) O)) return (V) temp[1];

        }
        return null;
    }

    public void set(java.lang.String O,V V){
        SinglyLinkedList L = this.mapa[h(O)];
        Object[] temp = null;
        Node<Object[]> tempNode = null;
        if(!L.empty()){
            tempNode = L.head;
            temp = (Object[]) tempNode.key;
            String ss = (String)temp[0];
            if(ss.equals((String) O)) return;
        }
        for(int j = 0; j<L.size()-1;j++){
            tempNode = tempNode.next;
            temp = (Object[]) tempNode.key;
            String s = (String)temp[0];
            if(s.equals((String) O)) return;
        }
        Object[] adding = new Object[2];
        adding[0]=O; adding[1]=V;
        L.pushBack(adding);
    }

    public int h(java.lang.String s) {
        int hashing = 0;
        for (int i = 0; i < s.length(); i++)
            hashing = (31 * hashing + s.charAt(i)) % this.n;
        return hashing;
    }

}
