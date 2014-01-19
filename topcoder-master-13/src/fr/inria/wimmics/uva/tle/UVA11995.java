package fr.inria.wimmics.uva.tle;

//tle in java but ac in c++


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
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;



//class Main {
//	public static void main(String[] args) throws FileNotFoundException {
//
//		
//		
//		UVA11995 sol = new UVA11995();
//		
//		//FileInputStream fis = new FileInputStream("in/11995.in");
//		//sol.solution(fis);
//		sol.solution(System.in);
//	}	
//}

class UVA11995 {
	class MyComparator implements Comparator<Integer>
	{
	    public int compare( Integer x, Integer y )
	    {
	        return y - x;
	    }
	};
	
	
	public void solution(InputStream inStream) {
		
		Scanner in = new Scanner(inStream);
		Stack<Integer> stack = new Stack<Integer>();
		Queue<Integer> queue = new LinkedList<Integer>();
		PriorityQueue<Integer> heap = new PriorityQueue<Integer>(11,new MyComparator());
		while(in.hasNext()) {
			int n = in.nextInt();
			//System.out.println(n);
			boolean isStack = true;
			boolean isQueue = true;
			boolean isHeap = true;
			stack.clear();
			queue.clear();
			heap.clear();
			
			for(int i=0;i<n;i++) {
				int op = in.nextInt();
				int val = in.nextInt();
				
				if(op==1) {
					if(isStack) {
						stack.push(val);
					}
					if(isQueue) {
						queue.add(val);
					}
					if(isHeap) {
						heap.add(val);
					}
				} 
				else if(op==2) {
					if(stack.empty() || (isStack && stack.pop()!=val)) {
						isStack = false;
					}
					if(queue.isEmpty()|| (isQueue && queue.poll()!=val)) {
						isQueue = false;
					}
					if(heap.isEmpty() || (isHeap && heap.poll()!=val)) {
						isHeap = false;
					}
				}
				
			}
			if( (isStack && isQueue) || (isStack && isHeap) || (isQueue && isHeap)) {
				System.out.println("not sure");
			}
			else if(isStack && !isQueue && !isHeap) {
				System.out.println("stack");
			}
			else if(!isStack && isQueue && !isHeap){
				System.out.println("queue");
			}
			else if(!isStack && !isQueue && isHeap){
				System.out.println("priority queue");
			}
			else if(!isStack && !isQueue && !isHeap) {
				System.out.println("impossible");
			}			
		}

		
	}

}


