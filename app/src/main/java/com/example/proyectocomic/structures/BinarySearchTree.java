package com.example.proyectocomic.structures;

import android.util.Log;

import java.util.ArrayList;

public class BinarySearchTree<T extends Comparable<T>> {
	
	public BinaryTreeNode<T> root;
	public DynamicArray<T> inOrdered;
	
	public BinarySearchTree(T key)
	{
		this(new BinaryTreeNode<T>(key));
	}
	
	public BinarySearchTree(BinaryTreeNode<T> root)
	{
		this.root = root;
	}
	
	public BinarySearchTree()
	{
		this.root = null;
	}
	
	public int height(BinaryTreeNode<T> node)
	{
		if (node == null) 
            return 0; 
  
        return node.height; 
	}
	
	public BinaryTreeNode<T> findMin(BinaryTreeNode<T> node) 
	{
		if(isEmpty()) return null;
		if(node.leftChild == null) return node;
		else return findMin(node.leftChild);
	}
	
	public BinaryTreeNode<T> findMax(BinaryTreeNode<T> node)
	{
		if(isEmpty()) return null;
		if(node.rightChild == null) return node;
		else return findMax(node.rightChild);
	}	
	
	public BinaryTreeNode<T> Find(T key, BinaryTreeNode<T> R) 
	{
		if(isEmpty()) return null;
		
		if(R.key.equals(key)) return R;
		else if(R.key.compareTo(key) > 0) {
			if(R.leftChild != null) return Find(key, R.leftChild);	
		}
		else {
			if(R.rightChild != null) return Find(key, R.rightChild);
		}
		return R;
	}
	
	public BinaryTreeNode<T> Insert(T key, BinaryTreeNode<T> R)
	{

	 if(R == null) 
		 {
		 return new BinaryTreeNode(key);
		 }
	 
	 if(R.key.compareTo(key) > 0)
	 {
		 R.leftChild = Insert(key, R.leftChild);
	 }
	 else if(R.key.compareTo(key) < 0)
	 {
		 R.rightChild = Insert(key,R.rightChild);
	 }
	 else 
	 {
		 R.numberOfInsertions++;
		 return R;
	 }
	 adjustHeight(R);
	 
	 int treeBalance = this.treeBalance(R);
	 
	 if (treeBalance > 1 && key.compareTo(R.leftChild.key) < 0) 
         return singleRotationRight(R); 

     if (treeBalance < -1 && key.compareTo(R.rightChild.key) > 0) 
         return singleRotationLeft(R); 

     if (treeBalance > 1 && key.compareTo(R.leftChild.key) > 0) { 
         R.leftChild = singleRotationLeft(R.leftChild); 
         return singleRotationRight(R); 
     } 

     if (treeBalance < -1 && key.compareTo(R.rightChild.key) < 0) { 
         R.rightChild = singleRotationRight(R.rightChild); 
         return singleRotationLeft(R); 
     } 
	 
	 return R;
	 
	}
	
	private int treeBalance(BinaryTreeNode<T> n)
	{
		if(n == null) return 0;
		return height(n.leftChild) - height(n.rightChild);
	}

	
	private void adjustHeight(BinaryTreeNode<T> n)
	{
		int l = 0;
		int r = 0;
		if(n.leftChild != null) l = n.leftChild.height;
		if(n.rightChild != null) r = n.rightChild.height;
		
		n.height = 1 + Math.max(l, r);
	}
	
	private BinaryTreeNode<T> singleRotationRight(BinaryTreeNode<T> n) 
	{
		BinaryTreeNode<T>  left = n.leftChild;
		
		BinaryTreeNode<T>  temp = left.rightChild;
		
		left.rightChild = n;
		
		n.leftChild = temp;
		
		adjustHeight(n);
		adjustHeight(left);
		return left;
	}
	
	private BinaryTreeNode<T> singleRotationLeft(BinaryTreeNode<T> n)
	{
		BinaryTreeNode<T>  right = n.rightChild;
		
		BinaryTreeNode<T>  temp = right.leftChild;
		
		right.leftChild = n;
		
		n.rightChild = temp;
		
		adjustHeight(n);
		adjustHeight(right);
		return right;		
	}
	
	public boolean isEmpty()
	{
		return root == null;
	}
	
	public int numberNodes(BinaryTreeNode<T> n)
	{
		if(n == null) return 0;
		else {
			return 1 + numberNodes(n.leftChild) + numberNodes(n.rightChild);
		}
	}

	public DynamicArray<T> inOrder(){
		this.inOrdered = new DynamicArray<T>();
		inOrderTraversal(this.root);
		return this.inOrdered;
	}

	public void inOrderTraversal(BinaryTreeNode<T> node){
		if (node==null){return;}
		inOrderTraversal(node.leftChild);
		Log.d("manzana",node.key.toString());
		inOrdered.pushBack(node.key);
		inOrderTraversal(node.rightChild);
	}
}