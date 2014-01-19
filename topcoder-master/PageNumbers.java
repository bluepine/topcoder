// BEGIN CUT HERE
/*
// PROBLEM STATEMENT
// 
We have a book with N pages, numbered 1 to N.
How many times does each digit occur in the page numbers?



You are given an int N. Return a int[] with 10 elements, where for all i between 0 and 9, inclusive, the element i will be the number of times digit i occurs when we write down all the numbers between 1 and N, inclusive.


DEFINITION
Class:PageNumbers
Method:getCounts
Parameters:int
Returns:int[]
Method signature:int[] getCounts(int N)


NOTES
-You may assume that for any valid input each of the output values fits into an int.


CONSTRAINTS
-N will be between 1 and 1,000,000,000, inclusive.


EXAMPLES

0)
7

Returns: {0, 1, 1, 1, 1, 1, 1, 1, 0, 0 }

The page numbers in this case are simply 1, 2, 3, 4, 5, 6, and 7.

1)
11

Returns: {1, 4, 1, 1, 1, 1, 1, 1, 1, 1 }

In comparison to the previous case, we added the pages 8, 9, 10, and 11. Now we have each digit exactly once, except for the digit 1 that occurs four times: once in 1 and 10, and twice in 11.

2)
19

Returns: {1, 12, 2, 2, 2, 2, 2, 2, 2, 2 }

Digits 2 to 9 now occur twice each, and we have plenty of occurrences of the digit 1.

3)
999

Returns: {189, 300, 300, 300, 300, 300, 300, 300, 300, 300 }

Due to symmetry, each of the digits 1 to 9 occurs equally many times in the sequence 1,2,...,999.

4)
543212345

Returns: {429904664, 541008121, 540917467, 540117067, 533117017, 473117011, 429904664, 429904664, 429904664, 429904664 }

Watch out for the time limit.

*/
// END CUT HERE
import java.util.*;
public class PageNumbers
{
	public int[] getCounts(int n)
	{
        int[] t = new int[10];
        for (int i = 0; i < 10; ++i)
            t[i] = 0;

        String s = Integer.toString(n);

        int mult = (int) Math.pow(10, s.length()-1);
        int tmp = 1;

        for (int j = 0; j < s.length(); ++j)
        {
            int d = (int) s.charAt(j)-'0';
            int prev;

            for (int i = 1; i < d; ++i)
                t[i] += mult;

            if (j > 0)
            {
                prev = (int) s.charAt(j)-'0';
                for (int i = 0; i <= 9; ++i)
                    t[i] += mult*(tmp/prev*(prev-1));
                for (int i = 1; i <= 9; ++i)
                    t[i] += mult;
            }

            t[d] += n%(mult)+1;

            mult /= 10;
            tmp *= d;
        }

        return t;
	}

	/*
	public static void main(String[] args) 
	{
		PageNumbers temp = new PageNumbers();
		System.out.println(temp.getCounts(int N));
	}
	*/
}
