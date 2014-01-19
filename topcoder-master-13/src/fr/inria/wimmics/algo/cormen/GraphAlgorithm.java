package fr.inria.wimmics.algo.cormen;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import fr.inria.wimmics.algo.cormen.Graph.Vertex;

public class GraphAlgorithm {

	int dfsTime; //take care of the synchronization issues in case of multiple threads calling dfs() 
	boolean dfsBackEdge;
	
	public void bfs(Graph G, Vertex s) {
		for(Vertex u:G.getVertexes()) {
			u.setColor(Graph.WHITE);
			u.setDistance(Graph.INF);
			u.setPath(null);
		}
		s.setColor(Graph.GRAY);
		s.setDistance(0);
		s.setPath(null);
		Queue<Vertex> Q=new LinkedList<Vertex>();
		Q.add(s);
		while(Q.isEmpty()==false) {
			Vertex u = Q.poll();
			for(Vertex v:G.getAdjacent(u)) {
				if(v.getColor() == Graph.WHITE) {
					v.setColor(Graph.GRAY);
					v.setDistance(u.getDistance()+1);
					v.setPath(u);
					Q.add(v);
				}
			}
			u.setColor(Graph.BLACK);
		}
	}
	
	public void printPath(Graph G,Vertex s, Vertex v) {
		if(v==s)
			System.out.print(v.getId());
		else if(v.path==null) {
			System.out.println("No path from"+s.getId()+" to "+v.getId()+" exists");
		}
		else {
			printPath(G, s, v.path);
			System.out.print(" "+v.getId());
		}
	}
	
	
	public void dfs(Graph G) {
		
		for(Vertex u:G.getVertexes()) {
			u.setColor(Graph.WHITE);
			u.setPath(null);
		}
		dfsBackEdge = false;
		dfsTime = 0;
		for(Vertex u:G.getVertexes()) {
			if(u.getColor()==Graph.WHITE) {
				dfsVisit(G,u);
			}
		}
	}

	private void dfsVisit(Graph G, Vertex u) {
		dfsTime++;
		u.setDistance(dfsTime);
		u.setColor(Graph.GRAY);
		for(Vertex v:G.getAdjacent(u)) {
			//dfs of undirected graphs can have only tree edges and back edges (Theorem 22.10, Cormen 3rd Ed.)
			if(v.getColor()==Graph.WHITE) {
				//a tree edge
				v.setPath(u);
				dfsVisit(G, v);
			}
			else if(v.getColor()==Graph.GRAY) {
				//a back edge
				dfsBackEdge = true;
				//if dfs is done only to check for dag, then it can be returned form here after the discovery of the first back edge
			}
			//if the color was black then it would have been a forward or cross edge
		}
		u.setColor(Graph.BLACK);
		dfsTime++;
		u.setFinish(dfsTime);
		List<Vertex> tSorted = G.getTopologicallySorted();
		tSorted.add(0, u);
	}
	
	public boolean isDAG(Graph g) {
		// a directed graph g is acyclic if and only if dfs of g yields not back edges (Lemma 22.11, Cormen 3rd Ed.).
		dfs(g);
		return !dfsBackEdge;
	}

	private List<Vertex> component;
	private List<List<Vertex>> dfs4SCC(Graph G) {
		for(Vertex u:G.getVertexes()) {
			u.setColor(Graph.WHITE);
			u.setPath(null);
		}
		dfsBackEdge = false;
		dfsTime = 0;
		List<List<Vertex>> scc = new ArrayList<List<Vertex>>();
		this.component = new ArrayList<Graph.Vertex>(); //for adding the gray vertexes, i.e. vertexes of each component
		for(Vertex u:G.getTopologicallySorted()) {
			if(u.getColor()==Graph.WHITE) {
				this.component.clear();
				dfsVisit4SCC(G,u);
				scc.add(new ArrayList<Graph.Vertex>(component));
			}
		}
		return scc;
	}

	private void dfsVisit4SCC(Graph G, Vertex u) {
		dfsTime++;
		u.setDistance(dfsTime);
		u.setColor(Graph.GRAY);
		this.component.add(u);
		for(Vertex v:G.getAdjacent(u)) {
			//dfs of undirected graphs can have only tree edges and back edges (Theorem 22.10, Cormen 3rd Ed.)
			if(v.getColor()==Graph.WHITE) {
				//a tree edge
				v.setPath(u);
				dfsVisit4SCC(G, v);
			}
			//if the color was gray then it would have been a back edge
			//if the color was black then it would have been a forward or cross edge
		}
		u.setColor(Graph.BLACK);
		dfsTime++;
		u.setFinish(dfsTime);
	}
	
	
	public List<List<Vertex>> stroglyConnectedComponent(Graph g) {
		dfs(g);
		Graph transposedGraph = g.transpose();
		return dfs4SCC(transposedGraph);
	}
}

