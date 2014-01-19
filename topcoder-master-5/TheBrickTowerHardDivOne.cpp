/*
// BEGIN CUT HERE
// PROBLEM STATEMENT
// 
John and Brus are building towers using toy bricks.
They have an unlimited supply of bricks of C different colors.
Each brick is a 1x1x1 cube.
A tower of height X is a 2x2xX rectangular prism, built using 4X bricks.



John and Brus want their towers to look nice.
A tower is nice if it has the following two properties:

There are at most K pairs of neighboring bricks with the same color. (Two bricks are neighboring if they share a common side.)
The height of the tower is between 1 and H, inclusive.




You are given the ints C and K and the long long H.
Return the number of nice towers, modulo 1,234,567,891.



DEFINITION
Class:TheBrickTowerHardDivOne
Method:find
Parameters:int, int, long long
Returns:int
Method signature:int find(int C, int K, long long H)


CONSTRAINTS
-C will be between 1 and 4747, inclusive.
-K will be between 0 and 7, inclusive.
-H will be between 1 and 474,747,474,747,474,747, inclusive.


EXAMPLES

0)
2
0
2

Returns: 4

No two neighboring bricks may share the same color.
As we only have two colors, the entire tower must be colored like a chessboard.
There are two such towers of height 1, and two of height 2.


1)
1
7
19

Returns: 1

Only one tower of height 1 is acceptable here.


2)
2
3
1

Returns: 14

There are 16 possible towers of height 1.
If all bricks share the same color, the tower is not nice.
There are two such towers.
Each of the remaining 14 towers is nice.


3)
4
7
47

Returns: 1008981254

// END CUT HERE
#line 86 "TheBrickTowerHardDivOne.cpp"
*/
#include <vector>
#include <queue>
#include <deque>
#include <map>
#include <iostream>
#include <cstring>
#include <string>
#include <math.h>
#include <cstdio>
#include <cstdlib>
#include <algorithm>

using namespace std;

#define s(x) scanf("%d",&x)
#define sll(x) scanf("%lld",&x)
#define sf(x) scanf("%lf",&x)
#define ss(x) scanf("%s",&x)

#define f(i,a,b) for(int i=a;i<b;i++)
#define fr(i,n)  f(i,0,n)

typedef long long ll;



class TheBrickTowerHardDivOne {
	public:
	int find(int C, int K, long long H) {
		
	}
};
