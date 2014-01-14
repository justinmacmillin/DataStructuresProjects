/* Entry.java */

package edge;

/**
 *  A class for dictionary entries.
 *
 *  DO NOT CHANGE THIS FILE.  It is part of the interface of the
 *  Dictionary ADT.
 **/

public class Entry {

  protected Object key;
  protected Object value;

  public Object key() {
	if(value == null || key == null) {System.out.println(value + " value returned by Entry: " + this + " with key: " + key);}
    return key;
  }

  public Object value() {
	if(value == null || key == null) {System.out.println(value + " value returned by Entry: " + this + " with key: " + key);}
    return value;
  }
  
  public void setVal(Object value) {
	  this.value = value;
  }

  public void setKey(Object key) {
	  this.key = key;
  }
}
