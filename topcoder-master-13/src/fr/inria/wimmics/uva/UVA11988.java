package fr.inria.wimmics.uva;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Scanner;





//class Main {
//	public static void main(String[] args) throws FileNotFoundException {
//
//		
//		
//		UVA11988 sol = new UVA11988();
//		
//		//FileInputStream fis = new FileInputStream("in/11988.in");
//		//sol.solution(fis);
//		sol.solution(System.in);
//	}	
//}

class UVA11988 {
	public void solution(InputStream inStream) {

		Scanner in = new Scanner(inStream);
		 Deque<String> queue = new LinkedList<String>(); 

		StringBuffer sb = new StringBuffer();
		StringBuffer finalSb = new StringBuffer();		 
		while (in.hasNext()) {
			
			String str = in.next();
			queue.clear();
			finalSb.setLength(0);
			sb.setLength(0);
			
			boolean home = false;

			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) == '[') {
					if(home)queue.addFirst(sb.toString());
					else queue.addLast(sb.toString());
					sb.setLength(0);
					home = true;
					
				} 
				else if (str.charAt(i) == ']') {
					if(home)queue.addFirst(sb.toString());
					else queue.addLast(sb.toString());
					sb.setLength(0);
					home = false;
					
				}
				else {
					sb.append(str.charAt(i));
				}
			}
			if(sb.length()>0) {
				if(home)queue.addFirst(sb.toString());
				else queue.addLast(sb.toString());				
			}
			
			while(queue.isEmpty()==false) {
				finalSb.append(queue.poll());
			}
			System.out.println(finalSb.toString());
			
		}
	}
}
