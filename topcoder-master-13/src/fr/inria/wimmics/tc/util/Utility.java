package fr.inria.wimmics.tc.util;

import java.util.Arrays;
import java.util.Comparator;

public class Utility {
	
	/**
	 * Integer comparator for sort and other built-in algorithms
	 * @author hrakebul
	 *
	 */
	class IntegerComparator implements Comparator<Integer> {

	    @Override
	    public int compare(Integer o1, Integer o2) {
	        return o2.compareTo(o1);
	    }
	}
	
	/**
	 * copies the content of a string array into a 2D char matrix
	 * @param array
	 * @return
	 */
	public char[][] copyStringArray2CharMatrix(String[] array) {

		if(array.length==0) return null;
		char tmpArray[][] = new char[array.length][array[0].length()];
		
		for(int i=0;i<array.length;i++) {
			for(int j=0;j<array[0].length();j++) {
				tmpArray[i][j] = array[i].charAt(j);
			}
		}
		
		return tmpArray;
		
	}
	

	
	/************* Prime GCD LCM *****************/
	
	/**
	 * The Sieve of Eratosthenes
	 * prime[n] = true or false
	 * @param n
	 * @return
	 */
	
	public boolean[] sieve(int n)
	{
	   boolean[] prime=new boolean[n+1];
	   Arrays.fill(prime,true);
	   prime[0]=false;
	   prime[1]=false;
	   int m=(int) Math.sqrt(n);

	   for (int i=2; i<=m; i++)
	      if (prime[i])
	         for (int k=i*i; k<=n; k+=i)
	            prime[k]=false;

	   return prime;
	}
	/**
	 * assume that a and b cannot both be 0
	 * @param a
	 * @param b
	 * @return
	 */
	public int GCD(int a, int b)
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
	public int LCM(int a, int b)
	{
	   return b*a/GCD(a,b);
	}
	
	
	/************* Base conversion *****************/	
	/**
	 * converts a number n in base b (2<=b<=10) to a decimal number
	 * can be also done in java as: Integer.parseInt(""+n,b);
	 * @param n
	 * @param b
	 * @return
	 */
	public int toDecimal(int n, int b)
	{
	   int result=0;
	   int multiplier=1;
	      
	   while(n>0)
	   {
	      result+=n%10*multiplier;
	      multiplier*=b;
	      n/=10;
	   }
	      
	   return result;
	}	
	
	/**
	 * convert from a decimal to any base (up to base 20)
	 * 
	 * in java there are functions for the followings:
	 * Integer.toBinaryString(n);
	 * Integer.toOctalString(n);
	 * Integer.toHexString(n);
	 * @param n
	 * @param b
	 * @return
	 */
	
	public String fromDecimal2(int n, int b)
	{
	   String chars="0123456789ABCDEFGHIJ";
	   String result="";
	      
	   while(n>0)
	   {
	      result=chars.charAt(n%b) + result;
	      n/=b;
	   }
	      
	   return result;
	}	
	
}
