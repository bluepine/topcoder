/*
// BEGIN CUT HERE
// PROBLEM STATEMENT
// You just bought a very delicious chocolate bar from a local store. This chocolate bar consists of N squares, numbered 0 through N-1. All the squares are arranged in a single row. There is a letter carved on each square. You are given a string letters. The i-th character of letters denotes the letter carved on the i-th square (both indices are 0-based).

You want to share this delicious chocolate bar with your best friend. At first, you want to give him the whole bar, but then you remembered that your friend only likes a chocolate bar without repeated letters. Therefore, you want to remove zero or more squares from the beginning of the bar, and then zero or more squares from the end of the bar, in such way that the remaining bar will contain no repeated letters.

Return the maximum possible length of the remaining chocolate bar that contains no repeated letters.

DEFINITION
Class:ChocolateBar
Method:maxLength
Parameters:string
Returns:int
Method signature:int maxLength(string letters)


CONSTRAINTS
-letters will contain between 1 and 50 characters, inclusive.
-Each character of letters will be a lowercase letter ('a' - 'z').


EXAMPLES

0)
"srm"

Returns: 3

You can give the whole chocolate bar as it contains no repeated letters.

1)
"dengklek"

Returns: 6

Remove two squares from the end of the bar.

2)
"haha"

Returns: 2

There are three possible ways:

Remove two squares from the beginning of the bar.
Remove two squares from the end of the bar.
Remove one square from the beginning of the bar and one square from the end of the bar.


3)
"www"

Returns: 1



4)
"thisisansrmbeforetopcoderopenfinals"

Returns: 9



// END CUT HERE
#line 67 "ChocolateBar.cpp"
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



class ChocolateBar {
public:
  int maxLength(string letters) {
    int a[26];
    fr (i, 26){
      a[i] = 0;
    }

    int n = letters.size();
    int res = 0;
    fr (i, n){
      fr (j, n){
	fr (p, 26){
	  a[p] = 0;
	}
	bool x = true;
	f (k, i, n - j){
	  a[letters[k] - 'a']++;
	  if (a[letters[k] - 'a'] > 1){
	    x = false;
	    break;
	  }
	}
	
	if (x){
	  res = max(res, n - j - i);
	}
      }      
    }
    return res;
  }
};
