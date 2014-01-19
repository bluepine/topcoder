package fr.inria.wimmics.uva;




import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;



//class Main {
//	public static void main(String[] args) throws FileNotFoundException {
//
//		
//		
//		UVA11558 sol = new UVA11558();
//		
//		//FileInputStream fis = new FileInputStream("in/11588.in");
//		//sol.solution(fis);
//		sol.solution(System.in);
//	}	
//}

class UVA11558 {
	
	
	
	int getIndex(char a) {
		return (int)a - (int)'A';
	}
	class IntegerComparator implements Comparator<Integer> {

	    @Override
	    public int compare(Integer o1, Integer o2) {
	        return o2.compareTo(o1);
	    }
	}
	
	public void solution(InputStream inStream) {
		
		Scanner in = new Scanner(inStream);
		
		int testCases = in.nextInt();
		int caseCount = 1;
		for(caseCount = 1;caseCount<=testCases;caseCount++) {
			System.out.print("Case "+caseCount+": ");
			int r = in.nextInt();
			int c = in.nextInt();
			int m = in.nextInt();
			int n = in.nextInt();
			Integer[] count = new Integer[26];
			Arrays.fill(count, 0);
			
			for(int i=0;i<r;i++) {
				String line = in.next();
				//System.out.println(line);
				for(int j=0;j<c;j++) {
					char ch = line.charAt(j);
					count[getIndex(ch)]++;
				}
			}

			
			Arrays.sort(count,new IntegerComparator());
			
			
			//printIntArray(count);
			
			
			int first = count[0];
			int firstCount = 1;
			for(int i=1;i<26;i++) {
				if(count[i]==first) 
					firstCount++;
				else break;
			}
			int othersCount = 0;
			for(int i=firstCount;i<26;i++) {
				if(count[i]>0) {
					othersCount+=count[i];
				}
			}
			int importantCost = first * firstCount * m;
			//System.out.println(first);
			int otherCost = othersCount * n;
			//System.out.println(othersCount);
			int cost = importantCost + otherCost;
			System.out.println(cost);
		}
		
	}
	public void printIntArray(Integer[] array) {
		System.out.print("[");
		boolean printComma = false;
		for(int x:array) {
			if(printComma) {
				System.out.print(",");
			}
			System.out.print(x);
			printComma = true;
		}
		System.out.println("]");
	}	

}


