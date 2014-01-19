package fr.inria.wimmics.tc.practice;

public class FoxAndGCDLCM {
	
	/**
	 * assume that a and b cannot both be 0
	 * @param a
	 * @param b
	 * @return
	 */
	public long GCD(long a, long b)
	{
	   if (b==0) return a;
	   return GCD(b,a%b);
	}	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public long LCM(long a, long b)
	{
	   return b*a/GCD(a,b);
	}	
	
	long min(long a,long b) {
		return a>b?b:a;
	}

	public long get(long G, long L) {
		
		if(L%G!=0) return -1;
		
		long tb = L/G;
		
		long res = L+G;
		for(long i=1;i*i<=tb;i++) {
			if(tb%i==0 && GCD(i,tb/i)==1)
				res = min(res,i*G+(tb/i)*G);
		}
		
		return res;
	}
}
