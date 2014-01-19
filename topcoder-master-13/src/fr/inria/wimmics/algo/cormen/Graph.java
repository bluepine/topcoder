package fr.inria.wimmics.algo.cormen;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Graph represents a graph using adjacency list concept. It can represent weighted graph. However, it cannot represent multi-edges between two given vertexes  
 * @author hrakebul
 *
 */
public class Graph {

	public final static int WHITE = 0;
	public final static int GRAY = 1;
	public final static int BLACK = 3;
	public final static int INF = 2147483647;
	public final static int NIL  = -1; //maybe have to be changed depending on the problems
	
	/**
	 * Represents a vertex containing the integer id, color in a visit algorithm, distance from the source in bfs/ discovery time in dfs, finish time in dfs, path
	 * @author hrakebul
	 *
	 */
	public class Vertex {
		int id;
		int color;
		int distance;
		int finish;
		Vertex path;
		
		public Vertex() {
			super();
			id=-1;
			color = WHITE;
			path = null;
			distance  = 0;
			finish = 0;
		}
		
		public Vertex(int id, int color, int distance, int finish, Vertex path) {
			super();
			this.id = id;
			this.color = color;
			this.distance = distance;
			this.finish = finish;
			this.path = path;
		}

		public Vertex(int id, int color, int distance, Vertex path) {
			super();
			this.id = id;
			this.color = color;
			this.path = path;
			this.distance = 0;
			this.finish = 0;
		}

		public int getFinish() {
			return finish;
		}

		public void setFinish(int finish) {
			this.finish = finish;
		}

		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public int getColor() {
			return color;
		}
		public void setColor(int color) {
			this.color = color;
		}
		public Vertex getPath() {
			return path;
		}
		public void setPath(Vertex path) {
			this.path = path;
		}

		public int getDistance() {
			return distance;
		}

		public void setDistance(int distance) {
			this.distance = distance;
		}
		
	}
	
	List<Vertex> V;
	HashMap<Vertex, HashMap<Vertex, Integer>> Adj; // weighted adjacency-list of the graph, adj[u] should return the list of the adjacent vertexes of u  
	List<Vertex> topologicallySorted;
	

	/**
	 * Returns the topologically sorted order of vertexes after a dfs in a linked list. Only for directed acyclic graphs (DAGs). 
	 * @return List<Vertex>
	 */
	public List<Vertex> getTopologicallySorted() {
		return topologicallySorted;
	}

	
	public Graph() {
		Adj = new HashMap<Graph.Vertex, HashMap<Vertex,Integer>>();
		V = new LinkedList<Graph.Vertex>();
		topologicallySorted = new LinkedList<Graph.Vertex>();
	}
	public Graph(Graph gn) {
		this.Adj = new HashMap<Graph.Vertex, HashMap<Vertex,Integer>>(gn.Adj);
		this.V = new LinkedList<Graph.Vertex>(gn.V);
		this.topologicallySorted = new LinkedList<Graph.Vertex>(gn.topologicallySorted);
	}
	/**
	 * Returns the vertexes of the graph
	 * @return List of Vertex
	 */
	public List<Vertex> getVertexes() {
		return V;
	}
	
	public void setVertexes(List<Vertex> v) {
		this.V = v;
	}

	/**
	 * Returns the adjacent vertexes of a vertex
	 * @param u
	 * @return Set<Vertex>
	 */
	public Set<Vertex> getAdjacent(Vertex u) {

		return Adj.get(u).keySet();
	}
	
	/**
	 * Returns the instance of a Vertex for an integer id. If there is no vertex with id in the graph, it returns null.
	 * @param id
	 * @return Vertex
	 */

	public Vertex getVertex(int id) {
		for(Vertex tu:V) {
			if(tu.getId()==id) {
				return tu;
			}
		}
		return null;
	}
	
	/**
	 * Adds a vertex with id u in the graph and returns the vertex. If the vertex already exists in the graph, it returns the existing vertex with adding a new one.
	 * @param u
	 * @return Vertex
	 */
	
	public Vertex addVertex(int u) {
		for(Vertex tu:V) {
			if(tu.getId()==u) {
				return tu;
			}
		}
		Vertex uu = new Vertex(u, WHITE, 0, null);
		V.add(uu);
		HashMap<Vertex, Integer> hm2D = new HashMap<Graph.Vertex, Integer>();
		Adj.put(uu, hm2D);
		return uu;
	}
	public void addVertex(Vertex u) {
		for(Vertex tu:V) {
			if(tu.getId()==u.getId()) {
				return;
			}
		}

		V.add(u);
		HashMap<Vertex, Integer> hm2D = new HashMap<Graph.Vertex, Integer>();
		Adj.put(u, hm2D);
		
	}
	
	/**
	 * Adds an edge between a vertex with id u and vertex with id v. The default weight for this edge is set to 1.
	 * @param u
	 * @param v
	 */
	public void addEdge(int u, int v) {
		addWeightedEdge(u, v, 1);
	}
	
	/**
	 * Adds an edge between a vertex with id u and vertex with id v with weight w.
	 * @param u
	 * @param v
	 * @param w
	 */
	public void addWeightedEdge(int u, int v, int w) {
		Vertex uu = getVertex(u);
		Vertex vv = getVertex(v);
		addWeightedEdge(uu,vv,w);
	}

	/**
	 * Adds an edge between a vertex u and vertex v. The default weight for this edge is set to 1.
	 * @param u
	 * @param v
	 */
	public void addEdge(Vertex u, Vertex v) {
		
		HashMap<Vertex, Integer> hm2D;
		hm2D = Adj.get(u);
		hm2D.put(v, 1);
	}
	
	/**
	 * Adds a weighted edge between a vertex u and vertex v with weight w.
	 * @param u
	 * @param v
	 * @param w
	 */
	public void addWeightedEdge(Vertex u, Vertex v, int w) {
		HashMap<Vertex, Integer> hm2D;
		hm2D = Adj.get(u);
		hm2D.put(v, w);
	}
	/**
	 * Adds a bidirectional edge between a vertex with id u and vertex with id v. The default weight for each of the unidirectional edges in the bidirectional edge is set to 1.
	 * @param u
	 * @param v
	 */
	public void addBiEdge(int u, int v) {
		addBiWeightedEdge(u, v, 1);
		addBiWeightedEdge(v, u, 1);

	}	
	
	/**
	 * Adds a bidirectional weighted edge between a vertex with id u and vertex with id v with weight w for each of the unidirectional edges in the bidirectional edge.
	 * @param u
	 * @param v
	 * @param w
	 */
	public void addBiWeightedEdge(int u, int v, int w) {
		addWeightedEdge(u, v, w);
		addWeightedEdge(v, u, w);
	}
	
	/**
	 * Adds a bidirectional edge between a vertex u and vertex v. The default weight for each of the unidirectional edges in the bidirectional edge is set to 1.
	 * @param u
	 * @param v
	 */
	public void addBiEdge(Vertex u, Vertex v) {
		addBiWeightedEdge(u, v, 1);
		addBiWeightedEdge(v, u, 1);

	}	
	
	/**
	 * Adds a bidirectional weighted edge between a vertex u and vertex v with weight w for each of the unidirectional edges in the bidirectional edge.
	 * @param u
	 * @param v
	 * @param w
	 */
	public void addBiWeightedEdge(Vertex u, Vertex v, int w) {
		addWeightedEdge(u, v, w);
		addWeightedEdge(v, u, w);
	}
	
	/**
	 * Removes an edge between vertex u and vertex v from the graph
	 * @param u
	 * @param v
	 */
	public void removeEdge(Vertex u, Vertex v) {
		HashMap<Vertex, Integer> hm2D;
		hm2D = Adj.get(u);
		hm2D.remove(v);
	}
	
	/**
	 * Removes an edge between vertex with id u and vertex with id v from the graph
	 * @param u
	 * @param v
	 */
	public void removeEdge(int u, int v) {
		Vertex uu = getVertex(u);
		Vertex vv = getVertex(v);
		removeEdge(uu, vv);
	}
	
	public Graph transpose() {
		Graph tg = new Graph();
		tg.topologicallySorted = new LinkedList<Graph.Vertex>(this.topologicallySorted);
		for(Vertex u:this.V) {
			tg.addVertex(u);
		}
		for(Vertex u:this.V) {
			for(Vertex v:this.getAdjacent(u)) {
				tg.addEdge(v, u);
			}
		}		
		return tg;
	}

}
