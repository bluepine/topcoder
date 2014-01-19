package fr.inria.wimmics.uva;


import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Scanner;

class UVA10300 {
	
	//OutputStream out = new DataOutputStream(System.out);
	PrintWriter out = new PrintWriter(System.out);
	
	public void println(String str) {
		
		out.println(str);
		out.flush();
	}
	
	public void println(int i)  {
		println(String.valueOf(i));
	}
	
	public void print(String str)  {
		
		out.print(str);
		out.flush();
	}

	
	
	public void solution(InputStream inStream) {
		Scanner in = new Scanner(inStream);
		
		while(in.hasNext()) {
			int testCases = in.nextInt();
			for(int cas=1;cas<=testCases;cas++) {
				int nFar = in.nextInt();
				int sum = 0;
				for(int farmer=0;farmer<nFar;farmer++) {
					int farmlandSize = in.nextInt();
					int nAnimal = in.nextInt();
					int envVal = in.nextInt();
					
					int score = farmlandSize * envVal;
					sum += score;
				}
				println(sum);
				
				
			}
		}
	} 
	

}