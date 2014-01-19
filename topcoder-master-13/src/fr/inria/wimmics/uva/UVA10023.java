package fr.inria.wimmics.uva;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Scanner;

class UVA10023 {

	
	public BigInteger sqrt(BigInteger A) 
	{
		//boolean didWork ;
		//didWork = false;
		BigInteger temp = A.shiftRight(BigInteger.valueOf(A.bitLength()).shiftRight(1).intValue()), result = null;
	    while (true)
	    {
	        result = temp.add(A.divide(temp)).shiftRight(1);
	        if (!temp.equals(result))
	            temp = result;
	        else
	            break ;
	    }
	    //didWork = false ;
	    //if (result.multiply(result).equals(A))
	       // didWork = true ;
	    
	    return result;
	}	
	
	public void solution(InputStream inStream) {
		Scanner in = new Scanner(inStream);
		
		int testCases = in.nextInt();
		
		for(int cas=1;cas<=testCases;cas++) {
			String num = in.next();
			//System.out.println("num: ["+num+"]");
			BigInteger number = new BigInteger(num);
			BigInteger res = sqrt(number);
			if(cas!=1) System.out.println();
			System.out.println(res);
		}
	}
	

}

