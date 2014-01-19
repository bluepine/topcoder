/*
// BEGIN CUT HERE
// PROBLEM STATEMENT
// A Ball Triangle is a set of identical balls placed in a triangular shape. A Ball Triangle has N rows, numbered 1 to N from top to bottom. For all i, 1 <= i <= N, the i-th row contains i balls. For example, the following image shows a Ball Triangle with N=3.




Fox Jiro has infinitely many Ball Triangles. He can paint a Ball Triangle according to the following conditions:

Each of the balls has to be painted either red, green, or blue.
No two adjacent balls may share the same color.

The following image shows one valid coloring of a Ball Triangle for N=3.




Jiro wants to paint as many Ball Triangles as he can.
As long as he follows the rules above, he may color the Ball Triangles in any way he likes.
Some of the colored Ball Triangles may look exactly the same, but they don't have to.
The only other constraint is the total amount of paint available to Jiro:
In all the triangles together, he can paint at most R balls red, G balls green, and B balls blue.

You are given the long longs R, G, and B.
You are also given the int N.
Return the maximum possible number of Ball Triangles Jiro can paint.

DEFINITION
Class:FoxPaintingBalls
Method:theMax
Parameters:long long, long long, long long, int
Returns:long long
Method signature:long long theMax(long long R, long long G, long long B, int N)


CONSTRAINTS
-R, G and B will each be between 0 and 1,000,000,000,000,000,000 (10^18), inclusive.
-N will be between 1 and 1,000,000,000, inclusive.


EXAMPLES

0)
2
2
2
3

Returns: 1

Jiro can paint one Ball Triangle in the same way as in the image in the statement.

1)
1
2
3
3

Returns: 0

This time Jiro can paint no Ball Triangles.

2)
8
6
6
4

Returns: 2



3)
7
6
7
4

Returns: 2



4)
100
100
100
4

Returns: 30



5)
19330428391852493
48815737582834113
11451481019198930
3456

Returns: 5750952686



6)
1
1
1
1

Returns: 3



// END CUT HERE
#line 116 "FoxPaintingBalls.cpp"
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



class FoxPaintingBalls {
public:
  long long theMax(long long r, long long g, long long b, int n) {
    ll cnt = ((ll)n * ((ll)n + 1)) / 6;
    if ((r == 0 || g == 0 || b == 0) && n > 1)
      return 0;
    if (n == 1)
      return r + g + b;
    if (n % 3 == 1){
      ll xmin = min(r,g);
      ll xmax = max(r,g);
      xmin = min(xmin, b);
      xmax = max(xmax, b);
      if ( r + g + b - xmin >= (xmin/cnt) * (2 * cnt + 1)){
	return xmin / cnt;
      }
      return ( r + g + b)/ ( 3 * cnt + 1);
    } else {
      ll xmin = min(r,g);
      xmin = min(xmin, b);
      return xmin / cnt;
    }
  }
};
