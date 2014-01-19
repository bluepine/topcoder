package fr.inria.wimmics.uva;





import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;


//class Main {
//	public static void main(String[] args) throws FileNotFoundException {
//
//		
//		
//		UVA11559 sol = new UVA11559();
//		
//		//FileInputStream fis = new FileInputStream("in/11559.in");
//		//sol.solution(fis);
//		sol.solution(System.in);
//	}	
//}

class UVA11559 {
	
	public void solution(InputStream inStream) {
		
		Scanner in = new Scanner(inStream);
		
		while(in.hasNext()) {
			int n_people = in.nextInt();
			int b_budget = in.nextInt();
			int h_hotels = in.nextInt();
			int w_weekends = in.nextInt();
			
			int min = Integer.MAX_VALUE;
			for(int i=0;i<h_hotels;i++) {
				int p_price = in.nextInt();
				for(int j=0;j<w_weekends;j++) {
					int a_beds = in.nextInt();
					int total = n_people*p_price;
					if(n_people<=a_beds &&  total <= b_budget && min>total) {
						min = total;
					}
				}
			}
			System.out.println(min==Integer.MAX_VALUE?"stay home":min);
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


