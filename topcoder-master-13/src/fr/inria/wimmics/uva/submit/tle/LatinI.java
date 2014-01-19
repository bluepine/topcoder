package fr.inria.wimmics.uva.submit.tle;






import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;


class Main {
	public static void main(String[] args) throws FileNotFoundException {

		
		
		LatinI sol = new LatinI();
		
		//FileInputStream fis = new FileInputStream("in/latin_i.in");
		//sol.solution(fis);
		sol.solution(System.in);
	}	
}

class LatinI {
	int size = 1000000;

	
	public void solution(InputStream inStream) {
		
		Scanner in = new Scanner(inStream);
		
		while(in.hasNext()) {
			int n = in.nextInt();
			int k = in.nextInt();
			int[] num = new int[n+1];
			int[] inputI = new int[n+1];
			int[] inputJ = new int[n+1];
			int inputIJCount = 0;
			int[] zeroMemo = new int[n+1];
			int[] negativeMemo = new int[n+1];
			
			
//			for(int i = 1;i<=n;i++) {
//				num[i] = in.nextInt();
//			}
			negativeMemo[0] = 0;
			zeroMemo[0] = 0;			
			for(int i = 1;i<=n;i++) {
				num[i] = in.nextInt();		
				if(num[i]<0) {
						negativeMemo[i] = negativeMemo[i-1]+1;
						zeroMemo[i] = zeroMemo[i-1];
				}
				else if(num[i]>=0) {
					
					negativeMemo[i] = negativeMemo[i-1];					
					if(num[i]==0) {
						zeroMemo[i] = zeroMemo[i-1]+1;
					}
				}
			}		
			inputIJCount = 0;
			for(int command = 0;command<k;command++) {
				//println("processing:"+command);
				String str = in.next();
				//println(str);
				if(str.equals("C")) {
					
					int ii = in.nextInt();
					int v = in.nextInt();
					
					num[ii] = v;
					if(v!=0) {
						if(v<0) {
							for(int ti=ii;ti<=n;ti++) {
								negativeMemo[ti] = negativeMemo[ti-1]+1;
								
							}
							if(zeroMemo[ii]>0) {
								for(int ti=ii;ti<=n;ti++) {
									zeroMemo[ti]--;
									
								}

							}
						}
						else if(v>0) {
							if(zeroMemo[ii]>0) {
								for(int ti=ii;ti<=n;ti++) {
									zeroMemo[ti]--;
									
								}
							}
							if(negativeMemo[ii]>0) {
								
								for(int ti=ii;ti<=n;ti++) {
									negativeMemo[ti]--;
									
								}
							}
							
						}

					}
					else if(v==0) {
						for(int ti=ii;ti<=n;ti++) {
							zeroMemo[ti] = zeroMemo[ti-1]+1;

						}
						if(negativeMemo[ii]>0) {
							for(int ti=ii;ti<=n;ti++) {
								negativeMemo[ti]--;
							}
						}
						
					}
				}
				else if(str.equals("P")) {
					int ii = in.nextInt();
					int jj = in.nextInt();
					//inputI[inputIJCount] = ii;
					//inputJ[inputIJCount] = jj;
					//inputIJCount++;
//					println("\nii: "+ii+" jj:"+jj);
//					printIntArray(num);
//					printIntArray(negativeMemo);
//					printIntArray(zeroMemo);
					
					//printIntArray(inputI);
					//printIntArray(inputJ);					

					int iCount = negativeMemo[ii-1];
					int jCount = negativeMemo[jj];
					int negativeCount = jCount - iCount;

					int iZeroCount = zeroMemo[ii-1];
					int jZeroCount = zeroMemo[jj];
					int zeroCount = jZeroCount-iZeroCount;
					
					

					if(zeroCount>0) {
						print("0");
					}
					else if(negativeCount%2==0) {
						print("+");
					}
					else if(negativeCount%2!=0) {
						print("-");
					}				
				
				}

				
			}
			
			

			//printIntArray(num);
			//printIntArray(negativeMemo);
			//printIntArray(zeroMemo);
			//printIntArray(inputI);
			//printIntArray(inputJ);
			
//			for(int i=0;i<inputIJCount;i++) {
//				int ii = inputI[i];
//				int jj = inputJ[i];
//				int iCount = negativeMemo[ii-1];
//				int jCount = negativeMemo[jj];
//				int negativeCount = jCount - iCount;
//
//				int iZeroCount = zeroMemo[ii-1];
//				int jZeroCount = zeroMemo[jj];
//				int zeroCount = jZeroCount-iZeroCount;
//				
//				
//
//				if(zeroCount>0) {
//					print("0");
//				}
//				else if(negativeCount%2==0) {
//					print("+");
//				}
//				else if(negativeCount%2!=0) {
//					print("-");
//				}				
//			}
//			
			println("");
		}
	}

	// common functions below, don't edit	
	
	public void printIntArray(int[] array) {
		print("[");
		boolean printComma = false;
		for(int x:array) {
			if(printComma) {
				print(",");
			}
			print(x);
			printComma = true;
		}
		println("]");
	}

	
	
	PrintWriter out = new PrintWriter(System.out);
	
	public void println(String x) {
		
		out.println(x);
		out.flush();
	}
	
	public void println(int x)  {
		out.println(x);
		out.flush();
		
	}
	
	public void print(String x)  {
		
		out.print(x);
		out.flush();
	}
	public void print(int x)  {
		out.print(x);
		out.flush();
		
	}
	

}


