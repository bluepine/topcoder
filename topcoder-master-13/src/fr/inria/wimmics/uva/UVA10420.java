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
//		UVA10420 sol = new UVA10420();
//		
//		FileInputStream fis = new FileInputStream("in/10420.in");
//		sol.solution(fis);
//		//sol.solution(System.in);
//	}	
//}

class UVA10420 {
	
	public void solution(InputStream inStream) {
		
		Scanner in = new Scanner(inStream);

		int n = in.nextInt();
		HashMap<String, Integer> countries = new HashMap<String, Integer>();
		for(int i=0;i<n;i++) {
			String country = in.next();
			//System.out.prin
			String name = in.nextLine();
			if(countries.containsKey(country)) {
				int val = countries.get(country);
				countries.put(country, val+1);
			} else {
				countries.put(country, 1);
			}
			
			
		}
		
		ArrayList<String> list = new ArrayList(countries.keySet());
		Collections.sort(list);
		
		for(int i=0;i<list.size();i++) {
			System.out.println(list.get(i)+" "+ countries.get(list.get(i)));
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


