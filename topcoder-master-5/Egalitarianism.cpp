// BEGIN CUT HERE
// END CUT HERE
#line 4 "Egalitarianism.cpp"
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

int MAX = 25000;

class Egalitarianism {
public:
  int maxDifference(vector <string> isFriend, int d) {
    int n = isFriend.size();
    int dp[n][n];

    fr (i, n){
      fr (j, n){
	dp[i][j] = MAX;
      }
      dp[i][i] = 0;
    }

    fr (i, n){
      fr (j, n){
	if (isFriend[i][j] == 'Y'){
	  dp[i][j] = 1;
	}	
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

    int MAXI = 0;

    fr (i, n){
      fr (j, n){
	MAXI = max(dp[i][j], MAXI);
      }
    }

    if (MAXI == MAX){
      return -1;
    } else {
      return MAXI * d;
    }
  }
};
