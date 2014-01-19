



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
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;



class Main {
	public static void main(String[] args) throws FileNotFoundException {

		
		
		J sol = new J();
		
		//FileInputStream fis = new FileInputStream("in/j.in");
		//sol.solution(fis);
		sol.solution(System.in);
	}	
}

class J {

	
	public void solution(InputStream inStream) {
		
		Scanner in = new Scanner(inStream);
		
		boolean end = false;
		int maxLen = Integer.MIN_VALUE;
		String maxWord = "";
		
		while(in.hasNext()) {
			String str = in.nextLine();
						
			String[] arr = str.trim().split("[^a-zA-Z\\d\\-]");
			
			
			
			for(String word:arr) {
				if(word.isEmpty()) continue;
				//System.out.print("["+word+", "+word.length()+"] ");
				
				if(word.equals("E-N-D")) {
					end=true;
					break;
				}
				
				if(maxLen<word.length()) {
					maxLen = word.length();
					maxWord = word;
				}
			}
			if(end) {
				//System.out.println("\nFound end");
				break;
			}
			//System.out.println();
		}
		//System.out.println("\nLen:"+maxLen);
		System.out.println(maxWord.toLowerCase());

		
	}

}

