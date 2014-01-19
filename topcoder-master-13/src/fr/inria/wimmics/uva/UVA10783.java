package fr.inria.wimmics.uva;

import java.io.InputStream;
import java.util.Scanner;

class UVA10783 {
	
	public int oddSum(int n) {
		if(n%2==0) {
			return (n*n)/4;
		}
		
		return ((n+1)*(n+1))/4;
	}
	public int beforeOddSum(int a) {
		int beforeN = a - 1;
		if(beforeN<=1) return 0;
		return oddSum(beforeN);
	}
	
	public void solution(InputStream inStream) {
			Scanner in = new Scanner(inStream);
			int cases = in.nextInt();
			
			for(int cas=1;cas<=cases;cas++) {
				int a = in.nextInt();
				int b = in.nextInt();
				int beforeSum = beforeOddSum(a);
				int totalSum = oddSum(b);
				int res = totalSum - beforeSum;
				System.out.println("Case "+cas+": "+res);
			}
		
	}
	

}
