/*
// BEGIN CUT HERE
// PROBLEM STATEMENT
// Fox Jiro has a rectangular board, divided into a grid of square cells.
Each cell in the grid contains either the character '0', or the character '1'.
The vector <string> board contains the current state of the board.
The j-th character of the i-th element of board is the character in row i, column j of the grid.

Jiro now has to make exactly two flips. 
In the first flip, he must pick a row and flip all characters in that row.
(When flipped, a '0' turns to a '1' and vice versa.)
In the second flip, he must pick a column and flip all characters in that column.

You are given the vector <string> board.
Return the maximum number of '1's in the grid after Jiro makes the two flips.

DEFINITION
Class:XorBoardDivTwo
Method:theMax
Parameters:vector <string>
Returns:int
Method signature:int theMax(vector <string> board)


CONSTRAINTS
-board will contain between 1 and 50 elements, inclusive.
-Each element of board will contain the same number of characters.
-Each element of board will contain between 1 and 50 characters, inclusive.
-Each character in board will be '0' or '1'.


EXAMPLES

0)
{"101",
 "010",
 "101"}

Returns: 9

Jiro can obtain 9 '1's by flipping the center row and then the center column.

1)
{"111",
 "111",
 "111"}

Returns: 5

Jiro has to make both flips, even if that decreases the number of '1's.

2)
{"0101001",
 "1101011"}

Returns: 9



3)
{"000",
 "001",
 "010",
 "011",
 "100",
 "101",
 "110",
 "111"}

Returns: 15



4)
{"000000000000000000000000",
 "011111100111111001111110",
 "010000000100000001000000",
 "010000000100000001000000",
 "010000000100000001000000",
 "011111100111111001111110",
 "000000100000001000000010",
 "000000100000001000000010",
 "000000100000001000000010",
 "011111100111111001111110",
 "000000000000000000000000"}


Returns: 105



// END CUT HERE
#line 94 "XorBoardDivTwo.cpp"
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



class XorBoardDivTwo {
	public:
	int theMax(vector <string> board) {
		
	}
};
