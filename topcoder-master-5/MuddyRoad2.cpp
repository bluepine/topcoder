
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

ll mod = 555555555;


class MuddyRoad2 {
public:
  int theCount(int N, int muddyCount) {
    ll a[N][muddyCount + 1][4];
    fr (i, N){
      fr (j, muddyCount + 1){
	fr (k, 4){
	  a[i][j][k] = 0;
	}
      }
    }

    a[0][0][1] = 1;    
    f (i, 1, N - 1){
      f (k, 0, muddyCount + 1){
	if (k){
	  a[i][k][3] += a[i - 1][k - 1][0];
	  a[i][k][3] += a[i - 1][k - 1][3];
	  a[i][k][3] %= mod;
	  f (p, 1, 3){
	    a[i][k][0] += a[i - 1][k - 1][p];
	    a[i][k][0] %= mod;
	  }
	} 
	fr (p, 3){
	  a[i][k][(p + 1) % 3] += a[i - 1][k][p];
	  a[i][k][(p + 1) % 3] %= mod;
	}
	a[i][k][3] += a[i - 1][k][3];
	a[i][k][3] %= mod;
	cout << "i k" << i << " " << k << endl;
	cout << a[i][k][0] << " " << a[i][k][1] << " " << a[i][k][2] << " " << a[i][k][3] << endl;
      }
      
    }
    a[N - 1][muddyCount][3] += a[N - 2][muddyCount][2];
    a[N - 1][muddyCount][3] += a[N - 2][muddyCount][3];
    a[N - 1][muddyCount][3] %= mod;
    return a[N - 1][muddyCount][3];
  }
};
