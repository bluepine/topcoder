package fr.inria.wimmics.tc.practice;

public class GogoXCake {

	char cakeDish[][];
	char cakeCutter[][];
	
	int dishN = 0;
	int dishM = 0;
	
	int cutterN = 0;
	int cutterM = 0;
	
	private void init(String[] cake, String[] cutter) {

		dishN = cake.length;
		dishM = cake[0].length();
		
		cutterN = cutter.length;
		cutterM = cutter[0].length();		
		cakeDish = copyStringArray2CharMatrix(cake);
		cakeCutter = copyStringArray2CharMatrix(cutter);

	}
	
	private char[][] copyStringArray2CharMatrix(String[] array) {

		if(array.length==0) return null;
		char tmpArray[][] = new char[array.length][array[0].length()];
		
		for(int i=0;i<array.length;i++) {
			for(int j=0;j<array[0].length();j++) {
				tmpArray[i][j] = array[i].charAt(j);
			}
		}
		
		return tmpArray;
		
	}
	
	public String solve(String[] cake, String[] cutter) {
		
		init(cake, cutter);
		
		
		for(int i=0;i<=dishN-cutterN;i++) {
			for(int j=0;j<=dishM-cutterM;j++) {
				if(cutterFits(i,j)) {
					markDish(i,j);
				}
			}
		}
		
		if(countCakes()==dishN*dishM) return "YES";
		
		return "NO";
	}

	private int countCakes() {
		// TODO Auto-generated method stub
		int count = 0;
		for(int i=0;i<dishN;i++) {
			for(int j=0;j<dishM;j++) {
				if(cakeDish[i][j]=='X')
					count++;
			}
		}
		return count;
	}

	private void markDish(int i, int j) {
		// TODO Auto-generated method stub
		for(int ci=0;ci<cutterN;ci++) {
			for(int cj=0;cj<cutterM;cj++) {
				if(cakeCutter[ci][cj]==cakeDish[ci+i][cj+j] && cakeCutter[ci][cj]=='.') {
					cakeDish[ci+i][cj+j] = 'X';
				}
			}
		}

		
	}

	private boolean cutterFits(int i, int j) {
		// TODO Auto-generated method stub
		
		for(int ci=0;ci<cutterN;ci++) {
			for(int cj=0;cj<cutterM;cj++) {
				if(cakeCutter[ci][cj]=='.') {
					if(cakeCutter[ci][cj]!=cakeDish[ci+i][cj+j]) {
						return false;
					}
				}
			}
		}
		return true;
	}
}
