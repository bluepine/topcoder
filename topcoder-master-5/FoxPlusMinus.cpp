/*
// BEGIN CUT HERE
// PROBLEM STATEMENT
// Fox Jiro is interested in sequences of intetegers. Today he considers the sequence given to you as the vector <int> first. Let K be the number of elements of first. Jiro chooses some permutation of first. Let's call this permutation F. An infinite sequence of integers A is defined as follows:

for all i, 0 <= i < K, A[i] = F[i].
Otherwise, A[i] = A[i-K] - A[i-K+1] + ... + (-1)^(K-1) * A[i-1].

In addition to first, Jiro has a int N.
His goal is to maximize the value of A[N].
Return a vector <int> containing the best choice of F.
If there are multiple permutations of first maximizing the value of A[N], return the lexicographically smallest one.

DEFINITION
Class:FoxPlusMinus
Method:maximize
Parameters:vector <int>, int
Returns:vector <int>
Method signature:vector <int> maximize(vector <int> first, int N)


NOTES
-A vector <int> A is lexicographically smaller than a vector <int> B if A contains a smaller number at the first index where they differ.


CONSTRAINTS
-first will contain between 1 and 50 elements, inclusive.
-Each element of first will be between -1,000,000,000 and 1,000,000,000, inclusive.
-N will be between 0 and 1,000,000,000, inclusive.


EXAMPLES

0)
{1, 2}
2

Returns: {2, 1 }

A[2] will be 1 if F = {2, 1}, and it will be -1 if F = {1, 2}.

1)
{1, 2, 3}
3

Returns: {2, 1, 3 }

{3, 1, 2} also maximizes A[3] but {2, 1, 3} is lexicographically smaller.

2)
{-3, 1, -4, 1, -5, 9, -2}
10

Returns: {-5, -4, 9, -3, -2, 1, 1 }



3)
{2, 7, -1, 8, -2, -8}
10

Returns: {2, -1, 7, -2, 8, -8 }



4)
{-10, -20, -30}
1

Returns: {-30, -10, -20 }



5)
{1, 2, 4, 9, 7, 3}
13

Returns: {3, 4, 2, 7, 1, 9 }



6)
{-4112039, 51143992, 941422315, -153492958, 499218832, 543599293, 132059490, -434243951,
 -95819234, 1552938, -857192847, 481950390, 401099286, 71482395, -711450593, 8101919}
884142312

Returns: {51143992, 8101919, 71482395, 1552938, 132059490, -4112039, 401099286, -95819234, 481950390, -153492958, 499218832, -434243951, 543599293, -711450593, 941422315, -857192847 }



// END CUT HERE
#line 93 "FoxPlusMinus.cpp"
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



class FoxPlusMinus {
public:
  vector <int> maximize(vector <int> first, int N) {
    int a[50][50];
    int 
  }
};
