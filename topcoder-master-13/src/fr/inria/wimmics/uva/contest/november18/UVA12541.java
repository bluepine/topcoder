package fr.inria.wimmics.uva.contest.november18;




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
import java.util.Date;



class Main {
	public static void main(String[] args) {//throws FileNotFoundException {

		
		
		UVA12541 sol = new UVA12541();
		
		//FileInputStream fis = new FileInputStream("in/h.in");
		//sol.solution(fis);
		sol.solution(System.in);
	}	
}

class UVA12541 {

	public void solution(InputStream inStream) {
		
		Scanner in = new Scanner(inStream);

		//int test=1;
		while(in.hasNext()) {
			int n = in.nextInt();
			long max = Long.MIN_VALUE;
			long min = Long.MAX_VALUE;
			String minName = "";
			String maxName = "";
			
			for(int i=0;i<n;i++) {
				String name = in.next();
				int dd = in.nextInt();
				int mm = in.nextInt();
				int yy = in.nextInt();
				Date bday = new Date(yy, mm, dd);
				Date today = new Date();
				long age = today.getTime() - bday.getTime();
				if(min>age) {
					min = age;
					minName = name;
				}
				if(max<age) {
					max = age;
					maxName = name;
				}

			}
			System.out.println(minName);
			System.out.println(maxName);
			
		}
	}

}












