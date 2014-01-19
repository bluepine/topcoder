import java.util.*;
import java.io.*;
public class CodeJam {
    public static void main(String args[]) {
        char[][] board = new char[4][4];
        Scanner in = new Scanner(System.in);
        int numOfRecords = in.nextInt();
        String[] result = new String[numOfRecords + 1];
        String line = null;
        TicTac t = new TicTac();
        for(int k = 1; k <= numOfRecords; k++){
            in.nextLine();
            for(int i = 0; i < 4; i++) {
                line = in.next();
                for(int j = 0; j < 4; j++) {
                    board[i][j] = line.charAt(j);
                }
            }
            result[k] = t.play(board); 
        }
        for(int r = 1; r <= numOfRecords; r++) {
            System.out.println("Case #"+r+": "+result[r]);
        }
    }
}

class TicTac {
    public String play(char[][] board){
        int countX = 0, countO = 0, diacountX = 0, diacountO = 0, negdiacountX = 0, negdiacountO = 0;
        boolean emptystatus = false;
        String status = "Draw";
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                if(board[i][j] == 'X' || board[i][j] == 'T'){
                    countX++;
                }
                if(board[i][j] == 'O' || board[i][j] == 'T'){
                    countO++;
                }
                if(board[i][j] == '.'){
                    emptystatus = true;
                }
            }
            if (countX == 4) {
                status = "X won";
                return status;
            }
            if (countO == 4){
                status = "O won";
                return status;
            }
            countX = 0;
            countO = 0;
        }
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                if(board[j][i] == 'X' || board[j][i] == 'T'){
                    countX++;
                }
                if(board[j][i] == 'O' || board[j][i] == 'T'){
                    countO++;
                }
                if(board[j][i] == '.'){
                    emptystatus = true;
                }
            }
            if (countX == 4) {
                status = "X won";
                return status;
            }
            if (countO == 4){
                status = "O won";
                return status;
            }
            countX = 0;
            countO = 0;
        }
        for(int k = 0; k < 4; k++){
            if(board[k][k] == 'X' || board[k][k] == 'T') {
                diacountX++;
            }
            if(board[k][k] == 'O' || board[k][k] == 'T') {
                diacountO++;
            }
            if(board[k][k] == '.') {
                emptystatus = true;
            }
        }
        for(int k = 3, offset = 0; k >= 0; k--, offset++) {
            if(board[offset][k] == 'X' || board[offset][k] == 'T') {
                negdiacountX++;
            }
            if(board[offset][k] == 'O' || board[offset][k] == 'T') {
                negdiacountO++;
            }
            if(board[k][k] == '.') {
                emptystatus = true;
            }
        }
        if (diacountX == 4 || negdiacountX == 4) {
            status = "X won";
            return status;
        }
        else if(diacountO == 4 || negdiacountO == 4) {
            status = "O won";
            return status;
        }
        else if(emptystatus){
            return "Game has not completed";
        }
        else {
            status = "Draw";
        }

        return status;

    }
}
