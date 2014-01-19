/*
// BEGIN CUT HERE
// PROBLEM STATEMENT
// King Dengklek received a container full of ducks as his birthday gift from his loyal subjects. Each duck is of a specific type, represented by an integer between 1 and 50, inclusive. He was told that the container contains the same number of ducks of each type that is present in the container.

King Dengklek wants to guess the total number of ducks in the container. He removed some of the ducks from the container and examined their types. These types are given in vector <int> duckTypes, where duckTypes[i] is the type of the i-th duck (0-based index) King Dengklek picked.

Compute and return the total number of ducks in the container in the beginning (before King Dengklek removed some of them). If there are multiple possibilities, return the smallest one.

DEFINITION
Class:KingdomAndDucks
Method:minDucks
Parameters:vector <int>
Returns:int
Method signature:int minDucks(vector <int> duckTypes)


CONSTRAINTS
-duckTypes will contain between 1 and 50 elements, inclusive.
-Each element of duckTypes will be between 1 and 50, inclusive.


EXAMPLES

0)
{5, 8}

Returns: 2

The container might have contained one duck of each of types 5 and 8.

1)
{4, 7, 4, 7, 4}

Returns: 6

The container might have contained three ducks of each of types 4 and 7.

2)
{17, 17, 19, 23, 23, 19, 19, 17, 17}

Returns: 12



3)
{50}

Returns: 1

The container might have contained only one duck of type 50.

4)
{10, 10, 10, 10, 10}

Returns: 5



// END CUT HERE
#line 62 "KingdomAndDucks.cpp"
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



class KingdomAndDucks {
public:
  int minDucks(vector <int> duckTypes) {
    int a[51];
    fr (i, 51){
      a[i] = 0;
    }
    fr (i, duckTypes.size()){
      a[duckTypes[i]]++;
    }
    int maxi = 0;
    int cnt = 0;
    fr (i, 51){
      if (a[i]){
	cnt++;
	maxi = max(maxi, a[i]); 
      }
    }
    return cnt * maxi;

  }
};
