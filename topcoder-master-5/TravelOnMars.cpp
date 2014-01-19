// BEGIN CUT HERE
// END CUT HERE
#line 4 "TravelOnMars.cpp"
// written by lonerdude(dvdreddy)
// all rights reserved
//the template by dvdreddy
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
#include <set>
#include <algorithm>

using namespace std;

#define si(x) scanf("%d",&x)
#define sll(x) scanf("%lld",&x)
#define sf(x) scanf("%lf",&x)
#define ss(x) scanf("%s",&x)

#define f(i,a,b) for(int i=a;i<b;i++)
#define fr(i,n)  f(i,0,n)
#define pb push_back
#define rf(i,a,b) for(int i=a; i>b;i--)
#define rfr(i,n) for(int i=n;i>=0;i--)

typedef long long ll;

int MAX = 2500;

class TravelOnMars {
public:
  int minTimes(vector <int> range, int startCity, int endCity) {
    int n = range.size();
    int dp[n][n];
    fr (i, n){
      fr (j, n){
	dp[i][j] = MAX;
      }
    }

    fr (i, n){
      fr (j, range[i]){
	dp[i][(i + j + 1) % n] = 1;
      }
      fr (j, range[i]){
	dp[i][(i - j - 1 + n) % n] = 1;
      }      
    }

    fr (k, n){
      fr (i, n){
	fr (j, n){
	  if (dp[i][k] + dp[k][j] < dp[i][j]){
	    dp[i][j] = dp[i][k] + dp[k][j];
	  }
	}
      }
    }

    return dp[startCity][endCity];

  }
};
