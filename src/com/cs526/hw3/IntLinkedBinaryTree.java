package com.cs526.hw3;

import java.util.*;

public class IntLinkedBinaryTree extends LinkedBinaryTree<Integer>{

	// define necessary instance variables and methods, including a constructor(s)
	
	/**
	 * Add a new node with e to the tree rooted at position p
	 * @param p The root of the tree to which new node is added
	 * @param e The element of the new node
	 * @return If a node with e does not exist, a new node with e is added and 
	 *   reference to the node is returned. If a node with e exists, null is returned.
	 */
	public Position<Integer> add(Position<Integer> p, Integer e){

		if (p == null) {					// tree is empty so add as root
			return addRoot(e);				// Add e as root and return the position
		}
		
		Position<Integer> x = p;
		Position<Integer> y = x;
		while (x != null) {
			if (x.getElement() == e) {
				return null;
			} else if (x.getElement() > e) {
				y = x;
				x = left(x);
			} else {
				y = x;
				x = right(x);
			}
		}
		
		Node<Integer> temp = createNode(e, null, null, null); 
		if (y.getElement() > e) {
			addLeft(y, e);
		} else {
			addRight(y, e);
		}
		
		return p;
	}
	
	/**
	 * Add all integers passed as arguments to this tree
	 * @param a This formal parameter has the passed integers
	 * @postcondition: All integers have been added
	 */
	public void addMultiple(Integer... a ){
		
		// implement this method

		
	}
	
	public static void inorderPrint(Position<Integer> p, IntLinkedBinaryTree t) {
		if (t.left(p) != null) {
			inorderPrint(t.left(p), t);
		}
		
		System.out.print(p.getElement() + " ");
		
		if (t.right(p) != null) {
			inorderPrint(t.right(p), t);
		}
	}
	
	public static void main(String[] args) {
		
		// create a new binary tree instance
		IntLinkedBinaryTree t =   new IntLinkedBinaryTree();
		
		// add some integers
		 t.add(t.root, 100);
		 t.add(t.root, 50);
		 t.add(t.root, 150);
		 t.add(t.root, 70);
		// test with more integers and addMultiple method
		
		
		
		// print all integers in the tree in increasing order
		// after adding above four integers, the following should be printed
		// 50 70 100 150
		inorderPrint(t.root, t);
		
		
		
		// experimentally determine the average height of a binary search tree
		// clear the tree before beginning this experiment
		// repeat 100 times
		
		int maxIteration = 100;
		
		for (int i= 0; i<maxIteration; i++){
			
			// begin with an empty tree in each iteration
			// generate 1000 random integers in the range [0, 999999]
			// and add them to the tree, one at a time
			// using the add method you implemented
			// make sure the resulting tree has 1000 distinct integers
			// determine the height of the resulting tree
			
		}
		
		// calculate and display the average height of the 100 trees
	}

}
