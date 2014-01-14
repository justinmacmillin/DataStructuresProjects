/* WUGraph.java */

package graph;

import linked.DList;
import linked.DListNode;
import edge.Edge;
import edge.EdgeTable;

/**
 * The WUGraph class represents a weighted, undirected graph.  Self-edges are
 * permitted.
 */

public class WUGraph {


	int edgeCount;
	private int vertexCount;
	DList vertexList;
	private VertexTable vertexTable = new VertexTable(100);
	EdgeTable edgeTable = new EdgeTable(100);
  
  /**
   * WUGraph() constructs a graph having no vertices or edges.
   *
   * Running time:  O(1).
   */
  public WUGraph() {
	  edgeCount = 0;
	  setVertexCount(0);
	  vertexList = new DList();
  }

  /**
   * vertexCount() returns the number of vertices in the graph.
   *
   * Running time:  O(1).
   */
  public int vertexCount() {
	  
	  
  return vertexList.length();
  }

  /**
   * edgeCount() returns the number of edges in the graph.
   *
   * Running time:  O(1).
   */
  public int edgeCount() {
	  
	  
  return edgeCount;
  }

  /**
   * getVertices() returns an array containing all the objects that serve
   * as vertices of the graph.  The array's length is exactly equal to the
   * number of vertices.  If the graph has no vertices, the array has length
   * zero.
   *
   * (NOTE:  Do not return any internal data structure you use to represent
   * vertices!  Return only the same objects that were provided by the
   * calling application in calls to addVertex().)
   *
   * Running time:  O(|V|).
   */
  public Object[] getVertices() {
	if (vertexCount() == 0) {
		return new Object[0];
	}
	Object[] lst = new Object[vertexCount()];
	DListNode finger = (DListNode) vertexList.front();
	for (int i = 0; i < vertexCount(); i++) {
		lst[i] = ((Vertex) finger.item).getVertex();
		finger = (DListNode) finger.next;			
	}
	return lst;
  }

  
  public Object[] getVObj() {
		if (vertexCount() == 0) {
			return new Object[0];
		}
		Object[] lst = new Object[vertexCount()];
		DListNode finger = (DListNode) vertexList.front();
		for (int i = 0; i < vertexCount(); i++) {
			lst[i] = vertexTable.find(((Vertex) finger.item).getVertex());
			finger = (DListNode) finger.next;			
		}
		return lst;
	  }
  
  /**
   * addVertex() adds a vertex (with no incident edges) to the graph.  The
   * vertex's "name" is the object provided as the parameter "vertex".
   * If this object is already a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(1).
   */
  public void addVertex(Object vertex) {
	  if (!isVertex(vertex)) {
		  Vertex newVertex = new Vertex(vertex);
		  vertexList.insertFront(newVertex);
		  getVertexTable().insert(newVertex);
		  setVertexCount(getVertexCount() + 1);
	  }
  }

  /**
   * removeVertex() removes a vertex from the graph.  All edges incident on the
   * deleted vertex are removed as well.  If the parameter "vertex" does not
   * represent a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  public void removeVertex(Object vertex){
	  if (isVertex(vertex)) {
		  DListNode finger = vertexList.front();
		  while (!((Vertex) finger.item).getVertex().equals(vertex)) {
			  finger = finger.next;
		  }
		  vertexList.remove(finger);
		  setVertexCount(getVertexCount() - 1);
	  
	  Vertex v = getVertexTable().find(vertex);
	  if (v.neighbors.front() != null) {
		  DListNode track = v.neighbors.front();
		  while (track.item != null) {
			  removeEdge(track.item,vertex);
			  track = track.next;
		  }
	  }
	  
	  
	  
	  getVertexTable().remove(vertex);
	  }
  }

  /**
   * isVertex() returns true if the parameter "vertex" represents a vertex of
   * the graph.
   *
   * Running time:  O(1).
   */
  public boolean isVertex(Object vertex) {
	 return (getVertexTable().find(vertex) != null);
	}

  /**
   * degree() returns the degree of a vertex.  Self-edges add only one to the
   * degree of a vertex.  If the parameter "vertex" doesn't represent a vertex
   * of the graph, zero is returned.
   *
   * Running time:  O(1).
   */
  public int degree(Object vertex) {
	  if (isVertex(vertex)) {
	  	Vertex vert = (Vertex) getVertexTable().find(vertex);
	  	return vert.neighbors.length();
	  }
	  return 0;
  }


  /**
   * getNeighbors() returns a new Neighbors object referencing two arrays.  The
   * Neighbors.neighborList array contains each object that is connected to the
   * input object by an edge.  The Neighbors.weightList array contains the
   * weights of the corresponding edges.  The length of both arrays is equal to
   * the number of edges incident on the input vertex.  If the vertex has
   * degree zero, or if the parameter "vertex" does not represent a vertex of
   * the graph, null is returned (instead of a Neighbors object).
   *
   * The returned Neighbors object, and the two arrays, are both newly created.
   * No previously existing Neighbors object or array is changed.
   *
   * (NOTE:  In the neighborList array, do not return any internal data
   * structure you use to represent vertices!  Return only the same objects
   * that were provided by the calling application in calls to addVertex().)
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  public Neighbors getNeighbors(Object vertex) {
	  if (!isVertex(vertex)) {
		  System.out.println("fuck");
		  return null;
	  }
	  if (degree(vertex) == 0) {
		  return null;
	  }
	  Vertex vert = getVertexTable().find(vertex);
	  Neighbors neighbors = new Neighbors();
	  neighbors.neighborList = new Object[degree(vertex)];
	  neighbors.weightList = new int[degree(vertex)];
	  DListNode finger = vert.neighbors.front();
	  for (int i = 0; i < degree(vertex); i++) {
		  neighbors.neighborList[i] = finger.item;
		  neighbors.weightList[i] = ((Edge) edgeTable.find(finger.item,vertex)).getWeight();
		  finger = finger.next;
	  }
	  return neighbors;
  }

  /**
   * addEdge() adds an edge (u, v) to the graph.  If either of the parameters
   * u and v does not represent a vertex of the graph, the graph is unchanged.
   * The edge is assigned a weight of "weight".  If the edge is already
   * contained in the graph, the weight is updated to reflect the new value.
   * Self-edges (where u == v) are allowed.
   *
   * Running time:  O(1).
   */
  public void addEdge(Object u, Object v, int weight) {
	  if (!(isVertex(u) && isVertex(v))) {
		  return;
	  }
	  Vertex v1 = getVertexTable().find(u);
	  Vertex v2 = getVertexTable().find(v);
	  if (isEdge(u,v)) {
		  edgeTable.find(u,v).setWeight(weight);
		  return;
	  }
	  Edge newEdge = new Edge(v1,v2,weight);
	  edgeTable.insert(newEdge);
	  edgeCount++;
	  if (v1 == v2) {
		  v1.addNeighbor(u);
	  } else {
		  v1.addNeighbor(v);
		  v2.addNeighbor(u);
	  }
	  
  }

  /**
   * removeEdge() removes an edge (u, v) from the graph.  If either of the
   * parameters u and v does not represent a vertex of the graph, the graph
   * is unchanged.  If (u, v) is not an edge of the graph, the graph is
   * unchanged.
   *
   * Running time:  O(1).
   */
  public void removeEdge(Object u, Object v) {
	  if (isEdge(u,v)) {
		  Vertex v1 = getVertexTable().find(u);
		  Vertex v2 = getVertexTable().find(v);
		  if (u != v) {
			  v1.removeNeighbor(v);
			  v2.removeNeighbor(u);
		  } else {
			  v1.removeNeighbor(v);
		  }
		  edgeTable.remove(u,v);
		  edgeCount--;
	  }
  }

  /**
   * isEdge() returns true if (u, v) is an edge of the graph.  Returns false
   * if (u, v) is not an edge (including the case where either of the
   * parameters u and v does not represent a vertex of the graph).
   *
   * Running time:  O(1).
   */
  public boolean isEdge(Object u, Object v) {
	  return isVertex(u) && isVertex(v) && (edgeTable.find(u, v) != null);
  }

  /**
   * weight() returns the weight of (u, v).  Returns zero if (u, v) is not
   * an edge (including the case where either of the parameters u and v does
   * not represent a vertex of the graph).
   *
   * (NOTE:  A well-behaved application should try to avoid calling this
   * method for an edge that is not in the graph, and should certainly not
   * treat the result as if it actually represents an edge with weight zero.
   * However, some sort of default response is necessary for missing edges,
   * so we return zero.  An exception would be more appropriate, but
   * also more annoying.)
   *
   * Running time:  O(1).
   */
  public int weight(Object u, Object v) {
	  if (isEdge(u,v)) {
	  	return edgeTable.find(u,v).getWeight();
	  }
	  return 0;
  }
  
  public String toString() {
	  String result = "";
	  DListNode curr = vertexList.front();
	  for(int i = 0; i < vertexList.length(); i++) {
		  if(i == vertexList.length() - 1) {result+=curr.item;}
		  else {result+=(curr.item + " | ");}
		  curr = curr.next;
	  }
	  return result;
  }

  public VertexTable getVertexTable() {
  	return vertexTable;
  }

  public void setVertexTable(VertexTable vertexTable) {
  	this.vertexTable = vertexTable;
  }

  public int getVertexCount() {
  	return vertexCount;
  }

  public void setVertexCount(int vertexCount) {
  	this.vertexCount = vertexCount;
  }
  
  
}
