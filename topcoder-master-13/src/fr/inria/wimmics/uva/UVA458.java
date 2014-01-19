
package fr.inria.wimmics.uva;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;


//working file... uva accepts only UVA458.java file and class UVA458
//copy past here the working problem
public class UVA458 {
	
	void processInput(DataInputStream in) throws Exception {
		DataOutputStream o = new DataOutputStream(System.out);
		byte c;
		
		while(true) {
			try {
				c=in.readByte();
				byte secret = 7;
				if(c!=10 && c!=13){
					byte db = (byte) (c-secret);
					o.write(db);
				}
				else
					o.write(c);
				//System.out.print("[###]");
				o.flush();
			}
			catch (EOFException e) {
				break;
			}
		}
	}	
	

	public static void main(String[] args) throws Exception {
		File file = new File("/Users/hrakebul/Documents/workspace/topcoder/in/458.in");
		FileInputStream fileIs = new FileInputStream(file);
		DataInputStream ds = new DataInputStream(fileIs);
		//DataInputStream ds = new DataInputStream(System.in);
		UVA458 m = new UVA458();
		m.processInput(ds);

	}
}