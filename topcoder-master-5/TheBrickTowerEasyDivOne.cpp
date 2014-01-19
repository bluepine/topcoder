/*
// BEGIN CUT HERE
// PROBLEM STATEMENT
// 
John and Brus are building towers using toy bricks.
They have two types of bricks: red and blue ones.
The number of red bricks they have is redCount and each of them has a height of redHeight.
The number of blue bricks they have is blueCount and each of them has a height of blueHeight.



A tower is built by placing bricks one atop another.
A brick can be placed either on the ground, or on a brick of a different color.
(I.e., you are not allowed to put two bricks of the same color immediately on one another.)
A tower has to consist of at least one brick.
The height of a tower is the sum of all heights of bricks that form the tower.
Two towers are considered to be different if they have different heights.
(Two towers of the same height are considered the same, even if they differ in the number and colors of bricks that form them.)



You are given the ints redCount, redHeight, blueCount and blueHeight.
Return the number of different towers that John and Brus can build.



DEFINITION
Class:TheBrickTowerEasyDivOne
Method:find
Parameters:int, int, int, int
Returns:int
Method signature:int find(int redCount, int redHeight, int blueCount, int blueHeight)


CONSTRAINTS
-redCount will be between 1 and 474,747,474, inclusive.
-redHeight will be between 1 and 474,747,474, inclusive.
-blueCount will be between 1 and 474,747,474, inclusive.
-blueHeight will be between 1 and 474,747,474, inclusive.


EXAMPLES

0)
1
2
3
4

Returns: 4

John and Brus have 1 red brick of height 2 and 3 blue bricks of height 4. Using these bricks, it's possible to build 4 towers:
red (height 2);
blue (height 4);
red, blue (height 6);
blue, red, blue (height 10).

1)
4
4
4
7

Returns: 12

2)
7
7
4
4

Returns: 13

3)
47
47
47
47

Returns: 94

// END CUT HERE
#line 84 "TheBrickTowerEasyDivOne.cpp"
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



class TheBrickTowerEasyDivOne {
public:
  int find(int redCount, int redHeight, int blueCount, int blueHeight) {
    if (redHeight == blueHeight){
      if (redCount == blueCount){
	return 2 * min(redCount, blueCount);
      } else {
	return 2 * min(redCount, blueCount) + 1;
      }
    } else {
      if (redCount != blueCount){
	return 3 * min(redCount, blueCount) + 1;
      } else {
	return 3 * min(redCount, blueCount);
      }
    }
  }
};
