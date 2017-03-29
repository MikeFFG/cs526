/*
 * Copyright 2014, Michael T. Goodrich, Roberto Tamassia, Michael H. Goldwasser
 *
 * Developed for use with the book:
 *
 *    Data Structures and Algorithms in Java, Sixth Edition
 *    Michael T. Goodrich, Roberto Tamassia, and Michael H. Goldwasser
 *    John Wiley & Sons, 2014
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * ******** New Methods are at the bottom of class **********
 */
package com.cs526.hw3;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Realization of a list by means of a dynamic array. This is a simplified version
 * of the java.util.ArrayList class.
 *
 * @author Michael T. Goodrich
 * @author Roberto Tamassia
 * @author Michael H. Goldwasser
 */
public class ArrayList<E> implements List<E> {
  // instance variables
  /** Default array capacity. */
  public static final int CAPACITY=16;     // default array capacity

  /** Generic array used for storage of list elements. */
  private E[] data;                        // generic array used for storage

  /** Current number of elements in the list. */
  private int size = 0;                    // current number of elements
  
  /** Holds the current capacity information of the array */
  private int currentCapacity;

  // constructors
  /** Creates an array list with default initial capacity. */
  public ArrayList() { this(CAPACITY); }   // constructs list with default capacity

  /** Creates an array list with given initial capacity. */
  @SuppressWarnings({"unchecked"})
  public ArrayList(int capacity) {         // constructs list with given capacity
    data = (E[]) new Object[capacity];     // safe cast; compiler may give warning
    currentCapacity = capacity;			   // saves the current capacity of the ArrayList
  }

  // public methods
  /**
   * Returns the number of elements in the list.
   * @return number of elements in the list
   */
  public int size() { return size; }

  /**
   * Tests whether the array list is empty.
   * @return true if the array list is empty, false otherwise
   */
  public boolean isEmpty() { return size == 0; }

  /**
   * Returns (but does not remove) the element at index i.
   * @param  i   the index of the element to return
   * @return the element at the specified index
   * @throws IndexOutOfBoundsException if the index is negative or greater than size()-1
   */
  public E get(int i) throws IndexOutOfBoundsException {
    checkIndex(i, size);
    return data[i];
  }

  /**
   * Replaces the element at the specified index, and returns the element previously stored.
   * @param  i   the index of the element to replace
   * @param  e   the new element to be stored
   * @return the previously stored element
   * @throws IndexOutOfBoundsException if the index is negative or greater than size()-1
   */
  public E set(int i, E e) throws IndexOutOfBoundsException {
    checkIndex(i, size);
    E temp = data[i];
    data[i] = e;
    return temp;
  }

  /**
   * Inserts the given element at the specified index of the list, shifting all
   * subsequent elements in the list one position further to make room.
   * @param  i   the index at which the new element should be stored
   * @param  e   the new element to be stored
   * @throws IndexOutOfBoundsException if the index is negative or greater than size()
   */
  public void add(int i, E e) throws IndexOutOfBoundsException {
    checkIndex(i, size + 1);
    if (size == data.length)               // not enough capacity
      ensureCapacity(2 * data.length);     // so double the current capacity
    for (int k=size-1; k >= i; k--)        // start by shifting rightmost
      data[k+1] = data[k];
    data[i] = e;                           // ready to place the new element
    size++;
  }

  /**
   * Removes and returns the element at the given index, shifting all subsequent
   * elements in the list one position closer to the front.
   * @param  i   the index of the element to be removed
   * @return the element that had be stored at the given index
   * @throws IndexOutOfBoundsException if the index is negative or greater than size()
   */
  public E remove(int i) throws IndexOutOfBoundsException {
    checkIndex(i, size);
    E temp = data[i];
    for (int k=i; k < size-1; k++)         // shift elements to fill hole
      data[k] = data[k+1];
    data[size-1] = null;                   // help garbage collection
    size--;
    return temp;
  }

  // utility methods
  /** Checks whether the given index is in the range [0, n-1]. */
  protected void checkIndex(int i, int n) throws IndexOutOfBoundsException {
    if (i < 0 || i >= n)
      throw new IndexOutOfBoundsException("Illegal index: " + i);
  }

  /** Resizes internal array to have given capacity >= size. */
  @SuppressWarnings({"unchecked"})
  protected void resize(int capacity) {
    E[] temp = (E[]) new Object[capacity];     // safe cast; compiler may give warning
    for (int k=0; k < size; k++)
      temp[k] = data[k];
    data = temp;                               // start using the new array
    currentCapacity = capacity;				   // Update instance variable
  }

  //---------------- nested ArrayIterator class ----------------
  /**
   * A (nonstatic) inner class. Note well that each instance contains an implicit
   * reference to the containing list, allowing it to access the list's members.
   */
  private class ArrayIterator implements Iterator<E> {
    /** Index of the next element to report. */
    private int j = 0;                   // index of the next element to report
    private boolean removable = false;   // can remove be called at this time?

    /**
     * Tests whether the iterator has a next object.
     * @return true if there are further objects, false otherwise
     */
    public boolean hasNext() { return j < size; }   // size is field of outer instance

    /**
     * Returns the next object in the iterator.
     *
     * @return next object
     * @throws NoSuchElementException if there are no further elements
     */
    public E next() throws NoSuchElementException {
      if (j == size) throw new NoSuchElementException("No next element");
      removable = true;   // this element can subsequently be removed
      return data[j++];   // post-increment j, so it is ready for future call to next
    }

    /**
     * Removes the element returned by most recent call to next.
     * @throws IllegalStateException if next has not yet been called
     * @throws IllegalStateException if remove was already called since recent next
     */
    public void remove() throws IllegalStateException {
      if (!removable) throw new IllegalStateException("nothing to remove");
      ArrayList.this.remove(j-1);  // that was the last one returned
      j--;                         // next element has shifted one cell to the left
      removable = false;           // do not allow remove again until next is called
    }
  } //------------ end of nested ArrayIterator class ------------

  /**
   * Returns an iterator of the elements stored in the list.
   * @return iterator of the list's elements
   */
  @Override
  public Iterator<E> iterator() {
    return new ArrayIterator();     // create a new instance of the inner class
  }

  /**
   * Produces a string representation of the contents of the indexed list.
   * This exists for debugging purposes only.
   *
   * @return textual representation of the array list
   */
  public String toString() {
    StringBuilder sb = new StringBuilder("(");
    for (int j = 0; j < size; j++) {
      if (j > 0) sb.append(", ");
      sb.append(data[j]);
    }
    sb.append(")");
    return sb.toString();
  }
  
  public void printList(){
	  System.out.println();
	  for (int i=0; i<size; i++){
		  System.out.print(get(i) + " ");
	  }
	  System.out.println();
  }
  
  public static void main(String[] args) {
	  
	  // Original Tests
	  System.out.println("Some basic tests...");
	  ArrayList<String> stringList = new ArrayList<>(10);
	  ArrayList<String> secondList = new ArrayList<>(10);
	  
	  stringList.add(0, "Java");
	  stringList.add(0, "with");
	  stringList.add(0, "Structure");
	  stringList.add(0, "Data");
	  stringList.printList();
	  
	  // Test we dynamically raise size
	  System.out.println("\n\nTest we dynamically double size when capacity is reached...");
	  ArrayList<String> smallList = new ArrayList<>(2);
	  System.out.println(smallList.currentCapacity);
	  smallList.add(0, "zero");
	  smallList.add(1, "one");
	  smallList.add(2, "two");
	  System.out.println(smallList.currentCapacity);
	  
	  // Test addAll(ArrayList<E>) method
	  System.out.println("\n\nTest the addAll method...");
	  secondList.add(0, "Test");
	  secondList.addAll(stringList);
	  secondList.printList();
	  
	  // Test addAll method properly doubles capacity
	  ArrayList<Integer> listOfTwenty = new ArrayList<>(20);
	  for (int i = 0; i < 15; i++) {
		  listOfTwenty.add(i, i);
	  }
	  ArrayList<Integer> listOfTen = new ArrayList<>(10);
	  for (int i = 0; i < listOfTen.currentCapacity; i++) {
		  listOfTen.add(i, i);
	  }
	  listOfTwenty.addAll(listOfTen);
	  listOfTwenty.printList();
	  System.out.println(listOfTwenty.currentCapacity);
	  
	  // Test ensureCapacity(int)
	  System.out.println("\n\nTest the ensureCapacity method...");
	  System.out.println(secondList.currentCapacity);
	  secondList.ensureCapacity(50);
	  System.out.println(secondList.currentCapacity);
	  
	  // Test remove(E)
	  System.out.println("\n\nTest the remove method...");
	  stringList.remove("with");
	  stringList.printList();
	  stringList.add(0, "with");
	  stringList.add(4, "with");
	  stringList.printList();
	  stringList.remove("with");
	  stringList.printList();
	  
	  // Test removeRange(int, int)
	  System.out.println("\n\nTest the removeRange method...");
	  ArrayList<Integer> intList = new ArrayList<>(10);
	  for (int i = 0; i < 10; i++) {
		  intList.add(i, i);
	  }
	  intList.printList();
	  intList.removeRange(2, 4);
	  intList.printList();
	  intList.removeRange(4, 6);
	  intList.printList();
	  
	  // Test trimToSize()
	  System.out.println("\n\nTest the trimToSize method...");
	  System.out.println(intList.currentCapacity);
	  intList.trimToSize();
	  System.out.println(intList.currentCapacity);
  }
  
  /* NEW METHODS */
  /**
   * Adds an ArrayList to the end of the current ArrayList
   * @param l - the other ArrayList to add to the current one
   */
  public void addAll(ArrayList<E> l) {
	  Iterator<E> iterator = l.iterator();
	  
	  if (size() + l.size() > currentCapacity) {
		  ensureCapacity((size() + l.size()) * 2);
	  }
	  
	  while(iterator.hasNext()) {
		 add(size(), iterator.next());
	  }
  }
  
  /**
   * Using the resize method, ensures the array has the capacity
   * specified in the argument. If the argument is less than the current
   * size, ArrayList remains unchanged.
   * @param minCapacity
   */
  public void ensureCapacity(int minCapacity) {
	  if (minCapacity > currentCapacity) {
		  resize(minCapacity);
	  }
  }
  
  /**
   * Removes the first occurence of a specific item in the ArrayList.
   * Note that this is not an index, but the actual item.
   * @param e - the item to be deleted
   * @return true if item is found and deleted. False otherwise
   */
  public boolean remove(E e) {
	  Iterator<E> iterator = iterator();
	  
	  while(iterator.hasNext()) {
		  if (iterator.next().equals(e)) {
			  iterator.remove();
			  return true;
		  }
	  }
	  return false;
  }
  
  /**
   * Removes a range of items in the ArrayList
   * @param fromIndex - index of first item to delete
   * @param toIndex - index to stop at. Note this is not deleted (exclusive range)
   */
  public void removeRange(int fromIndex, int toIndex) throws IndexOutOfBoundsException {
	  // Check that we have two valid indexes
	  checkIndex(fromIndex, size());
	  checkIndex(toIndex, size());
	  
	  // Check whether we actually have a valid range
	  if (toIndex < fromIndex) {
		  throw new IllegalArgumentException();
	  }
	  
	  for(int i = fromIndex; i < toIndex; i++) {
		  remove(fromIndex);
	  }
  }
  
  /**
   * Uses the resize(int) method to trim to the current size by passing in 
   * size() as the argument.
   */
  public void trimToSize() {
	  resize(size());
  }
   
}
