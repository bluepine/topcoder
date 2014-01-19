package fr.inria.wimmics.uva;


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
//	public static void main(String[] args) throws FileNotFoundException  {
//
//		
//		
//		LatinH sol = new LatinH();
//		
////		FileInputStream fis = new FileInputStream("in/latin_h.in");
////		sol.solution(fis);
//		sol.solution(System.in);
//	}	
//}

class LatinH {
	public void solution(InputStream inStream) {
		Scanner in = new Scanner(inStream);
		
		while(in.hasNext()!=false) {
			int x = in.nextInt();
			if(x%6==0) {
				println("Y");
				
			}
			else {
				println("N");
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


