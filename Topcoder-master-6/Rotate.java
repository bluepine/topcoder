import java.util.*;
public class Rotate {
    public static void main(String args[]){
        Scanner in = new Scanner(System.in);
        int TestCases = in.nextInt();
        String[] result = new String[TestCases+1];
        String line = null;
        RotateBoard t = new RotateBoard();
        for(int k = 1; k < TestCases + 1; k++){
            int N = in.nextInt();
            int K = in.nextInt();
            char[][] board = new char[N][N]; 
            for(int i = 0; i < N; i++){
                line = in.next();
                for(int j = 0; j < N; j++){
                    board[i][j] = line.charAt(j);
                }
            }
            result[k] = t.play(board, K);
        }
         
        for(int r = 1; r < result.length; r++){
            System.out.println("Case #"+r+": "+result[r]);
        }
    }
}
class RotateBoard {
    public String play(char[][] board, int K) {
        int N = board.length;
        char[][] transboard = new char[N][N];
        int countR = 0; 
        int countB = 0;
        boolean winR = false;
        boolean winB = false;
        //Finding the transpose
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                transboard[j][i] = board[i][j];
            }
        }
        //Gravity
        for(int g = N - 1; g >= 0; g--) {
            for(int col = 0; col < N; col++){
                if(transboard[g][col] == '.'){
                    int i = g;
                    while(transboard[i][col] == '.' && i > 0){
                        i--;
                    }
                    transboard[g][col] = transboard[i][col];
                    transboard[i][col] = '.'; 
                }
            }
        }
        //Row
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < (N - K) + 1 ; j++) {
                if(transboard[i][j] == 'R') {
                    for(int k = j; k < K + j;k++){
                        if(transboard[i][k] == 'R') {
                            countR++;
                        }
                    }
                }
                if(transboard[i][j] == 'B') {
                    for(int k = j; k < K + j; k++){
                        if(transboard[i][k] == 'B') {
                            countB++;
                        }
                    }
                }
                if( countR == K ) {
                    winR = true;
                }
                if( countB == K ) {
                    winB = true;
                }
            } 
            countB = 0;
            countR = 0;
        }
        //col
        for(int i = 0; i < N; i++){
            for(int j = 0; j < (N - K) + 1; j++){
                if(transboard[j][i] == 'R'){
                    for(int k = j; k < K + j; k++) {
                        if(transboard[k][i] == 'R') {
                            countR++;
                        }
                    }
                }
                if(transboard[j][i] == 'B') {
                    for(int k = j; k < K + j; k++) {
                        if(transboard[k][i] == 'B') {
                            countB++;
                        }
                    }
                }
                if(countR == K) {
                    winR = true;
                }
                if(countB == K) {
                    winB = true;
                }
            }
            countB = 0;
            countR = 0;
        }
        //diagonal
        for(int i = 0; i < N - K + 1; i++){
            for(int j = i + 1; j < K + i; j++){
                if(!winR) {
                    if(transboard[i][j] == 'R'){
                        for(int k = i; k < N - K + 1; k++){
                            for(int f = j; f < K + k; f++) {
                                if(transboard[k][f] == 'R')
                                    countR++;
                            }
                            if(countR == K)
                                winR = true;

                            countR = 0;
                        }
                    }
                }
                if(!winB){
                    if(transboard[i][j] == 'B'){
                        for(int k = i; k < N - K + 1; k++) {
                            for(int f = j; f < K + k; f++) {
                                if(transboard[k][f] == 'R')
                                    countB++;
                            }
                            if(countB == K)
                                winB = true;

                            countB = 0;
                        }
                    }
                }
            }
            countR = 0;
            countB = 0;
        } 
        for(int i = 0; i < (N - K) + 1; i++) {
            for(int j = N - 1; j >= (N - K) + 1; j--) {
                if(!winR) {
                    if(transboard[i][j] == 'R') {
                        for(int f = j, k = i; f > j - K && f >= 0; f--, k++) {
                            if(transboard[k][f] == 'R')
                                countR++;
                        }
                        if(countR == K)
                            winR = true;
                
                        countR = 0;
                    }
                }
                if(!winB) {
                    if(transboard[i][j] == 'B'){
                        for(int k = i, f = j; f > j - K && f >= 0; k++, f--){
                            if(transboard[k][f] == 'B') {
                                countB++;    
                            }
                        }
                        
                        if(countB == K)
                            winB = true;
   
                        countB = 0;
                    }
                }
            }
            countR = 0;
            countB = 0;
        }
        if( winB && winR ) {
            return "Both";
        }
        else if( winB ) { 
            return "Blue";
        }
        else if( winR ) {
            return "Red";
        }
        else {
            return "Neither";
        }
    }
}
