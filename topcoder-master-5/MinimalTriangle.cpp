/*
// BEGIN CUT HERE
// PROBLEM STATEMENT
// You are given a int length.
We have a regular hexagon: a polygon with six sides, in which all internal angles have 120 degrees and length is the length of each side.
We are going to draw three non-intersecting diagonals in some way.
These will divide the hexagon into four triangles.
We will then compute their areas, take a piece of paper and write down the smallest of those four areas.
Compute and return the largest number we can obtain on our piece of paper (by choosing which diagonals to draw).

DEFINITION
Class:MinimalTriangle
Method:maximalArea
Parameters:int
Returns:double
Method signature:double maximalArea(int length)


NOTES
-Your return value must have a relative or an absolute error of less than 1e-9.


CONSTRAINTS
-length will be between 1 and 1,000,000 (10^6), inclusive.


EXAMPLES

0)
5

Returns: 10.825317547305485



1)
10

Returns: 43.30127018922194



2)
100000

Returns: 4.330127018922194E9



// END CUT HERE
#line 52 "MinimalTriangle.cpp"
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



class MinimalTriangle {
	public:
	double maximalArea(int length) {
		
	}
};
