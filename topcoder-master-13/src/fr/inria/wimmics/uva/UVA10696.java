package fr.inria.wimmics.uva;

import java.io.InputStream;
import java.util.Scanner;

//TLE with java but accepted with the same code in C++
class UVA10696 {
	
	int size = 101;
	int[] table = new int[size];
	
	public int f91(int n) {
		if(n>100) return n-10;
		if(table[n] != Integer.MIN_VALUE) {
			return table[n]; 
		}
		table[n] = f91(f91(n+11));
		return table[n];

	}
	
	public void computeF91() {
		
		for(int i=0;i<size;i++) {
			table[i] = Integer.MIN_VALUE;
		}
		
		for(int n=100;n>0;n--) {
			table[n] = f91(n);
		}
	}
	
	public void solution(InputStream inStream) {

		Scanner in = new Scanner(inStream);

		computeF91();
		
		while(in.hasNext()) {
			int n = in.nextInt();
			if(n==0) break;
			int res = f91(n);
			System.out.println("f91("+n+") = "+res);
		}
		
	}
	

}
