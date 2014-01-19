package fr.inria.wimmics.uva;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Scanner;

class UVA10071 {


	public void solution(InputStream inStream) {
		Scanner in = new Scanner(inStream);
		while(in.hasNext()) {
			System.out.println(in.nextInt()*in.nextInt()*2);
		}
	} 
	

}
