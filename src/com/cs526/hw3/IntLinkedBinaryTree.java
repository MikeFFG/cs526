package com.cs526.hw3;

import java.util.*;

/**
 * Class that implements a LinkedBinaryTree to create a 
 * Binary Search Tree with integers
 * 
 * @author Mike Burke
 */
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

		if (p == null) {						// tree is empty so add as root
			return addRoot(e);					// Add e as root and return the position
		}
		
		Position<Integer> x = p;				// Create temp position equal to root
		Position<Integer> y = x;				// Create another temp position equal to root
		while (x != null) {						// Loop until we hit an empty position
			if (x.getElement() == e) {			// Return null if this is a duplicate
				return null;
			} else if (x.getElement() > e) {	// If element in x is greater than new element
				y = x;							// Set y to x
				x = left(x);					// Set x to the left child of x
			} else {							// If element in x is smaller than new element
				y = x;							// Set y to x
				x = right(x);					// Set x to the right child of x
			}
		}

		// At the end of the loop, y will equal the correct last non-null position
		if (y.getElement() > e) { 				// If y is greater than e, add e as left child
			p = addLeft(y, e);
		} else {								// If y is less than e, add e as right child
			p = addRight(y, e);
		}
		
		return p;								// Return reference to new node
	}
	
	/**
	 * Add all integers passed as arguments to this tree
	 * @param a - This parameter has the integers to add
	 * @postcondition: All integers have been added
	 */
	public void addMultiple(Integer... a ){
		// Use for each loop to iterate over array and add each using add method
		for (int num : a) {
			add(this.root(), num);
		}
	}
	
	/**
	 * A simple inOrder traversal of the tree that prints each element
	 * Note that after writing this method I did notice that there is a 
	 * Tree.inOrder() that returns an iterable of the items that I could have used
	 * but I already wrote the method so decided to keep it.
	 * @param p - The position to check
	 * @param t - The IntLinkedBinaryTree to traverse
	 */
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
		IntLinkedBinaryTree t = new IntLinkedBinaryTree();
		
		// add some integers
		 t.add(t.root, 100);
		 t.add(t.root, 50);
		 t.add(t.root, 150);
		 t.add(t.root, 70);
		// test with more integers and addMultiple method
		t.addMultiple(1,1000, 47);
		
		
		// print all integers in the tree in increasing order
		// after adding above four integers, the following should be printed
		// 50 70 100 150
		System.out.println("Printing all integers in tree in increasing order: ");
		inorderPrint(t.root, t);
		
		System.out.println("\n");
		
		
		// experimentally determine the average height of a binary search tree
		// clear the tree before beginning this experiment
		// repeat 100 times
		int maxIteration = 100;
		
		// Saves the total height.
		int totalHeight = 0;
		
		for (int i= 0; i<maxIteration; i++){
			// begin with an empty tree in each iteration
			t = new IntLinkedBinaryTree();
			
			// generate 1000 random integers in the range [0, 999999]
			int[] randomInts = new Random().ints(0, 999999).distinct().limit(1000).toArray();
					
			// and add them to the tree, one at a time
			// using the add method you implemented
			for (int j = 0; j < randomInts.length; j++) {
				t.add(t.root(), randomInts[j]);
			}
			
			// Make sure the resulting tree has 1000 distinct integers
			System.out.println("Resulting tree has " + t.size() + " distinct integers.");
			
			// determine the height of the resulting tree and add to totalHeight
			totalHeight += t.height(t.root());
		}
		
		// calculate and display the average height of the 100 trees
		int averageHeight = totalHeight / maxIteration;
		System.out.println("\nThe average height of the 100 trees is " + averageHeight);
	}

}
