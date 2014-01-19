package fr.inria.wimmics.uva;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class UVA299 {
	
	
	public static void main(String[] args) throws Exception {
		//File file = new File("in/299.in");
		//Scanner scanner = new Scanner(file);
		Scanner scanner = new Scanner(System.in);
		
		int N = scanner.nextInt();
		
		
		for(int i=0;i<N;i++) {
			int swap = 0;
			int L = scanner.nextInt();
			List<Integer> list = new ArrayList<Integer>();
			for(int j=0;j<L;j++) {
				int x = scanner.nextInt();
				list.add(x);

				
				for(int k=0;k<list.size();k++) {
					for(int l=k+1;l<list.size();l++) {
						if(list.get(k)>list.get(l)) {
							int t = list.get(k);
							list.set(k, list.get(l));
							list.set(l,t);
							swap++;
						}
					}
				}
			
			}
			System.out.println("Optimal train swapping takes "+swap+" swaps.");
			
		}
		
	}

}
