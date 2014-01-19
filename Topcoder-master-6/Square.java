import java.util.*;
public class Square {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        long start = 0;
        long end = 0;
        int numOfRecords = in.nextInt();
        int[] result = new int[numOfRecords + 1];
        String line = null;
        Paliandrom t = new Paliandrom();
        for(int k = 1; k <= numOfRecords; k++){
            int count = 0;
            for(int j = 0; j < 1; j++){
                start = in.nextLong();
                end = in.nextLong();
            }
            for (long i = start; i <= end; i++){
                if( t.isPalidrom(i)  && t.isPalidrom(t.isSquare(i))) {
                    count++;
                }
            }
            result[k] = count;
            count = 0;
        }
        for(int r = 1; r <= numOfRecords; r++) {
            System.out.println("Case #"+r+": "+result[r]);
        }
    }
}

class Paliandrom {
    public boolean isPalidrom(long number){
        long pal = number;
        long rev = 0;
        if(number <= 0) {
            return false;
        }
        if(number < 10) {
            return true;
        }
        while(pal != 0) {
            long rem = pal % 10;
            rev = rev * 10 + rem;
            pal = pal / 10;
        }
        if( rev == number) {
            return true;
        }

        return false;
    }

    public long isSquare(long number) {
        long num = 0;
        if ( number == 1 ) {
            return 1;
        }
        for(long i = 0; i <= number/2; i++){
            if((i * i) == number) {
                num = i;
            }
        }
        return num;
    }
}
