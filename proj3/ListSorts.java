/* ListSorts.java */

import list.*;
import edge.*;

public class ListSorts {

  /**
   * We changed this class slightly so that it could handle and sort Edge objects.
   **/

  private final static int SORTSIZE = 100000;

  /**
   *  makeQueueOfQueues() makes a queue of queues, each containing one item
   *  of q.  Upon completion of this method, q is empty.
   *  @param q is a LinkedQueue of objects.
   *  @return a LinkedQueue containing LinkedQueue objects, each of which
   *    contains one object from q.
   **/
  public static LinkedQueue makeQueueOfQueues(LinkedQueue q) {
    // Replace the following line with your solution.
	LinkedQueue QOQ = new LinkedQueue();
    while(q.size() > 0) {
    	try {QOQ.enqueue(new LinkedQueue(q.dequeue()));} 
    	catch (QueueEmptyException e) {e.printStackTrace();}
    }
    return QOQ;
  }

  /**
   *  mergeSortedQueues() merges two sorted queues into a third.  On completion
   *  of this method, q1 and q2 are empty, and their items have been merged
   *  into the returned queue.
   *  @param q1 is LinkedQueue of Comparable objects, sorted from smallest 
   *    to largest.
   *  @param q2 is LinkedQueue of Comparable objects, sorted from smallest 
   *    to largest.
   *  @return a LinkedQueue containing all the Comparable objects from q1 
   *   and q2 (and nothing else), sorted from smallest to largest.
   **/
  public static LinkedQueue mergeSortedQueues(LinkedQueue q1, LinkedQueue q2) {
    // Replace the following line with your solution.
	  LinkedQueue merged = new LinkedQueue();
		Comparable large = null;
		LinkedQueue smallList = null; //get it? smallest?
		Comparable check1, check2;
		try {
			while (smallList==null || smallList.size() > 0) {
				if (large == null) {
					check1 = (Comparable) q1.dequeue();
					check2 = (Comparable) q2.dequeue();
				} else if (smallList == q1) {
						check1 = (Comparable) smallList.dequeue();
						check2 = large;
				} else {
						check1 = large;
						check2 = (Comparable) smallList.dequeue();
				}
				if (check1.compareTo(check2) <= 0) {
					merged.enqueue(check1);
					large = check2;
					smallList = q1;
				} else {
					merged.enqueue(check2);
					large = check1;
					smallList = q2;
				}
			}
			if (large!=null) {merged.enqueue(large);}
			while (q1.size() > 0) {merged.enqueue(q1.dequeue());}
			while (q2.size() > 0) {merged.enqueue(q2.dequeue());}
		}
		catch (QueueEmptyException e) {e.printStackTrace();}
		return merged;
  }

  /**
   * Changed the implementation of MergeSortedQueues so that it would handle edges.
   * This method also makes sure that no two same edges are added.
   * @param linked queue 1
   * @param linked queue 2
   * @return sorted linked queue
   **/
  public static LinkedQueue noEQMergeSortedQueues(LinkedQueue q1, LinkedQueue q2) {
		  LinkedQueue merged = new LinkedQueue();
			Comparable large = null;
			LinkedQueue smallList = null; //get it? smallest?
			Comparable check1, check2;
			try {
				while (smallList==null || smallList.size() > 0) {
					if (large == null) {
						check1 = (Comparable) q1.dequeue();
						check2 = (Comparable) q2.dequeue();
					} else if (smallList == q1) {
							check1 = (Comparable) smallList.dequeue();
							check2 = large;
					} else {
							check1 = large;
							check2 = (Comparable) smallList.dequeue();
					}
					if (check1.compareTo(check2) < 0) {
						merged.enqueue(check1);
						large = check2;
						smallList = q1;
					} else if (check1.compareTo(check2) == 0) {
						large = check2;
						smallList = q1;
					} else {
						merged.enqueue(check2);
						large = check1;
						smallList = q2;
					}
				}
				if (large!=null) {merged.enqueue(large);}
				while (q1.size() > 0) {merged.enqueue(q1.dequeue());}
				while (q2.size() > 0) {merged.enqueue(q2.dequeue());}
			}
			catch (QueueEmptyException e) {e.printStackTrace();}
			return merged;
	  }
  /**
   *  partition() partitions qIn using the pivot item.  On completion of
   *  this method, qIn is empty, and its items have been moved to qSmall,
   *  qEquals, and qLarge, according to their relationship to the pivot.
   *  @param qIn is a LinkedQueue of Comparable objects.
   *  @param pivot is a Comparable item used for partitioning.
   *  @param qSmall is a LinkedQueue, in which all items less than pivot
   *    will be enqueued.
   *  @param qEquals is a LinkedQueue, in which all items equal to the pivot
   *    will be enqueued.
   *  @param qLarge is a LinkedQueue, in which all items greater than pivot
   *    will be enqueued.  
   **/   
  
  public static void partition(LinkedQueue qIn, Comparable pivot, 
          LinkedQueue qSmall, LinkedQueue qEquals, 
          LinkedQueue qLarge) {
	try {
		while (qIn.size() > 0) {
			Comparable curr = (Comparable) qIn.dequeue();
		if (curr.compareTo(pivot) < 0) {qSmall.enqueue(curr);} 
		else if (curr.compareTo(pivot) == 0) {qEquals.enqueue(curr);}
		else {qLarge.enqueue(curr);} 
		}
	} catch (QueueEmptyException e) {e.printStackTrace();}
	if (qSmall.size() > 1) {quickSort(qSmall);}
	if (qLarge.size() > 1) {quickSort(qLarge);}
	qIn.append(qSmall);
	qIn.append(qEquals);
	qIn.append(qLarge);
	}

  /**
   *  mergeSort() sorts q from smallest to largest using mergesort.
   *  @param q is a LinkedQueue of Comparable objects.
   **/
  public static void mergeSort(LinkedQueue q) {
	  LinkedQueue QOQ = makeQueueOfQueues(q);
	  try {
		  while (QOQ.size() > 1) {QOQ.enqueue(mergeSortedQueues((LinkedQueue) QOQ.dequeue(), (LinkedQueue) QOQ.dequeue()));}
		  q.append((LinkedQueue) QOQ.dequeue());
	  } catch (QueueEmptyException e) {e.printStackTrace();}
  }
  
  public static void noEQMergeSort(LinkedQueue q) {
		  LinkedQueue QOQ = makeQueueOfQueues(q);
		  try {
			  while (QOQ.size() > 1) {QOQ.enqueue(noEQMergeSortedQueues((LinkedQueue) QOQ.dequeue(), (LinkedQueue) QOQ.dequeue()));}
			  q.append((LinkedQueue) QOQ.dequeue());
		  } catch (QueueEmptyException e) {e.printStackTrace();}
	  }

  /**
   *  quickSort() sorts q from smallest to largest using quicksort.
   *  @param q is a LinkedQueue of Comparable objects.
   **/
  public static void quickSort(LinkedQueue q) {
	  if (q.size() > 0) {
			int index;
			index = (1 + ((int) (Math.random() * 10.0)) % q.size());
			Comparable pivot = (Comparable) q.nth(index);
			partition(q, pivot, new LinkedQueue(), new LinkedQueue(), new LinkedQueue());
			}
  }

  /**
   *  makeRandom() builds a LinkedQueue of the indicated size containing
   *  Integer items.  The items are randomly chosen between 0 and size - 1.
   *  @param size is the size of the resulting LinkedQueue.
   **/
  public static LinkedQueue makeRandom(int size) {
    LinkedQueue q = new LinkedQueue();
    for (int i = 0; i < size; i++) {
      q.enqueue(new Integer((int) (size * Math.random())));
    }
    return q;
  }
  
}