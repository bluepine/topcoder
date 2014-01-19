/*
// BEGIN CUT HERE
// PROBLEM STATEMENT
// Ralph was once playing with a set of cards and a grid of cells.
Each card was a rectangle of a unique color.
Different cards may have had different dimensions.
Ralph took his cards one at a time, and placed each of them onto the grid.
When placing a card, Ralph aligned its sides with grid lines, so each card covered some rectangular block of cells.
Some cards may have overlapped other, previously placed cards partially or even completely.
Once all the cards were placed, on each cell only the topmost card was visible.

You are given a vector <string> grid that describes what Ralph could see after placing the cards. The j-th character of element i of grid is '.' if the cell at position (i,j) is empty (does not contain any card) or is an alphanumeric character that represents the only color Ralph could see when looking at the cell.

Ralph does not remember the order he used to place the cards. Write a program that finds the order in which the cards of each visible color were placed.  The return value should be a string, containing exactly once each of the alphanumeric characters that occur in grid.
The i-th character of the return value should be the color of the i-th card (0-based index) that Ralph placed.
In case there are multiple valid orders, return the lexicographically smallest one.
In case there is no valid order of colors, return "ERROR!" (quotes for clarity).

DEFINITION
Class:TopView
Method:findOrder
Parameters:vector <string>
Returns:string
Method signature:string findOrder(vector <string> grid)


NOTES
-The letters in grid are case-sensitive: 'a' and 'A' are distinct colors.
-The lexicographically smaller of two strings of equal length is the one that has the earlier character (using ASCII ordering) at the first position at which they differ.


CONSTRAINTS
-grid will contain between 1 and 50 elements, inclusive.
-Each element of grid will contain between 1 and 50 characters, inclusive.
-All elements of grid will contain the same number of characters.
-Each character in each element of grid will be a period ('.'), an uppercase letter ('A'-'Z'), a lowercase letter ('a'-'z'), or a digit ('0'-'9').
-At least one character in grid will be different from '.'.


EXAMPLES

0)
{"..AA..",
 ".CAAC.",
 ".CAAC."}

Returns: "CA"

The card with color C is a rectangle of 2 rows and 4 columns. The card with color A, a rectangle of 3 rows and 2 columns, was placed on top of it.

1)
{"..47..",
 "..74.."}


Returns: "ERROR!"

Each card has a unique color, this top perspective view tells us about 2 cards. This setting is not possible without using multiple cards of color 4 or 7.

2)
{"bbb666",
 ".655X5",
 "a65AA5",
 "a65AA5",
 "a65555"}

Returns: "65AXab"



3)
{"aabbaaaaaaaaaaaaaaaaaa",
 "aabbccccccccccccccaaaa",
 "aab11111ccccccccccaaaa",
 "aab12221ccccccccccaaaa",
 "aab12221ccccccccccaaaa",
 "aab12221ccccccccccaaaa",
 "aab12221ccccccccccaaaa",
 "aab12221ccccccccccaaaa",
 "aab12221ddddddddddaaaa",
 "aab13331DDDDDDDDDDaaaa",
 "aab13331DDDDDDDDDDaaaa",
 "aa.11111DDDDDDDDDDaaaa",
 "aaaaaaaaaaaaaaaaaaaaaa"}

Returns: "ERROR!"



// END CUT HERE
#line 92 "TopView.cpp"
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



class TopView {
	public:
	string findOrder(vector <string> grid) {
		
	}
};
