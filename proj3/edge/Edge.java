/* Edge.java */

package edge;

import graph.*;

public class Edge extends Entry implements Comparable {

	private Vertex v1, v2;
	private int weight;
	
	/**
	 * Creates an edge object between two vertices.
	 * @param vertex one
	 * @param vertex two
	 * @param weight of the edge
	 **/
	public Edge(Vertex v1, Vertex v2, int weight) {
		this.setV1(v1);
		this.setV2(v2);
		this.setWeight(weight);
	}

	/**
	 * This method ensures that no two edges will have the same hashcode.
	 * @return int hashcode
	 **/
	public int hashCode() {
	    if (v1.equals(v2)) {
	      return v1.hashCode() + 1;
	    } else {
	      return v1.hashCode() + v2.hashCode();
	    }
	  }


	/** 
	 * Returns true if the two objects are the same.
	 * @param v1
	 * @param v2
	 * @return true or false
	 **/
	public boolean isSame(Object v1, Object v2) {
		return (v1 == this.getV1().getVertex() && v2 == this.getV2().getVertex()) || (v2 == this.getV1().getVertex() && v1 == this.getV2().getVertex());
	}
	
	/**
	 * Provides a way to access instance variable v1 from outside edge package.
	 * @return v1
	 **/	
	public Vertex getV1() {
		return v1;
	}

	/**
	 * Provides a way to set instance variable v1 from outside edge package.
	 * @param v1 is the new v1
	 **/
	public void setV1(Vertex v1) {
		this.v1 = v1;
	}

	/**
	 * Provides a way to access instance variable v2 from outside edge package.
	 * @return v2
	 **/
	public Vertex getV2() {
		return v2;
	}

	/**
	 * Provides a way to set instance variable v2 from outside edge package.
	 * @param v2 is the new v2
	 **/
	public void setV2(Vertex v2) {
		this.v2 = v2;
	}

	/**
	 * Provides a way to access the instance variable weight from outside edge package.
	 * @return integer weight
	 **/
	public int getWeight() {
		return weight;
	}

	/**
	 * Provides a way to set instance variable weight from outside graph package.
	 * @param weight is the new weight
	 **/
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	/**
	 * Print a representation of an edge object.
	 * @return String representation
	 **/
	public String toString() {
		return v1 + " " + v2 + ":" + weight;
	}

	/**
	 * Checks if two Edge objects are the same.
	 * @
	 **/
	public boolean equals(Object i) {
		if ((i instanceof Edge) && (v1.equals(((Edge) i).getV1())) && v2.equals(((Edge) i).getV2()) || (v2.equals(((Edge) i).getV1())) && v1.equals(((Edge) i).getV2())) {return true;}
		return false;
	}
	
	/**
	 * Overrides the comparable method compareTo. Used in ListSorts for sorting edges in a queue.
	 * @param Object i, we used edge objects.
	 * @return int of normal compareTo function.
	 **/
	@Override
	public int compareTo(Object i) {
		if(i.equals(this)) {return 0;}
		if(i instanceof Edge) {
			if(this.equals(i) && this.weight == ((Edge) i).getWeight()) { return 0; } //should be an undirected graph but it can't hurt to check
			else if(this.weight < ((Edge) i).getWeight()) { return -1; }
			else if(this.weight > ((Edge) i).getWeight()) { return 1; }
			else if (this.weight == ((Edge) i).getWeight()) { return 0; }
		}
		return -2;
	}
	
}
	