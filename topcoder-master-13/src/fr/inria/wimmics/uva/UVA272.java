package fr.inria.wimmics.uva;

//import java.io.File;
//import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

public class UVA272 {
	
	void processInput(InputStream in) throws Exception {
		Scanner inFile = new Scanner(in);
		boolean start = true;
		while(inFile.hasNextLine()) {
			String line = inFile.nextLine();
			for(int i=0;i<line.length();i++){
				if(line.charAt(i)=='"') {
					if(start){
						start = false;
						System.out.print("``");
					}
					else {
						start = true;
						System.out.print("''");
					}
				}
				else{
					System.out.print(line.charAt(i));
				}
			}
			System.out.println();
		}
	}


	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//File file = new File("/Users/hrakebul/Documents/workspace/topcoder/in/272.in");
		//FileInputStream fileIs = new FileInputStream(file);
		UVA272 a = new UVA272();
		//a.processInput(fileIs);
		a.processInput(System.in);

	}

}
