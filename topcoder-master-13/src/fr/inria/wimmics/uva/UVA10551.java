package fr.inria.wimmics.uva;





import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Scanner;


//class Main {
//	public static void main(String[] args) throws FileNotFoundException {
//
//		
//		
//		UVA10551 sol = new UVA10551();
//		
//		FileInputStream fis = new FileInputStream("in/10551.in");
//		sol.solution(fis);
//		//sol.solution(System.in);
//	}	
//}

class UVA10551 {
	
	public void solution(InputStream inStream) {
		
		Scanner in = new Scanner(inStream);
		while(in.hasNext()) {
			int base = in.nextInt();
			if(base==0) break;
			String pStr = in.next();
			String mStr = in.next();
			BigInteger p = new BigInteger(pStr,base);
			BigInteger m = new BigInteger(mStr,base);
			println(p.mod(m).toString(base));
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



