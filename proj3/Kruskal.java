/* Kruskal.java */

import graph.*;
import linked.ListNode;
import list.*;
import set.*;
import edge.Edge;
import edge.EdgeTable;
import edge.Entry;

/**
 * The Kruskal class contains the method minSpanTree(), which implements
 * Kruskal's algorithm for computing a minimum spanning tree of a graph.
 */

public class Kruskal {

  /**
   * minSpanTree() returns a WUGraph that represents the minimum spanning tree
   * of the WUGraph g.  The original WUGraph g is NOT changed.
   */
 public static WUGraph minSpanTree(WUGraph g) {
	  WUGraph newG = vClone(g);
	  Object[] newVList = newG.getVObj();
	  KruskalTable KT = makeVtxDictionary(newVList);
	  LinkedQueue EL = edgeList(g);
	  ListSorts.noEQMergeSort(EL);
	  DisjointSets ES = new DisjointSets(EL.size());
	  while (!EL.isEmpty()) {
		  try {
			  	Edge curr = (Edge) EL.dequeue();
				int currOrigin = (Integer)(KT.find(curr.getV1()).value());	// Corresponding int to the origin and dest respectively.
				int currDest = (Integer)(KT.find(curr.getV2()).value());
				int currWeight = curr.getWeight();
				currOrigin = ES.find(currOrigin);
				currDest = ES.find(currDest);
				if (currOrigin != currDest) {
					newG.addEdge(curr.getV1().getVertex(), curr.getV2().getVertex(), currWeight);
					ES.union(currOrigin, currDest);
				}
	  } catch (QueueEmptyException e) {System.out.println(e);}
	  }
	  return newG;
  }
  
   
	
  /**
   * This method makes a dictionary out of a sorted linked queue input.
   * @param sorted linked queue
   * @return EdgeTable
   **/
  public static KruskalTable makeVtxDictionary(Object[] vList) {
    KruskalTable myTable = new KruskalTable(769);
    for(int i = 0; i < vList.length; i++) {myTable.insert((Vertex) vList[i], i);}
    return myTable;
  }
  
  public static LinkedQueue edgeList(WUGraph g) {
	  LinkedQueue EL = new LinkedQueue();
	  Object[] vList = g.getVertices();
	  for(int i = 0; i < vList.length; i++) {
		  Neighbors curr = g.getNeighbors(vList[i]);
		  if (curr == null || (curr.neighborList.length != curr.weightList.length)) { continue; }
		  for (int j = 0; j < curr.neighborList.length; j++) {EL.enqueue(new Edge(g.getVertexTable().find(vList[i]), g.getVertexTable().find(curr.neighborList[j]), curr.weightList[j]));;}
	  }
	  return EL;
}
  
  /**
   * Duplicates a graph, without copying the reference.
   * @param g is the WUGraph we want to duplicate
   * @return new WUGraph
   *
   * Performance: runs in O(|V|) time.
   **/
  public static WUGraph vClone(WUGraph g) {
          WUGraph t = new WUGraph();
          if (g.getVertexCount() == 0) {return t;}
          Object[] vertices = g.getVertices();
          for (int i = vertices.length-1; i >= 0; i--) {t.addVertex(vertices[i]);}
          return t;
  }

public static void main(String[] args) {
	WUGraph g = new WUGraph();
	g.addVertex("A");
	g.addVertex("B");
	g.addVertex("C");
	g.addVertex("D");
	System.out.println(g);
	Object[] test = g.getVertices();
	for(int i = 0; i < test.length; i++) {
		int j = i+1;
		if (i == test.length-1) {j = 0;}
		g.addEdge(test[i], test[j], i);
		}
	for(int k = 0; k < test.length; k++) {
		int l = k+2;
		int m = k-1;
		if (k == test.length-2) {l = 0;}
		if (k == test.length-1) {l = 1;}
		if (k == 0) {m = 4;}
		g.addEdge(test[k], test[l], m);
		}
	for(int n = 0; n < test.length; n++) {
		int o = n+3;
		int p = n-2;
		if (n == test.length-3) {o = 0;}
		if (n == test.length-2) {o = 1;}
		if (n == test.length-1) {o = 2;}
		if (n == 0) {p = 4;}
		if (n == 1) {p = 5;}
		g.addEdge(test[n], test[o], p);
		}
	LinkedQueue EL = (edgeList(g));
	System.out.println(EL);
	ListSorts.noEQMergeSort(EL);
	System.out.println(EL);
	System.out.println(vClone(g));
	WUGraph newG = minSpanTree(g);
	System.out.println(newG);
	EL = edgeList(newG);
	ListSorts.noEQMergeSort(EL);
	System.out.println(EL);
}
}