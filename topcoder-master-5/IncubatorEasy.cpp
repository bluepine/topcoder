/*
// BEGIN CUT HERE
// PROBLEM STATEMENT
// You are the Incubator.
You have the ability to turn normal girls into magical girls.



There are n girls in the city.
The girls are conveniently numbered 0 through n-1.
Some girls love some other girls.
Love is not necessarily symmetric.
It is also possible for a girl to love herself.



You are given a vector <string> love.
Character j of element i of love is 'Y' if girl i loves girl j.
Otherwise, that character is 'N'.
In the rest of the problem statement, we will use love[i][j] to denote the truth value of the statement "girl i loves girl j".



Each girl has two boolean properties: isMagical (is she a magical girl?) and isProtected (is she protected by some girl?).
Initially, for each girl i we have isMagical[i] = False and isProtected[i] = False.



At any moment, you can choose an ordinary girl and turn her into a magical girl.
That is, you can take a girl i such that isMagical[i] = False, and change isMagical[i] to True.



Each such change will trigger a series of events:

Each magical girl will protect all girls she loves: if isMagical[i] and love[i][j], then isProtected[j] is set to True.
Each protected girl will also protect all girls she loves: if isProtected[i] and love[i][j], then isProtected[j] is set to True.

Note that some of these changes may in turn trigger other changes, as more and more girls become protected.



Once there are no more changes, you can again change another ordinary girl into a magical one, and so on.
Your goal is to reach a situation with many girls that are magical, but not protected.
That is, you are interested in girls such that isMagical[i] = True and isProtected[i] = False.
Return the maximum number of such girls in a situation that can be reached using the above process.

DEFINITION
Class:IncubatorEasy
Method:maxMagicalGirls
Parameters:vector <string>
Returns:int
Method signature:int maxMagicalGirls(vector <string> love)


CONSTRAINTS
-n will be between 1 and 10, inclusive.
-love will contain exactly n elements.
-Each element of love will contain exactly n characters.
-Each character in each element of love will be either 'Y' or 'N'.


EXAMPLES

0)
{"NY","NN"}

Returns: 1

One optimal solution is to change girl 0 to a magical girl.
Girl 0 will be magical and she will not be protected.
It is not possible to have two girls that are both magical and not protected:
if you change both girls to magical girls (in any order), you will get a situation in which girl 1 is protected.


1)
{"NYN", "NNY", "NNN"}

Returns: 1

Again, there is no way to create more than one unprotected magical girl.
For example, if we start by making girl 2 magical, and then make girl 0 magical, magical girl 0 will protect girl 1, and protected girl 1 will protect girl 2.
Thus, in this case girl 0 will be magical and unprotected, girl 1 will be ordinary and protected, and girl 2 will be magical and protected.

2)
{"NNYNN","NNYNN","NNNYY","NNNNN","NNNNN"}

Returns: 2



3)
{"NNNNN","NYNNN","NYNYN","YNYNN","NNNNN"}

Returns: 2



4)
{"NNNNN","NNNNN","NNNNN","NNNNN","NNNNN"}

Returns: 5



5)
{"Y"}

Returns: 0

Note that a girl may love herself.

// END CUT HERE
#line 115 "IncubatorEasy.cpp"
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
typedef pair<int, int> p;


class IncubatorEasy {
public:
  int maxMagicalGirls(vector <string> love) {
    int n = love.size();
    int allp = 1 << n;
    bool is_true[n];
    bool is_p[n];
    queue<int> q;
    int dp[n][n];
    fr (i, n){
      fr (j, n){
	if (love[i][j] == 'Y'){
	  dp[i][j] = 1;
	} else {
	  dp[i][j] = 0;
	}
      }
    }

    fr (k, n){
      fr (i, n){
	fr (j, n){
	  if (dp[i][k] && dp[k][j]){
	    dp[i][j] = 1;
	  }
	}
      }
    }
    int res = 0;
    fr (i, allp){
      int x = i;
      fr (j, n){
	is_true[j] = false;
	is_p[j] = false;
      }
      fr (j, n){
	if (x & 1){
	  is_true[j] = true;
	  fr (k, n){
	    if (dp[j][k] == 1){
	      is_p[k] = true;
	    }
	  }
	}
	x = x >> 1;
      }
      int res1 = 0;
      fr (j, n){
	if (is_p[j] == false && is_true[j] == true){
	  res1++;
	}
      }
      res = max(res1, res);
    }
    return res;
  }
};
