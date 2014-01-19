/*
// BEGIN CUT HERE
// PROBLEM STATEMENT
// Fox Jiro came to a flower shop to buy flowers.
The flowers in the shop are arranged in some cells of a rectangular grid.
The layout of the grid is given as a vector <string> flowers.
If the j-th cell of the i-th row of the grid contains a flower, then the j-th character of the i-th element of flowers will be 'F'.
(All indices in the previous sentence are 0-based.)
If the particular cell is empty, the corresponding character will be '.' (a period).

In order to buy flowers, Jiro has to draw a rectangle on this grid and buy all the flowers which lie inside the rectangle.
Of course, the sides of the rectangle must be on cell boundaries.
(Therefore, the sides of the rectangle will necessarily be parallel to the coordinate axes.)

Jiro wants to buy as many flowers as possible.
Unfortunately, he cannot select the entire grid.
Eel Saburo came to this shop before Jiro.
Saburo has already drawn his rectangle.
Saburo's rectangle contains just a single cell: the c-th cell of the r-th row of the grid.
(Again, both indices are 0-based.)
Jiro's rectangle may not contain this cell.

You are given the vector <string> flowers and the ints r and c.
Return the maximum possible number of flowers Jiro can buy in this situation.

DEFINITION
Class:FoxAndFlowerShopDivTwo
Method:theMaxFlowers
Parameters:vector <string>, int, int
Returns:int
Method signature:int theMaxFlowers(vector <string> flowers, int r, int c)


CONSTRAINTS
-flowers will contain R elements.
-R will be between 2 and 10, inclusive.
-Each element of flowers will contain C characters.
-C will be between 1 and 10, inclusive.
-Each character in flowers will be either 'F' or '.'.
-r will be between 0 and R - 1, inclusive.
-c will be between 0 and C - 1, inclusive.


EXAMPLES

0)
{"F.F",
 ".F.",
 ".F."}
1
1

Returns: 2

The forbidden cell is the one in the middle. Jiro can buy two flowers by drawing a rectangle that contains the entire first row.

1)
{"F..",
 "...",
 "..."}
0
0

Returns: 0

There are no flowers Jiro can buy.

2)
{".FF.F.F",
 "FF...F.",
 "..FF.FF"}
1
2

Returns: 6



3)
{"F",
 ".",
 "F",
 "F",
 "F",
 ".",
 "F",
 "F"}
4
0

Returns: 3



4)
{".FFF..F...",
 "FFF...FF.F",
 "..F.F.F.FF",
 "FF..F.FFF.",
 "..FFFFF...",
 "F....FF...",
 ".FF.FF..FF",
 "..F.F.FFF.",
 ".FF..F.F.F",
 "F.FFF.FF.F"}
4
3

Returns: 32



// END CUT HERE
#line 115 "FoxAndFlowerShopDivTwo.cpp"
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



class FoxAndFlowerShopDivTwo {
public:
  int theMaxFlowers(vector <string> flowers, int r, int c) {
    int res = 0;
    int temp = 0;
    int rx = flowers.size();
    int cx = flowers[0].size();
    fr (i, rx){
      fr (j, cx){
	f (k, i, rx){
	  f (p, j, cx){
	    if ((i <= r) && (j <= c) && (r <= k) && (c <= p)){
	      continue;
	    } else {
	      temp = 0;
	      f (x, i, k + 1){
		f (y, j, p + 1){
		  if (flowers[x][y] == 'F'){
		    temp++;
		  }
		}
	      }
	    }
	    res = max(res, temp);
	  }
	}
      }
    }
    
    return res;
  }
};
