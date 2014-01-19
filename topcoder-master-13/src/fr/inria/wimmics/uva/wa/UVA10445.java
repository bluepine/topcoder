package fr.inria.wimmics.uva.wa;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

//class Main {
//
//}

class UVA10445 {
	int size = 12;
	//int[] config = new int[size];
	int[] leftRingClockConfigIndex = null;
	int[] leftRingCounterClockConfigIndex = null;
	int[] counterClockWhole = null;
	int[] clockWhole = null;
	int[] rightRingClockConfigIndex = null;
	int[] rightRingCounterClockConfigIndex = null;
	
	public UVA10445() {
		init();
	}

	public void init() {
		leftRingClockConfigIndex = new int[] {2,3,4,5,6,12,7,8,9,10,11,1};
		leftRingCounterClockConfigIndex = new int[]{12,1,2,3,4,5,7,8,9,10,11,6};
		counterClockWhole = new int[]{12,1,2,3,4,5,6,7,8,9,10,11};
		clockWhole = new int[]{2,3,4,5,6,7,8,9,10,11,12,1};
		rightRingClockConfigIndex = new int[]{1,2,3,4,5,7,8,9,10,11,12,6};
		rightRingCounterClockConfigIndex = new int[]{1,2,3,4,5,12,6,7,8,9,10,11};
		
	}
	
	public static void main(String[] args) throws FileNotFoundException  {

		
		
		UVA10445 sol = new UVA10445();
		
		//FileInputStream fis = new FileInputStream("in/12445.in");
		//sol.solution(fis);
		sol.solution(System.in);
	}		

	public List<Integer> rotate(List<Integer> config, int[] turnConfig) {
		List<Integer> tempConfig = new ArrayList<Integer>(config);
		List<Integer> resConfig = new ArrayList<Integer>(config);
				
		for(int i=0;i<tempConfig.size();i++) {
			resConfig.set(i, tempConfig.get(turnConfig[i]-1));
		}
		return resConfig;
		
	}
	
	public boolean isInitial(List<Integer> config) {
		
		for(int i=0;i<config.size();i++) {
			if(config.get(i)!=(i+1)) {
				return false;
			}
		}
		
		return true;
	}
	//adjacent
	List<List<Integer>> getAdjacent(List<Integer> config) {
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		
		result.add(rotate(config, leftRingClockConfigIndex));
		result.add(rotate(config, leftRingCounterClockConfigIndex));
		result.add(rotate(config, counterClockWhole));
		result.add(rotate(config, clockWhole));
		result.add(rotate(config, rightRingClockConfigIndex));
		result.add(rotate(config, rightRingCounterClockConfigIndex));
		
		return result;
	}
	
	public String stateName(List<Integer> config) {
		StringBuffer name = new StringBuffer();
		for(int i:config) {
			name.append(String.valueOf(i)+",");
		}
		return name.toString();
	}
	
	// optimize using heuristics, uniform search, A* search. check the AI book
	// the sate-space of this problem
//			1 = 1
//			2 = 6
//			3 = 36
//			4 = 216
//			5 = 1296
//			6 = 7776
//			7 = 46656
//			8 = 279936
//			9 = 1679616
//			10 = 10077696
//			11 = 60466176
//			12 = 362797056
//			13 = 2176782336
//			14 = 13060694016
//			15 = 78364164096
//			16 = 470184984576
//			17 = 2821109907456
//			18 = 16926659444736
//			19 = 101559956668416
//			20 = 609359740010496
	
	public int bfs(List<Integer> config) {
		
		//if(isInitial(config)) return 0;
		Set<String> visited = new HashSet<String>();
		Map<String, Integer> distance = new HashMap<String, Integer>();
		Queue<List<Integer>> q = new LinkedList<List<Integer>>();
		
		List<Integer> start = new ArrayList<Integer>(config);
		
		q.add(start);
		distance.put(stateName(start), 0);
		visited.add(stateName(start));
		
		while(q.isEmpty()==false) {
			List<Integer> current = q.poll();
			//println("current: "+stateName(current)+" Level:"+distance.get(stateName(current)));
			if(isInitial(current))
				break;			
			List<List<Integer>> adjacents = getAdjacent(current);
			visited.add(stateName(current));
			for(List<Integer> adjacent:adjacents) {
				//println("adj: "+stateName(adjacent));
				if(visited.contains(stateName(adjacent))==false) {
					
					int d = distance.get(stateName(current))+1;
					distance.put(stateName(adjacent), d);
					q.add(adjacent);
				}
			}
		}
		
		return distance.get(stateName(getGoal()));
	}
	
	List<Integer> getGoal() {
		List<Integer> l = new ArrayList<Integer>();
		for(int i=1;i<=size;i++) {
			l.add(i);
		}
		return l;
	}
	
	public void solution(InputStream inStream) {
		Scanner in = new Scanner(inStream);
		
		int totalTestCase = in.nextInt();
		
		for(int testCase=0;testCase<totalTestCase;testCase++) {
		
			List<Integer> config = new ArrayList<Integer>();
			for(int i=0;i<size;i++) {
				config.add(in.nextInt());
			}
//			println(config.hashCode());
//			List<Integer> newConfig = new ArrayList<Integer>(config);
//			println(newConfig.hashCode());
//			println(stateName(config));
			int x = aStar(config);
			println(x);
			
		}
		
		
	}
	public int costFunc(List<Integer> config1, List<Integer> config2) {
		int cost = 0;
		for(int i=0;i<config1.size();i++) {
			if(config1.get(i)!=config2.get(i))
				cost++;
		}
		return cost;
	}
	class Config {
		List<Integer> config = null;
		
		int hCost;
		int cost;
		int fx;
		String stateName = "";
				
		public Config(List<Integer> config, int d) {
			this.config = new ArrayList<Integer>(config);
			cost = d;
			hCost = calculateHCost();
			fx = cost + hCost;
			stateName = stateName(config);
			
		}
		
		private int calculateHCost() {
			int c = 0;
			for(int i=0;i<config.size();i++) {
				if(config.get(i)!=(i+1))
					c++;
			}
			return c;
		}
		public int getCost() {
			return cost;
		}
		public int getFx() {
			return fx;
		}
		public int gethCost() {
			return hCost;
		}
		public void setCost(int cost) {
			this.cost = cost;
		}
		public String getStateName() {
			return stateName;
		}
		public List<Integer> getConfig() {
			return config;
		}
		public void setConfig(List<Integer> config) {
			this.config = config;
		}
		public int getHCost() {
			return hCost;
		}
		public void setHCost(int cost) {
			this.hCost = cost;
		}
		
	}
	class ConfigComparator implements Comparator<Config> {

		@Override
		public int compare(Config o1, Config o2) {
			// TODO Auto-generated method stub
			return o1.getFx()-o2.getFx();
		}
		
	}
	
	List<Config> getAdjacent(Config config, int d) {
		List<Config> result = new ArrayList<Config>();
		
		result.add(new Config(rotate(config.getConfig(), leftRingClockConfigIndex), d));
		result.add(new Config(rotate(config.getConfig(), leftRingCounterClockConfigIndex), d));
		result.add(new Config(rotate(config.getConfig(), counterClockWhole),d));
		result.add(new Config(rotate(config.getConfig(), clockWhole),d));
		result.add(new Config(rotate(config.getConfig(), rightRingClockConfigIndex),d));
		result.add(new Config(rotate(config.getConfig(), rightRingCounterClockConfigIndex),d));
		
		return result;
	}
	public Config getGoalConfig(){
		List<Integer> l = new ArrayList<Integer>();
		for(int i=1;i<=size;i++) {
			l.add(i);
		}
		Config c = new Config(l,-1);
		return c;
	}
	
	public int aStar(List<Integer> config) {
		
		if(isInitial(config)) return 0;
		Set<String> visited = new HashSet<String>();
		Map<String, Integer> g_score = new HashMap<String, Integer>();
		Map<String, Integer> f_score = new HashMap<String, Integer>();
		ConfigComparator cmp = new ConfigComparator();
		Queue<Config> q = new PriorityQueue<Config>(10077696,cmp);
		Map<String, Config> inQ = new HashMap<String, Config>();
		
		//List<Integer> start = new ArrayList<Integer>(config);
		Config start = new Config(config,0);
		Config goal = getGoalConfig(); 
		q.add(start);
		g_score.put(start.getStateName(), start.getCost());
		f_score.put(start.getStateName(), start.getFx());
		visited.add(start.getStateName());
		inQ.put(start.getStateName(), start);
		
		while(q.isEmpty()==false) {
			Config current = q.poll();
			inQ.remove(current.getStateName());
			
			//println("current: "+current.getStateName()+" Level:"+distance.get(current.getStateName()));
			if(isInitial(current.getConfig()))
				break;
			int tentative_g_score = g_score.get(current.getStateName())+1;
			List<Config> adjacents = getAdjacent(current,tentative_g_score);
			visited.add(current.getStateName());
			
			for(Config adjacent:adjacents) {
				//println("adj: "+adjacent.getStateName());
				if(visited.contains(adjacent.getStateName())==false) {
					
					if(inQ.containsKey(adjacent.getStateName())==false) {
						
						g_score.put(adjacent.getStateName(), tentative_g_score);
						f_score.put(adjacent.getStateName(), adjacent.getFx());
						q.add(adjacent);
						inQ.put(adjacent.getStateName(), adjacent);
					}
					else {
						Config cn = inQ.get(adjacent.getStateName());
						
						//if(adjacent.getCost()<=cn.getCost()) {
						if(tentative_g_score <= g_score.get(adjacent.getStateName())) {
							q.remove(cn);
							inQ.remove(cn.getStateName());
							
							g_score.put(adjacent.getStateName(), tentative_g_score);
							f_score.put(adjacent.getStateName(), adjacent.getFx());
							q.add(adjacent);
							inQ.put(adjacent.getStateName(), adjacent);
						}
						
					}
				}
			}
		}
		return g_score.get(goal.getStateName());
	}

	// common functions below, don't edit	
	
	public void printIntArray(int[] array) {
		print("[");
		boolean printComma = false;
		for(int x:array) {
			if(printComma) {
				print(",");
			}
			print(x);
			printComma = true;
		}
		println("]");
	}

	
	
	PrintWriter out = new PrintWriter(System.out);
	
	public void println(String x) {
		
		out.println(x);
		out.flush();
	}
	
	public void println(int x)  {
		out.println(x);
		out.flush();
		
	}
	
	public void print(String x)  {
		
		out.print(x);
		out.flush();
	}
	public void print(int x)  {
		out.print(x);
		out.flush();
		
	}
	

}

