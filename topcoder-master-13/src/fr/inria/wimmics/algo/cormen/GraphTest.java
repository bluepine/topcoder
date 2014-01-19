package fr.inria.wimmics.algo.cormen;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fr.inria.wimmics.algo.cormen.Graph.Vertex;

public class GraphTest {

	
	GraphAlgorithm algo;
	
	@Before
	public void setUp() {

		algo = new GraphAlgorithm();
	}
	
	void initBfsGraph(Graph graph) {
		int totalNodes;
		totalNodes = 8;
		for(int i=0;i<totalNodes;i++) {
			Graph.Vertex v = graph.addVertex(i);
			if(v.getId()!=i) {
				fail("addVertex failed");
			}
		}
		
		graph.addBiEdge(0,1);
		graph.addBiEdge(0,4);
		graph.addBiEdge(1,5);
		graph.addBiEdge(2,5);
		graph.addBiEdge(2,6);
		graph.addBiEdge(2,3);
		graph.addBiEdge(3,6);
		graph.addBiEdge(3,7);
		graph.addBiEdge(5,6);
		graph.addBiEdge(6,7);


	}
	
	@Test
	public void testBfs() {
		//fail("Not yet implemented");
		Graph graph = new Graph();
		initBfsGraph(graph);
	
		Vertex start = graph.getVertex(1);
		Vertex end = graph.getVertex(7);
		
		algo.bfs(graph,start);
		System.out.println(end.getDistance());
		
		algo.printPath(graph,start,end);
		System.out.println("");
		
	}
	
	void initTopoGraph(Graph graph) {
		int totalNodes;
		totalNodes = 9;

		for(int i=0;i<totalNodes;i++) {
			Graph.Vertex v = graph.addVertex(i);
			if(v.getId()!=i) {
				fail("addVertex failed");
			}
		}
		
		graph.addEdge(0, 1);
		graph.addEdge(0, 7);
		graph.addEdge(1, 2);
		graph.addEdge(1, 7);
		graph.addEdge(2, 5);
		graph.addEdge(3, 2);
		graph.addEdge(3, 4);
		graph.addEdge(4, 5);
		graph.addEdge(6, 7);
		
		/*System.out.println("Graph:");
		for(Vertex u:graph.V) {
			System.out.println("u:"+u.getId());
			for(Vertex v:graph.getAdjacent(u)) {
				System.out.println("    v:"+v.getId());
				
			}
		}*/
		
	}

	@Test
	public void testTopologicalSort() {
		Graph graph = new Graph();
		initTopoGraph(graph);
		algo.dfs(graph);
		System.out.println("Topologically sorted vertexs:");
		System.out.print("Vertex/f:");
		for(Vertex v:graph.getTopologicallySorted()) {
			System.out.print(v.getId()+"/"+v.getFinish()+" ");
		}
		System.out.println("");

	}
	
	@Test
	public void testStronglyConnectedComponent() {
		int totalNodes = 8;
		//totalEdges = 9;
		Graph g = new Graph();
		for(int i=0;i<totalNodes;i++) {
			Graph.Vertex v = g.addVertex(i);
			if(v.getId()!=i) {
				fail("addVertex failed");
			}
		}
		g.addEdge(0, 1);
		g.addEdge(1, 2);
		g.addEdge(1, 4);
		g.addEdge(1, 5);
		g.addEdge(2, 3);		
		g.addEdge(2, 6);
		g.addEdge(3, 2);	
		g.addEdge(3, 7);
		g.addEdge(4, 0);	
		g.addEdge(4, 5);
		g.addEdge(5, 6);
		g.addEdge(6, 5);
		g.addEdge(6, 7);
		g.addEdge(7, 7);
		
		List<List<Vertex>> scc = algo.stroglyConnectedComponent(g);
		
		int i=1;
		for(List<Vertex> com:scc) {
			System.out.print("Component "+i+++": ");
			for(Vertex v:com) {
				System.out.print(v.getId()+" ");
			}
			System.out.println("");
		}
	}
}
