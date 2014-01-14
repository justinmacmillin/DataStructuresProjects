/* KruskalTable.java */


import edge.Entry;
import graph.Vertex;
import linked.*;

public class KruskalTable {

	DList[] table;
	int size;
  int totalItems;

  /** 
   *  Construct a new empty Kruskal hash table intended to hold roughly sizeEstimate
   *  entries.
   **/
  public KruskalTable(int sizeEstimate) {
  	int estimate = sizeEstimate + sizeEstimate/2;
  	while (!prime(estimate)) {
  		estimate++;
  	}
  	table = new DList[estimate];
  	size = estimate;
  }

  /** 
   *  Construct a new empty hash table with a default size.
   **/
  public KruskalTable() {
    size = 101;
    table = new DList[101];
  }

  /**
   *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
   *  to a value in the range 0...(size of hash table) - 1.
   *
   *  This function should have package protection (so we can test it), and
   *  should be used by insert, find, and remove.
   **/
  int compFunction(int code) {
    while (code < 0) {
      code = code + table.length;
    }
    return Math.abs(((6 * code + 11) % 16127) % table.length);
  }

  /**
   * Returns true if the number is a prime.
   * @param number
   * @return true if prime
   **/
	boolean prime (int num) {
		for (int i = 2; i < num/2; i++) {
			if (num % i == 0) {
				return false;
			}
		}
		return true;
	}

  /** 
   *  Returns the number of entries stored in the dictionary.  Entries with
   *  the same key (or even the same key and value) each still count as
   *  a separate entry.
   *  @return number of entries in the dictionary.
   **/
  public int size() {
    return size;
  }

  /** 
   *  Tests if the dictionary is empty.
   *
   *  @return true if the dictionary has no entries; false otherwise.
   **/
  public boolean isEmpty() {
  	if (size == 0) {
      	return true;
  	}
  	return false;
  }

  /**
   * Resizes the table only if the load factor is less than 0.25 or 1.
   * @return resized EdgeTable
   **/
  public KruskalTable resize() {
    double loadFactor = (this.totalItems/this.size);
    int halfLoad = (this.size)/2;
    int doubleLoad = (this.size)*2;
    KruskalTable newTable = new KruskalTable(this.size);
    if (loadFactor < 0.25 || loadFactor >= 1) {
      if (loadFactor < 0.25) {
        newTable = new KruskalTable(halfLoad);
      }
      if (loadFactor >= 1) {
        newTable = new KruskalTable(doubleLoad);
      }
      for (DList i : this.table) {
        if (i != null && i.getSize() > 0) {
          DListNode current = (DListNode) i.front();
          for (int n = 0; n < i.getSize(); n++) {
            ((KruskalTable)newTable).insert((Vertex)current.item, ((Entry) current.item).value());
            current = current.next;
          }
        }
      }
    }
    return ((KruskalTable)newTable);
  }

  /**
   *  Create a new Vertex object referencing the input key and associated value,
   *  and insert the entry into the dictionary.  Return a reference to the new
   *  entry.  Multiple entries with the same key (or even the same key and
   *  value) can coexist in the dictionary.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the key by which the entry can be retrieved.
   *  @param value an arbitrary object.
   *  @return an entry containing the key and value.
   **/
  public Entry insert(Vertex v, Object value) {
   if (this.totalItems > 0 && this.size > 0) {
      int loadFactor = (this.totalItems/this.size);
      if (loadFactor >= 1) {
        this.resize();
      }
    } 
	
  	int index = compFunction(v.hashCode());
  	DList ind = table[index];
  	if (ind == null) {
  		table[index] = new DList();
  		table[index].insertFront(v);
  	} else {
  		ind.insertFront(v);
  	}
    totalItems++;
    v.setVal(value);
    v.setKey(v);
  	return v;
  }

  /**
   * Inserts an Entry vertex object.
   * @param vertex to insert
   * @return entry object inserted.
   **/
  public Entry insert(Vertex v) {
    return this.insert(v, null);
  } 

  /** 
   *  Search for an entry with the specified key.  If such an entry is found,
   *  return it; otherwise return null.  If several entries have the specified
   *  key, choose one arbitrarily and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   **/
  public Entry find(Object vertex) {
  	int index = compFunction(vertex.hashCode());
  	DList ind = table[index];
  	if (ind == null) {
  		return null;
  	} else {
    	DListNode finger = (DListNode) ind.front();
    	while (finger != null) {
   			if (((Vertex) finger.item).equals(vertex)) {return (Vertex) finger.item;}
   			finger = (DListNode) ind.next(finger);
    		}
  		} 
    return null;
  }

  /** 
   *  Remove an entry with the specified key.  If such an entry is found,
   *  remove it from the table and return it; otherwise return null.
   *  If several entries have the specified key, choose one arbitrarily, then
   *  remove and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   */
  public Vertex remove(Object vertex) {
    double loadFactor = (this.totalItems/this.size);
    if (loadFactor <= 0.25) {
      this.resize();
    }
    int index = compFunction(vertex.hashCode());
    if (table[index] == null) {
      return null;
    } else {
  		DListNode finger = (DListNode) table[index].front();
  		while (finger != null) {
  			if (((Vertex) finger.item).getVertex().equals(vertex)) {
  				Vertex removed = (Vertex) finger.item;
  				table[index].remove(finger);
  				size--;
          totalItems--;
  				return removed;
  			}
  		  finger = (DListNode) finger.next;
		  }
    }
    return null;
  }

  /**
   *  Remove all entries from the dictionary.
   */
  public void makeEmpty() {
    table = new DList[table.length];
    size = 0;
  }
  
  public void printHistogram() {
	  for (DList list : table) {
		  if (list == null) {
			  System.out.println("0 ");
			  continue;
		  } else {
			  System.out.println(list.length() + " ");
		  }
	  }
  }

}
