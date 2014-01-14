/* Vertex.java */

package graph;

import linked.DList;
import linked.DListNode;
import edge.Entry;

public class Vertex extends Entry {

	DList neighbors = new DList();
	private Object vertex;

	/** 
	 * Creates a null Vertex object.
	 **/
	Vertex() {
		this.value = null;
		this.key = null;
	}

	/** 
	 * Creates a vertex object given a vertex parameter.
	 **/
	Vertex (Object vertex) {
		this.setVertex(vertex);
		this.value = null;
		this.key = null;
	}

	/** 
	 * Provides a way to access a vertex from outside graph package.
	 * @return vertex object
	 **/
	public Object getVertex() {
		return vertex;
	}

	/** 
	 * Provides a way to set a vertex object from outside the graph package.
	 * @param vertex object
	 **/
	public void setVertex(Object vertex) {
		this.vertex = vertex;
	}
	
	/**
	 * Adds a neighbor to a vertex.
	 * @param object to add
	 **/
	public void addNeighbor(Object vertex) {
		neighbors.insertFront(vertex);
	}

	/**
	 * Removes a neighbor from a vertex.
	 * @param object to remove
	 **/
	public void removeNeighbor(Object vertex) {
		DListNode finger = neighbors.front();
		while (finger.item != vertex) {
			finger = finger.next;
		}
		neighbors.remove(finger);
	}

	/**
	 * Returns a string representation of the Vertex.
	 * @return String
	 **/
	public String toString() {
		return getVertex().toString();
	}

	/** 
	 * Returns hash code for a Vertex object.
	 * @return hash code
	 **/
	public int hashCode() {
		return vertex.hashCode();
	}
	
	/** 
	 * Returns true if this equals the given object.
	 * @return true if they are .equals()
	 **/
	public boolean equals(Object i) {
		if(i instanceof Vertex && vertex.equals(((Vertex) i).getVertex())) {return true;}
		return false;
	}
}
	