package fr.inria.wimmics.uva;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


class UVA10474 {
	public static void main(String[] args) /*throws Exception*/ {
		
		
		//File file = new File("in/10474.in");
		//Scanner in = new Scanner(file);
		Scanner in = new Scanner(System.in);
		int caseCount = 1;
		while(true) {
			int n = in.nextInt();
			int q = in.nextInt();
			
			if(n==0 && q==0) break;

			int max = 10000;
			int[] count = new int[max+1];
			for(int i=0;i<n;i++) {
				int number = in.nextInt();
				count[number]++;
			}

			List<Integer> numbers = new ArrayList<Integer>();
			for(int i = 0;i<=max;i++) {
				if(count[i]>0)
					numbers.add(i);
			}
			
			int[] dp = new int[numbers.size()];
			
			int[] search = new int[max+1];
			dp[0] = 1;
			search[numbers.get(0)] = 0;
			for(int i=1;i<numbers.size();i++) {
				dp[i] = dp[i-1] + count[numbers.get(i-1)];
				search[numbers.get(i)] = i;
				//System.out.print("number i:"+numbers.get(i)+" dp:"+dp[i]+", ");
			}
			//System.out.println();
			System.out.println("CASE# "+(caseCount++)+":");
			for(int i=0;i<q;i++) {
				
				int x = in.nextInt();
				//int index = BinarySearch.find(data, x);
				//int index = Collections.binarySearch(numbers, x);
				
				if(count[x]>0) {
					int index = search[x];
					System.out.println(x+" found at "+dp[index]);
				} 
				else {
					
					System.out.println(x+" not found");
				}
			}
			
		}
		
	}
}