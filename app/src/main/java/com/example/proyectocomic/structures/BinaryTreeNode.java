package com.example.proyectocomic.structures;


public class BinaryTreeNode<T extends Comparable<T>> {
	public T key;
	BinaryTreeNode<T> leftChild;
	BinaryTreeNode<T> rightChild;
	int height;
	int numberOfInsertions;
	
	public BinaryTreeNode(T key, BinaryTreeNode<T> leftChild,BinaryTreeNode<T> rightChild) {
		this.key = key;
		this.height = 1;
		this.numberOfInsertions = 1;
	}
	
	public BinaryTreeNode(T key) {
		this(key,null,null);
	}	
	
}