//Jack Young
//Facebook Practice Solution


import java.util.HashMap;


public class displayGraphicalTower {

	private int N;
	private int K;
	private int[][] designatedMoves;
	private int numberOfMovesItWillTake;
	
	public displayGraphicalTower(int n, int k, int[][] deMoes, int num){
		this.N = n;
		this.K = k;
		this.designatedMoves = deMoes;
		this.numberOfMovesItWillTake = num;
		
		String[][] moveCharList = convertNumToStringArray(designatedMoves); // this move number notation to actually letters, ie 1 is a and 2 is b and so forth

		Tower tower = new Tower();
		HashMap<String, int[]> hm = createInitialHashMap(N, K);
		
		int[][] arr = reCreateArray(hm, N, K);
		tower.setFirstTower(arr);
		
		System.out.println("First Tower\n---------------------\n");
		outputTower(tower.getFirstTower(), N, K);
		System.out.println("------------------------------");
		
		int ro = 0;
		Holder[] holder = new Holder[numberOfMovesItWillTake];
		for(int t = 0; t < numberOfMovesItWillTake; t++){
			holder[t] = new Holder(N, K);
			System.out.println("Move " + (t + 1) + " : " + moveCharList[ro][0] + " goes to " + moveCharList[ro][1]  +"\n----------------------");
			Mover(moveCharList[ro][0], moveCharList[ro][1], hm);
			holder[t].setMatrix(reCreateArray(hm, N, K));
			outputTower(holder[t].getMatrix(), N, K);
			System.out.println("------------------------------");
			ro++;
		}
		
	}
	
	
	public int[][] reCreateArray(HashMap<String, int[]> hm, int N, int K){
		String[] aaa = {"a", "b", "c", "d"};
		int[][] m1 = new int[N][K];
		
			for(int b = 0; b < K; b++){
				for(int l = 0; l < N; l++){
					m1[l][b] = hm.get(aaa[b])[l];
				}
			}
		return m1;
	}
	
	public void Mover(String fromPole, String toPole, HashMap<String, int[]> haMap){
		int disc;

		// this searches the pole for the high position piece
		
		int anotherSize = haMap.get(fromPole).length;
		int topIndex = -1;
		for(int y = 0; y < anotherSize; y++){
			if(topIndex == -1){
				if(haMap.get(fromPole)[y] != 0){
					topIndex = y;
				}
			}
		}
		//System.out.println("The top occupied index was found at: " + topIndex);
		
		if(topIndex == -1){
			disc = haMap.get(fromPole)[0];
			haMap.get(fromPole)[0] = 0;
		} else {
			disc = haMap.get(fromPole)[topIndex];
			haMap.get(fromPole)[topIndex] = 0;
		}
		
		// This portion searches for the lowest position to place the moving disc on the new pole
		
		//System.out.println(haMap.get(toPole).length);
		int size = haMap.get(toPole).length;
		int bottomIndex = -1;
		for(int z = size - 1; z >= 0; z--){
			if(bottomIndex == -1){
				if(haMap.get(toPole)[z] == 0){
					bottomIndex = z;
				}
			}
		}
		//System.out.println("THE found index is : " + bottomIndex);
		haMap.get(toPole)[bottomIndex] = disc;
		

		
		
	}
	

// This method create a letter that corresponds to the pole
	public String[][] convertNumToStringArray(int[][] array){
		//String[] abc = {"a", "b", "c", "d", "e", "f", "g"};
		String[][] aArray = new String[array.length][array[0].length];
		for(int row = 0; row < array.length; row++){
			for(int colm = 0; colm < array[0].length; colm++){
				if(array[row][colm] == 1){
					aArray[row][colm] = "a";
				} else if (array[row][colm] == 2) {
					aArray[row][colm] = "b";
				} else if (array[row][colm] == 3){
					aArray[row][colm] = "c";
				} else {
					aArray[row][colm] = "d";
				}
			}
		}
		
		
		return aArray;
	}
	

	
//This creates the initial tower configuration
	public int[][] createInitialTower(int N, int K){
		int initialArray[][] = new int[N][K];
		int disc = N;
		for(int row = 0; row < 2; row++){
			for(int colm = 0; colm < 3; colm++){
				if(colm == 0){
					initialArray[row][colm] = disc--;
				} else {
					initialArray[row][colm] = 0;
				}
			}
			
		}
		
		
		return initialArray;
	}

//This method simply outputs the tower	
	public void outputTower(int[][] ar, int N, int K){
		String aString = "";
		for(int x = 0; x < N; x++){
			for(int y = 0; y < K; y++){
				aString += ar[x][y] + " ";
			}
			aString += "\n";
		}
		System.out.println(aString);
	}
	
	
	public HashMap<String, int[]> createInitialHashMap(int numDisc, int numPegs){
		String[] abc = {"a", "b", "c", "d", "e", "f", "g"};
		HashMap<String, int[]> hm = new HashMap<String, int[]>();
		
		for(int x = 0; x < numPegs; x++){
			if(x == 0){
				int[] arra = {2, 1};
				hm.put(abc[x], createFilledArray(numDisc));
			} else {
			hm.put(abc[x], createBlankArray(numDisc));
			}
		}
		return hm;
	}
	
	public int[] createFilledArray(int size){
		int count = size;
		int[] array = new int[size];
		for(int x = 0; x < array.length; x++){
			array[x] = count--;
		}
		return array;
	}
	
	public int[] createBlankArray(int size){
		int[] array = new int[size];
		for(int x = 0; x < array.length; x++){
			array[x] = 0;
		}
		return array;
	}
	
	
}
