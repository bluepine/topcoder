//Jack Young
//Facebook Practice SOlution



import java.io.BufferedReader;
import java.io.InputStreamReader;

public class HTower {
	
		public static int moves = 0;
		public static int totalDisks = 0;
		public static int[][] aArray = new int[0][0];
		
		public static int[][] getaArray() {
			return aArray;
		}
		public static void setaArray(int[][] aArray) {
			HTower.aArray = aArray;
		}
		
		public static void addToArray(int el, int el2){
			setaArray(expandArray(getaArray(), el, el2));
		}
		
		public static int[][] expandArray(int[][] old, int newElement1, int newElement2){
			int[][] newArray = new int[old.length + 1][2];
			int index = 0;
			for(int row = 0; row < old.length; row++){
				newArray[row][0] = old[row][0];
				newArray[row][1] = old[row][1];
				index++;
				
			}
			newArray[index][0] = newElement1;
			newArray[index][1] = newElement2;

			return newArray;
		}
		
		
		
		
		public static void main(String[] arguments) throws java.io.IOException {
			int disks;
			char fromPole = 'a';
			char withPole = 'b';
			char toPole = 'c';
			disks = getNumber("\nHow many disks are there on the tower? ");
			totalDisks = disks;
			if(totalDisks > 10){
				System.out.println("Working...");
			}
			
			
			solveHanoi(disks, fromPole, toPole, withPole, 0);
			
			System.out.println();
			System.out.println("\nAmount of moves: " + moves + "\n");
			
			
			displayGraphicalTower dgt = new displayGraphicalTower(totalDisks, 3, getaArray(), moves);
			

			
			}
		
		public static void solveHanoi(int disks, char fromPole, char toPole, char withPole, int value) {
			
			if (disks >= 1) {
				solveHanoi(disks-1, fromPole, withPole, toPole, (value + 1));
				moveDisk(fromPole, toPole);
				solveHanoi(disks-1, withPole, toPole, fromPole, -5);
				}
			}
			
		public static void moveDisk(char fromPole, char toPole) {
			moves++;
			if(totalDisks <= 10){
				System.out.println("Moved from " + fromPole + " to " + toPole + ". ");
				addToArray(number(fromPole), number(toPole));
				
				if (moves%4 == 0){
					System.out.println();
					}
				}
			}
		
		public static int number(char letter){
			if(letter == 'a'){
				return 1;
			} else if (letter == 'b'){
				return 2;
			} else {
				return 3;
			}
			
		}
		
		public static int getNumber(String question) throws java.io.IOException {
			String theNumber;
			int number = 0;
			BufferedReader in = new BufferedReader (new InputStreamReader(System.in));
			System.out.print(question);
			theNumber = in.readLine();
			System.out.println();
			number = Integer.parseInt(theNumber);
			return number;
			}
}
