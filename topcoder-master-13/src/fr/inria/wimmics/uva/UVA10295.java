package fr.inria.wimmics.uva;


import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class Data {
	String word;
	int number;
	public Data(String word, int number) {
		super();
		this.word = word;
		this.number = number;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
	
}

class UVA10295 {

	static void processInput(InputStream in){
		Scanner inScanner = new Scanner(in);
		
		int m = inScanner.nextInt();
		int n = inScanner.nextInt();
		//Data[] data = new Data[m];
		List<Data> data = new ArrayList<Data>();
		for(int i=0;i<m;i++) {
			String word = inScanner.next();
			int number = inScanner.nextInt();
			//System.out.println(word +" : "+number);
			//data[i] = new Data(word, number);
			data.add(new Data(word, number));
		}
		
		Comparator<Data> cmp = new Comparator<Data>() {

	        public int compare(Data o1, Data o2) {
	        	return o1.getWord().compareTo(o2.getWord());
	        }
		};

		Collections.sort(data,cmp);
		
		for(int i=0;i<n;i++) {
			int result = 0;
			while(true) {
				String word = inScanner.next();
				//System.out.print(word+",");
				if(word.equals(".")) {
					break;
				}
				Data d = new Data(word,0); 
				int index = Collections.binarySearch(data,d,cmp);
				d = null; //garbage
				if(index>=0) {
					Data foundData = data.get(index);
					result += foundData.getNumber();
				}

			}
			System.out.println(result);
		}
		
	}
	
	public static void main(String[] args) /*throws Exception*/ {
		
		//FileInputStream in = new FileInputStream("in/10295.in");
		processInput(System.in);
	}
}
