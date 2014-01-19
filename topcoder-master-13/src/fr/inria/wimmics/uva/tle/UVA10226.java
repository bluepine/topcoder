package fr.inria.wimmics.uva.tle;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.Formatter;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;


class Main {
	public static void main(String[] args) throws FileNotFoundException {

		
		
		UVA10226 sol = new UVA10226();
		
		//FileInputStream fis = new FileInputStream("in/10226.in");
		//sol.solution(fis);
		sol.solution(System.in);
	}	
}

class UVA10226 {
	
	
	class MyComparator implements Comparator<String> {
		@Override
		public int compare(String o1, String o2) {
			
			return o1.compareTo(o2);
		}	    
	}


	public void solution(InputStream inStream) {
		
		Scanner in = new Scanner(inStream);
		
		int testCases = in.nextInt();
		int caseNumber = 0;
		String line = in.nextLine(); //return the rest of the current line
		line = in.nextLine(); // return the current line.. blank line
		
		while(caseNumber<testCases) {
			if(caseNumber>0) 
				println("");
			caseNumber++;
			int totalPopulation = 0;
			Map<String,Integer> count = new TreeMap<String, Integer>(new MyComparator());
			while(in.hasNext()) {
				line = in.nextLine();
				if(line.isEmpty()) break;
				totalPopulation++;
				int val = count.containsKey(line) ? count.get(line):0;
				count.put(line, val+1);
			}
			
			
			for(String key:count.keySet()) {
				double populationPercentage = 100.00 * count.get(key).doubleValue()/totalPopulation;
				StringBuilder sb = new StringBuilder();
				   // Send all output to the Appendable object sb
				Formatter formatter = new Formatter(sb);

				//System.out.printf("%s %.4f%n",key, populationPercentage);
				formatter.format("%s %.4f%n",key, populationPercentage);
				print(sb.toString());
			}
		}
		
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
