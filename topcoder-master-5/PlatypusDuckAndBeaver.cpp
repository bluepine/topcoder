/*
// BEGIN CUT HERE
// PROBLEM STATEMENT
// Your friend the farmer liked to use the following method to count the number of beavers and ducks present on the farm: He would first find three ints webbedFeet, duckBills and beaverTails, representing  the number of webbed feet, duck bills and beaver tails he could count and then he used those numbers to find the number of ducks and beavers on the farm. (A duck has one duck bill and two webbed feet. A beaver has one beaver tail and four webbed feet.)

The method worked great until he received news that there could be a platypus invasion on the farm. A platypus is a marvellous creature that has 4 webbed feet, a duck bill and a beaver tail. Our farmer is justifiably confused. Help him find out that his method is still useful. You are given ints webbedFeet, duckBills and beaverTails that specify the number of webbed feet, duck bills and beaver tails  the farmer has counted, respectively. Return the total number of animals (ducks, beavers, and platypuses) on the farm. The constraints guarantee that there will be exactly one solution.

DEFINITION
Class:PlatypusDuckAndBeaver
Method:minimumAnimals
Parameters:int, int, int
Returns:int
Method signature:int minimumAnimals(int webbedFeet, int duckBills, int beaverTails)


CONSTRAINTS
-webbedFeet, duckBills and beaverTails will each be between 0 and 1000, inclusive.
-There will be exactly one way to assign the number of ducks, beavers and platypuses such that the total number of each specific kind of body part matches webbedFeet, duckBills and beaverTails, respectively.


EXAMPLES

0)
4
1
1

Returns: 1

If there is only one platypus, it will have 4 webbed feet, 1 beaver tail and 1 duck bill. Matching the description.

1)
0
0
0

Returns: 0



2)
10
2
2

Returns: 3

One platypus, one duck and one beaver.

3)
60
10
10

Returns: 20

10 ducks and 10 beavers.

4)
1000
200
200

Returns: 300



// END CUT HERE
#line 70 "PlatypusDuckAndBeaver.cpp"
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



class PlatypusDuckAndBeaver {
public:
  int minimumAnimals(int webbedFeet, int duckBills, int beaverTails) {
    return beaverTails + (webbedFeet - 4 * beaverTails) / 2;
  }
};
