/*
// BEGIN CUT HERE
// PROBLEM STATEMENT
// You are playing a solitaire game called Left-Right Digits Game. This game uses a deck of N cards. Each card has a single digit written on it. These digits are given as characters in the string digits. More precisely, the i-th character of digits is the digit written on the i-th card from the top of the deck (both indices are 0-based).

The game is played as follows. First, you place the topmost card (whose digit is the 0-th character of digits) on the table. Then, you pick the cards one-by-one from the top of the deck. For each card, you have to place it either to the left or to the right of all cards that are already on the table.

After all of the cards have been placed on the table, they now form an N-digit number. You are given a string lowerBound that represents an N-digit number. The primary goal of the game is to arrange the cards in such a way that the number X shown on the cards will be greater than or equal to lowerBound. If there are multiple ways to satisfy the primary goal, you want to make the number X as small as possible.

Return the smallest possible value of X you can achieve, as a string containing N digits. If it is impossible to achieve a number which is greater than or equal to lowerBound, return an empty string instead.

DEFINITION
Class:LeftRightDigitsGame2
Method:minNumber
Parameters:string, string
Returns:string
Method signature:string minNumber(string digits, string lowerBound)


NOTES
-lowerBound has no leading zeros. This means that any valid number X should also have no leading zeros (since otherwise it will be smaller than lowerBound).


CONSTRAINTS
-digits will contain between 1 and 50 characters, inclusive.
-Each character of digits will be between '0' and '9', inclusive.
-lowerBound will contain the same number of characters as digits.
-Each character of lowerBound will be between '0' and '9', inclusive.
-The first character of lowerBound will not be '0'.


EXAMPLES

0)
"565"
"556"

Returns: "556"

You can achieve exactly 556. The solution is as follows:

Place the first card on the table.
Place the second card to the right of all cards on the table.
Place the last card to the left of all cards on the table.


1)
"565"
"566"

Returns: "655"



2)
"565"
"656"

Returns: ""

The largest number you can achieve is 655, but it is still less than 656.

3)
"9876543210"
"5565565565"

Returns: "5678943210"



4)
"8016352"
"1000000"

Returns: "1086352"



// END CUT HERE
#line 81 "LeftRightDigitsGame2.cpp"
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



class LeftRightDigitsGame2 {
	public:
	string minNumber(string digits, string lowerBound) {
		
	}
};
