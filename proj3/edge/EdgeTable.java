/* EdgeTable.java */

package edge;

import linked.*;

public class EdgeTable {
	
	SList[] table;
	int size;
  int totalItems;

  /** 
   *  Construct a new empty  edge table intended to hold roughly sizeEstimate
   *  entries.
   **/
  public EdgeTable(int sizeEstimate) {
  	int estimate = sizeEstimate + sizeEstimate/2;
  	while (!prime(estimate)) {
  		estimate++;
  	}
  	table = new SList[estimate];
  	size = estimate;
  }

  /** 
   * Construct a new empty hash table with a default size.
   **/
  public EdgeTable() {
  	size = 101;
  	table = new SList[101];
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
  public EdgeTable resize() {
    double loadFactor = (this.totalItems/this.size);
    int halfLoad = (this.size)/2;
    int doubleLoad = (this.size)*2;
    EdgeTable newTable = new EdgeTable(this.size);
    if (loadFactor < 0.25 || loadFactor >= 1) {
      if (loadFactor < 0.25) {
        newTable = new EdgeTable(halfLoad);
      }
      if (loadFactor >= 1) {
        newTable = new EdgeTable(doubleLoad);
      }
      for (SList i : this.table) {
        if (i != null && i.getSize() > 0) {
          SListNode current = (SListNode) i.front();
          for (int n = 0; n < i.getSize(); n++) {
            ((EdgeTable)newTable).insert((Edge)current.getItem());
            try {current = (SListNode) current.next();} 
            catch (InvalidNodeException e) {System.out.println("in resize");}
          }
        }
      }
    }
    return ((EdgeTable)newTable);
  }

  /**
   *  Create a new Edge object referencing the input key and associated value,
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
  public Edge insert(Edge e) {
    if (this.totalItems > 0 && this.size > 0) {
      int loadFactor = (this.totalItems/this.size);
      if (loadFactor >= 1) {
        this.resize();
      }
    }
  	int index = compFunction(e.getV1().getVertex().hashCode()+e.getV2().getVertex().hashCode());
  	SList ind = table[index];
  	if (ind == null) {
  		table[index] = new SList();
  		table[index].insertFront(e);
  	} else {
  		ind.insertFront(e);
  	}
    totalItems++;
  	return e;
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
  public Edge find(Object v1, Object v2) {
  	int index = compFunction(v1.hashCode()+v2.hashCode());
  	SList ind = table[index];
  	if (ind == null) {
  		return null;
	  } else {
  		try {
  			SListNode finger = (SListNode) ind.front();
  			while (finger != null) {
  				if ((((Edge) (finger.item())).isSame(v1,v2))) {
  					return (Edge) finger.item();
  				}
  				finger = (SListNode) finger.next();
  			}
  		}
  		catch (InvalidNodeException m) {
  			return null;
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
  public Edge remove(Object v1, Object v2) {
    double loadFactor = (this.totalItems/this.size);
    if (loadFactor <= 0.25) {
      this.resize();
    }
    int index = compFunction(v1.hashCode()+v2.hashCode());
  	if (table[index] == null) {
  		return null;
  	} else {
  		try {
  			SListNode finger = (SListNode) table[index].front();
  			while (finger != null) {
  				if ((((Edge) (finger.item())).isSame(v1,v2))) {
  					Edge removed = (Edge) finger.item();
  					finger.remove();
  					size--;
            totalItems--;
  					return removed;
  				}
  				finger = (SListNode) finger.next();
  			}
  		}
  		catch (InvalidNodeException m) {
  			return null;
  		}
  	}
  return null;
  }

  /**
   *  Remove all entries from the dictionary.
   */
  public void makeEmpty() {
    table = new SList[table.length];
    size = 0;
  }
  
  public void printHistogram() {
	  for (SList list : table) {
		  if (list == null) {
			  System.out.println("0 ");
			  continue;
		  } else {
			  System.out.println(list.length() + " ");
		  }
	  }
  }

  public static void main(String[] args) {
  }

}
