// BEGIN CUT HERE
/*
// PROBLEM STATEMENT
// M integers are randomly independently chosen from the interval lowerBound...upperBound, inclusive.  Return the probability that the K-th smallest element of the generated set is equal to N.  K=1 refers to the smallest element, K=2 refers to the second smallest element, and so on.


DEFINITION
Class:KthProbableElement
Method:probability
Parameters:int, int, int, int, int
Returns:double
Method signature:double probability(int M, int lowerBound, int upperBound, int N, int K)


NOTES
-The returned value must have an absolute or relative error less than 1e-9.


CONSTRAINTS
-M will be between 1 and 100, inclusive.
-lowerBound will be between 1 and 1000, inclusive.
-upperBound will be between lowerBound and 1000, inclusive.
-N will be between lowerBound and upperBound, inclusive.
-K will be between 1 and M, inclusive.


EXAMPLES

0)
1
1
10
3
1

Returns: 0.1

The probability that the only chosen number will be equal to 3 is 0.1.

1)
3
1
2
2
2

Returns: 0.5

There are 8 ways to choose 3 numbers from the interval 1..2:
Numbers | 2nd smallest element
 1 1 1  |  1
 1 1 2  |  1
 1 2 1  |  1
 1 2 2  |  2
 2 1 1  |  1
 2 1 2  |  2
 2 2 1  |  2
 2 2 2  |  2
Exactly 4 of the ways have the 2nd smallest element equal to 2.

2)
3
1
3
2
2

Returns: 0.48148148148148145

There are 27 ways to choose 3 numbers from the interval 1..3, 13 of them have the 2nd smallest element equal to 2.

3)
10
1
10
1
10

Returns: 1.0000000000000003E-10

Only one of 1010 ways to choose 10 numbers from the interval 1..10 has 1 as the 10th smallest element.

4)
4
61
65
62
3

Returns: 0.15200000000000002



*/
// END CUT HERE
import java.util.*;
public class KthProbableElement
{
	public double probability(int M, int l, int u, int N, int K)
	{
        double res = 1.0;
        int s = u - l + 1;
        for (int i = 0; i < K - 1; ++i)
        {
            res = res * (double)(N - l) / (double)s;
        }
        return res * (double)K / (double)s;
	}

	/*
	public static void main(String[] args) 
	{
		KthProbableElement temp = new KthProbableElement();
		System.out.println(temp.probability(int M, int lowerBound, int upperBound, int N, int K));
	}
	*/
}
