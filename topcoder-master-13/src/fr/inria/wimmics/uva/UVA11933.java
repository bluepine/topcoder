package fr.inria.wimmics.uva;





import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

//
//class Main {
//	public static void main(String[] args) throws FileNotFoundException {
//
//		
//		
//		UVA11933 sol = new UVA11933();
//		
//		//FileInputStream fis = new FileInputStream("in/11993.in");
//		//sol.solution(fis);
//		sol.solution(System.in);
//	}	
//}

class UVA11933 {
	
	int size = 32;
	public void solution(InputStream inStream) {
		
		Scanner in = new Scanner(inStream);
		
		
		while(in.hasNext()) {
			int n = in.nextInt();
			if(n==0)break;
			int[] indices = new int[size+1];
			int index = 1;
			for(int i=0;i<size;i++) {
				int t = isOn(n, i);
				if(t!=0) {
					indices[index++] = i;
				}
			}
			int a = 0;
			for(int i=1;i<index;i+=2) {
				a += (1<<indices[i]);
			}
			
			int b = 0;
			for(int i=2;i<index;i+=2) {
				b += (1<<indices[i]);
			}
			
			System.out.println(a+" "+b);
		}
		
	}
	
	  private static int setBit(int S, int j) { return S | (1 << j); }

	  private static int isOn(int S, int j) { return S & (1 << j); }

	  private static int clearBit(int S, int j) { return S & ~(1 << j); }

	  private static int toggleBit(int S, int j) { return S ^ (1 << j); }

	  private static int lowBit(int S) { return S & (-S); }

	  private static int setAll(int n) { return (1 << n) - 1; }

	  private static int modulo(int x, int N) { return ((x) & (N - 1)); } // returns x % N, where N is a power of 2

	  private static int isPowerOfTwo(int x) { return (x & (x - 1)); }

	  private static int nearestPowerOfTwo(int x) { return ((int)Math.pow(2.0, (int)((Math.log((double)x) / Math.log(2.0)) + 0.5))); }

	  private static void printSet(int _S) {             // in binary representation
	    System.out.printf("S = %2d = ", _S);
	    Stack<Integer> st = new Stack<Integer>();
	    while (_S > 0) {
	      st.push(_S % 2);
	      _S /= 2;
	    }
	    while (!st.empty()) {                          // to reverse the print order
	      System.out.printf("%d", st.peek());
	      st.pop();
	    }
	    System.out.printf("\n");
	  }	

}



