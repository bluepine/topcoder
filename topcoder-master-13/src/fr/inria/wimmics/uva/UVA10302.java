package fr.inria.wimmics.uva;


import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Scanner;



class UVA10302 {
	

	
	public BigInteger polySum3(int x) {
		//return ((n*(n+1))*(n*(n+1)))/4;
		BigInteger n = new BigInteger(String.valueOf(x));
		BigInteger s1 = n.add(new BigInteger("1"));
		BigInteger s2 = n.multiply(s1);
		BigInteger s3 = s2.multiply(s2);
		return s3.shiftRight(2);
	}
	
	public void solution(InputStream inStream) {
		Scanner in = new Scanner(inStream);
		
		while(in.hasNext()) {
			int x = in.nextInt();
			BigInteger res = polySum3(x);
			println(res.toString());
		}

	} 

	

	// common functions below, don't edit	
	
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

