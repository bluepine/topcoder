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

ll mod = 1000000009;

class FoxAndMountain {
public:
  int count(int n, string history) {
    ll af[n + 3][n + 3];
    ll ab[n + 3][n + 3];
    fr (i, n + 3){
      fr (j, n + 3){
	af[i][j] = 0;
	ab[i][j] = 0;
      }
    }

    af[0][0] = 1;
    ab[n][0] = 1;
    
    f (i, 1, n + 1){
      fr (j, n + 1){
	if (j){
	  af[i][j] += af[i - 1][j - 1];
	}
	af[i][j] += af[i - 1][j + 1];
	af[i][j] %= mod;      
      }
    }

    int k = history.size();
    int hst = 0;
    int mini = 0;
    int hfl = 0;
    fr (i, k){
      if (history[i] == 'U'){
	hfl += 1;
      } else {
	hfl -= 1;
      }
      mini = min(mini, hfl);
    }
    ll res = 0;
    if (mini >= 0){
      res += af[n - k][hfl];
      res %= mod;
    }
    if (hfl <= 0 && k != n){
      int x = -hfl;
      if ((x + mini) >= 0){
	res += af[n - k][x];
	res %= mod;
      }      
    }
    ll dp[n + 1];
    fr (i, n){
      dp[i] = 0;      
    }
    f (i, 1, n - k){
      fr (j, n){
	if ((j + mini) >= 0 && (j + hfl) <= n ){
	  res += (af[i][j] * af[n- i - k][j + hfl]);	  
	  res %= mod;
	}
      }
      f (j, i + k, n){
	dp[j] += af[i][j];
	
      }
    }
    return res;
  }
};
