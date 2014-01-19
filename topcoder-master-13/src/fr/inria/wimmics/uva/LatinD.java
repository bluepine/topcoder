package fr.inria.wimmics.uva;


import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;


//class Main {
//	public static void main(String[] args) {
//
//		
//		
//		LatinD sol = new LatinD();
//		
//		//FileInputStream fis = new FileInputStream("in/latin_d.in");
//		//sol.solution(fis);
//		sol.solution(System.in);
//	}	
//}

class LatinD {
	
	
	public boolean hasRepeat(int num) {
		String str = String.valueOf(num);
		
		for(int i=0;i<str.length();i++) {
			for(int j=i+1;j<str.length();j++) {
				if(str.charAt(i)==str.charAt(j))
					return true;
			}
		}
		return false;
	}
	public void generate(boolean[] num) {
		for(int i=1;i<=5000;i++) {
			num[i] = hasRepeat(i);
		}
	}
	
	public void solution(InputStream inStream) {
		
		Scanner in = new Scanner(inStream);
		boolean[] num = new boolean[5001];
		generate(num);
		while(in.hasNext()) {
			int n = in.nextInt();
			int m = in.nextInt();
			int count=0;
			for(int i=n;i<=m;i++) {
				if(num[i]) {
					count++;
				}
			}
			int res = (m-n+1) - count;
			println(res);
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

