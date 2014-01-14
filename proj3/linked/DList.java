/* DList.java */

package linked;


/**
 *  A DList is a mutable doubly-linked list ADT.  Its implementation is
 *  circularly-linked and employs a sentinel (dummy) node at the head
 *  of the list.
 *
 *  DO NOT CHANGE ANY METHOD PROTOTYPES IN THIS FILE.
 */

public class DList {

  /**
   *  head references the sentinel node.
   *  size is the number of items in the list.  (The sentinel node does not
   *       store an item.)
   *
   *  DO NOT CHANGE THE FOLLOWING FIELD DECLARATIONS.
   */

  protected DListNode head;
  private int size;

  /* DList invariants:
   *  1)  head != null.
   *  2)  For any DListNode x in a DList, x.next != null.
   *  3)  For any DListNode x in a DList, x.prev != null.
   *  4)  For any DListNode x in a DList, if x.next == y, then y.prev == x.
   *  5)  For any DListNode x in a DList, if x.prev == y, then y.next == x.
   *  6)  size is the number of DListNodes, NOT COUNTING the sentinel,
   *      that can be accessed from the sentinel (head) by a sequence of
   *      "next" references.
   */

  /**
   *  newNode() calls the DListNode constructor.  Use this class to allocate
   *  new DListNodes rather than calling the DListNode constructor directly.
   *  That way, only this method needs to be overridden if a subclass of DList
   *  wants to use a different kind of node.
   *  @param item the item to store in the node.
   *  @param prev the node previous to this node.
   *  @param next the node following this node.
   */
  protected DListNode newNode(Object item, DListNode prev, DListNode next) {
    return new DListNode(item, prev, next);
  }

  /**
   *  DList() constructor for an empty DList.
   */
  public DList() {
    head = newNode(null, head, head);
    setSize(0);
  }

  /**
   *  isEmpty() returns true if this DList is empty, false otherwise.
   *  @return true if this DList is empty, false otherwise. 
   *  Performance:  runs in O(1) time.
   */
  public boolean isEmpty() {
    return getSize() == 0;
  }

  /** 
   *  length() returns the length of this DList. 
   *  @return the length of this DList.
   *  Performance:  runs in O(1) time.
   */
  public int length() {
    return getSize();
  }

  /**
   *  insertFront() inserts an item at the front of this DList.
   *  @param item is the item to be inserted.
   *  Performance:  runs in O(1) time.
   */
  public void insertFront(Object item) {
    if (getSize() == 0) {
      DListNode insert = newNode(item, head, head);
      head.next = insert;
      head.prev = insert;
    }
    else {
      DListNode insert1 = newNode(item, head, head.next);
      head.next = insert1;
      insert1.next.prev = insert1;
    }
    setSize(getSize() + 1);
  }

  /**
   *  insertBack() inserts an item at the back of this DList.
   *  @param item is the item to be inserted.
   *  Performance:  runs in O(1) time.
   */
  public void insertBack(Object item) {
    if (getSize() == 0) {
      insertFront(item);
    }
    else {
      DListNode insert = newNode(item, head.prev, head);
      head.prev = insert;
      insert.prev.next = insert;
    }
    setSize(getSize() + 1);
  }

  /**
   *  front() returns the node at the front of this DList.  If the DList is
   *  empty, return null.
   *
   *  Do NOT return the sentinel under any circumstances!
   *
   *  @return the node at the front of this DList.
   *  Performance:  runs in O(1) time.
   */
  public DListNode front() {
    if (this.head == null) {
      return null;
    }
    else {
      return head.next;
    }
  }

  /**
   *  back() returns the node at the back of this DList.  If the DList is
   *  empty, return null.
   *
   *  Do NOT return the sentinel under any circumstances!
   *
   *  @return the node at the back of this DList.
   *  Performance:  runs in O(1) time.
   */
  public DListNode back() {
    if (this.head == null) {
      return null;
    }
    else {
      return head.prev;
    }
  }

  /**
   *  next() returns the node following "node" in this DList.  If "node" is
   *  null, or "node" is the last node in this DList, return null.
   *
   *  Do NOT return the sentinel under any circumstances!
   *
   *  @param node the node whose successor is sought.
   *  @return the node following "node".
   *  Performance:  runs in O(1) time.
   */
  public DListNode next(DListNode node) {
    if (node == null || node == head.prev) {
      return null;
    }
    else {
      return node.next;
    }
  }

  /**
   *  prev() returns the node prior to "node" in this DList.  If "node" is
   *  null, or "node" is the first node in this DList, return null.
   *
   *  Do NOT return the sentinel under any circumstances!
   *
   *  @param node the node whose predecessor is sought.
   *  @return the node prior to "node".
   *  Performance:  runs in O(1) time.
   */
  public DListNode prev(DListNode node) {
    if (node == null || node == head.next) {
      return null;
    }
    else {
      return node.prev;
    }
  }

  /**
   *  insertAfter() inserts an item in this DList immediately following "node".
   *  If "node" is null, do nothing.
   *  @param item the item to be inserted.
   *  @param node the node to insert the item after.
   *  Performance:  runs in O(1) time.
   */
  public void insertAfter(Object item, DListNode node) {
    if (node != null) {
      DListNode comp = newNode(item, node, node.next);
      node.next.prev = comp;
      node.next = comp;
      setSize(getSize() + 1);
    }
  }

  /**
   *  insertBefore() inserts an item in this DList immediately before "node".
   *  If "node" is null, do nothing.
   *  @param item the item to be inserted.
   *  @param node the node to insert the item before.
   *  Performance:  runs in O(1) time.
   */
  public void insertBefore(Object item, DListNode node) {
    if (node != null) {
      DListNode meow = newNode(item, node.prev, node);
      node.prev.next = meow;
      node.prev = meow;
      setSize(getSize() + 1);
    }
  }

  /**
   *  remove() removes "node" from this DList.  If "node" is null, do nothing.
   *  Performance:  runs in O(1) time.
   */
  public void remove(DListNode node) {
    node.next.prev = node.prev;
    node.prev.next = node.next;
    setSize(getSize() - 1);
  }

  /**
   *  toString() returns a String representation of this DList.
   *
   *  DO NOT CHANGE THIS METHOD.
   *
   *  @return a String representation of this DList.
   *  Performance:  runs in O(n) time, where n is the length of the list.
   */
  public String toString() {
    String result = "[  ";
    DListNode current = head.next;
    while (current != head) {
      result = result + current.item + "  ";
      current = current.next;
    }
    return result + "]";
  }

  /* Making a main method to test the above methods.
   */

  public static void main(String[] args) {

    /* Let's test DList */
    DList lst1 = new DList();
    lst1.insertFront(new Integer(3));
    lst1.insertFront(new Integer(2));
    lst1.insertFront(new Integer(0));
    System.out.println();
    System.out.println("Here is a list after insertFront 3, 2, 0: " + lst1.toString()); 
    System.out.println("length() should be 3. It is: " + lst1.length());
    lst1.insertBack(new Integer(5));
    System.out.println("Here is the same list after insertBack(5): " + lst1.toString());
    System.out.println("length() should be 4. It is: " + lst1.length());
    System.out.println();
    System.out.println("front() should return 0. It is: " + lst1.front().item);
    System.out.println("back() should return 5. It is: " + lst1.back().item);
    System.out.println();
    DListNode a = lst1.head.next;
    DListNode z = lst1.head.prev;
    System.out.println("next(1) should return 2. It is: " + lst1.next(a).item);
    System.out.println("next(5) should return null. It is: " + lst1.next(z));
    System.out.println("prev(5) should return 3. It is: " + lst1.prev(z).item);
    System.out.println("prev(1) should return null. It is: " + lst1.prev(a));
    System.out.println();
    DListNode thirdItem = lst1.head.next.next.next;
    System.out.println("The third item in the list is 3. Make sure this is 3: " + thirdItem.item);
    lst1.insertAfter(new Integer(4), thirdItem);
    System.out.println("Here is the list after insertAfter(4, thirdItem): " + lst1);
    System.out.println("length() should be 5. It is: " + lst1.length());
    lst1.insertBefore(new Integer(1), thirdItem);
    System.out.println("Now the thirdItem is: " + thirdItem.item);
    System.out.println("Here is the list after insertBefore(1, thirdItem): " + lst1);
    System.out.println("length() should be 6. It is: " + lst1.length());
    DListNode lastItem = lst1.head.prev;
    lst1.remove(thirdItem);
    System.out.println("Here is the list after remove(thirdItem): " + lst1);
    System.out.println("length() should be 5. It is: " + lst1.length());
    DList lst2 = new DList();
    lst2.insertFront(new Integer(3));
    lst2.insertFront(new Integer(1));
    System.out.println("Current second list: " + lst2);
    DListNode hi = lst2.head.next.next;
    System.out.println(hi.item);
    lst2.insertBefore(new Integer(2), hi);
    System.out.println(lst2);
  }

public int getSize() {
	return size;
}

public void setSize(int size) {
	this.size = size;
}
}

