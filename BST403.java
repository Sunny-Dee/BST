/* CSC 403 Sections 901,902,910,911 Spring 2015 Homework assignment 2
 * Your assignment is to write the following:
 * 
 * (1) The remove method below.  It is called once the Node
 *     that should be removed is located, and should return
 *     the Node that replaces it.  The cases are:
 *     
 *     if x is a leaf, then return null
 *     if x only has one child, then return it
 *     if x has 2 children, then find minLeft (the leftmost descendant
 *       of x's right subtree).  Then change x.val to be minLeft.val,
 *       and replace min with its right child
 */
package csc403wk2;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

import csc403wk1.City;


public class BST403<T extends Comparable<T>> {
	// the tree consists of Node objects.  Each node stores a value,
	// and left and right children.
	private class Node<T extends Comparable<T>> {
		public T val;
		public Node<T> left;
		public Node<T> right;
		
		public Node(T val) {
			this.val = val;
			left = right = null;
		}
	}
	
	private Node<T> root=null;

	// display the tree in Python style
	public String toString() {
		if (root == null) return "[]";
		return toStringBuilder(root).toString();
	}
	
	// it is more efficient to modify StringBuilders than Strings
	public StringBuilder toStringBuilder(Node<T> x) {
		if (x == null) return new StringBuilder("[]");
		StringBuilder string = new StringBuilder("[");
		string.append(x.val);
		if (x.left == null && x.right == null)
			return string.append("]");
		string.append(" ");
		string.append(toStringBuilder(x.left));
		string.append(" ");
		string.append(toStringBuilder(x.right));
		return string.append("]");		
	}

	/* Add a value to the tree if the tree doesn't already contain it. 
	   Returns false if the tree already contains
	   the value, or true otherwise */
	public boolean add(T val) {
		// special case for the root
		if (root == null) {
			root = new Node<T>(val);
			return true;
		}
		Node<T> x = root;
		int comp = x.val.compareTo(val);
		while (comp != 0) {
			if (comp > 0)
				// comp > 0: the value is added to the left subtree
				if (x.left == null) {
					x.left = new Node<T>(val);
					return true;
				}
				else x = x.left;
			else // comp < 0:  value is added to the right subtree
				if (x.right == null) {
					x.right = new Node<T>(val);
					return true;
				}
				else x = x.right;
			comp = x.val.compareTo(val);
		}
		return false;
	}

	// returns true if the tree contains a value, or false otherwise
	public boolean contains(T val) {
		Node<T> x = root;
		while (x != null) {
			int comp = x.val.compareTo(val);
			if (comp == 0)
				return true;
			if (comp > 0) 
				x = x.left;
			else x = x.right;
		}
		return false;
	}
	
	// remove a value from the tree, if contains the value, and return true
	// if the tree doesn't contain the value, return false
	public boolean remove(T val) {
		// special case for an empty tree
		if (root == null)
			return false;
		// special case for removing the root
		int comp = root.val.compareTo(val);
		if (comp == 0)
			root = remove(root);
		Node<T> x = root;
		while (x != null) {
			Node<T> child;
			if (comp > 0) {
				// comp > 0: remove the value from x's left subtree
				child = x.left;
				if (child == null)
					return false;
				comp = child.val.compareTo(val);
				if (comp == 0) {
					// comp == 0:  child should be removed; replace it
					// with the Node returned by remove(child)
					x.left = remove(child);
					return true;
				}
			}
			else { // comp < 0:  remove the value from x's right subtree
				child = x.right;
				if (child == null)
					return false;
				comp = child.val.compareTo(val);
				if (comp == 0) {
					// comp == 0:  child should be removed; replace it
					// with the Node returned by remove(child)
					x.right = remove(child);
					return true;
				}
			}
			x = child;
		}
		return false;
	}

	// this method is called when x.val is the value to be removed from the tree
	// it returns the Node that replaces x
	public Node<T> remove(Node<T> x) {
		if (x.right == null && x.left == null) return null;
		else if(x.left == null) return x.right;
		else if(x.right == null) return x.left;
		else {
			Node<T> minLeft;
			minLeft = x.right;
			while (minLeft.left != null){
				minLeft = minLeft.left;
				
			}
			x.val = minLeft.val;
			minLeft = minLeft.right;
//			if (minLeft.right != null){ 
//					minLeft = minLeft.right;
//			}
//			else {
//				//minLeft.val = null;
//				minLeft = null;
//			}

			return x;
		}
	}


//	public static Comparator<City> countryNameComparator() {
//		 class countryNameComparator implements Comparator<City>{
//			public int compare(City city1, City city2) {
//				return city1.country.compareTo(city2.country);
//				}
//		}
	
	
	public Iterator<T> iterator(){
		class iterator implements Iterator<T> {
				private Node<T> x;
				private Stack<Node> stack;
				
				public iterator(){
					x = root;
					stack = new Stack<Node>();
					while (x.left != null){
						stack.push(x);
						x = x.left;
					}
				}
		
				public boolean hasNext() {
					if (x != null || !stack.isEmpty()) return true;
					else return false;
				}
				
	
				public T next() {
					Node<T> temp;
					Node leftmost = x.right;
					if (leftmost != null){
						while (leftmost.left != null){
							stack.push(leftmost);
							leftmost = leftmost.left; //leftmost child in right subtree
						}
						temp = x;
						x = leftmost;		
						return temp.val; 
					}
					else {
						temp = x;
						x = stack.pop();
						return temp.val;
					}
				}
			}
		
		return new iterator();
	}
	
//	public Iterator<Node> iterator(){
//		return new NodeIterator();
//	}

	
	private static int random(int max) {
		return (int) Math.floor(max*Math.random()+.99999999);
	}

	// you can use this to test your remove method
	public static void main(String[] args) {
//		BST403<Integer> tree = new BST403<Integer>();
//		for (int i=0; i<20; i++) {
//			int x = random(20);
//			System.out.println("Add " + x + " " + tree.add(x));
//		}
//		System.out.println(tree);
//		Scanner console = new Scanner(System.in);
//		for (int i=0; i<10; i++) {
//			int x = random(20);
//			System.out.println("Remove " + x + " " + tree.remove(x));
//			System.out.println(tree);
//		}
		BST403<Integer> tree = new BST403<Integer>();
		tree.add(10);
		tree.add(5);
		tree.add(12);
		tree.add(18);
		tree.add(25);
		tree.add(20);
		tree.add(21);

		System.out.println(tree);
		Scanner console = new Scanner(System.in);
		

		System.out.println("Remove 12 " + tree.remove(12));
		System.out.println(tree);
		

		Iterator<Integer> i = tree.iterator();
		  while(i.hasNext()) {
		   System.out.println(i.next());
		  }

		
	}
}