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
ll mod = 1000000007;

ll a[3][3][54][54][54];

class ColorfulCupcakesDivTwo {
public:
  int countArrangements(string cupcakes) {
    int x[3];
    fr (i, 3){
      x[i] = 0;
    }
    fr (i, cupcakes.size()){
      x[cupcakes[i] - 'A']++;
    }
    cout << x[0] << x[1] << x[2] << endl;
    fr (i, x[0] + 4){
      fr (j, x[1] + 4){
	fr (k, x[2] + 4){
	  fr (p, 3){
	    fr(t, 3){
	      a[t][p][i][j][k] = 0;
	    }
	  }
	}
      }
    }
    a[0][0][1][0][0] = 1;
    a[1][1][0][1][0] = 1;
    a[2][2][0][0][1] = 1;
    
    fr (i, x[0] + 4){
      fr (j, x[1] + 4){
	fr (k, x[2] + 4){
	  if ((i + j + k) == 1){
	    continue;
	  }
	  fr (p, 3){
	    if (i){
	      a[p][0][i][j][k] = a[p][1][i - 1][j][k] + a[p][2][i - 1][j][k];
	      a[p][0][i][j][k] %= mod;
	    }
	    
	    if (j){
	      a[p][1][i][j][k] = a[p][0][i][j - 1][k] + a[p][2][i][j - 1][k];
	      a[p][1][i][j][k] %= mod;
	    }
	  
	    if (k){
	      a[p][2][i][j][k] = a[p][1][i][j][k - 1] + a[p][0][i][j][k - 1];
	      a[p][2][i][j][k] %= mod;
	    }
	  }
	}
      }
    }
    
    ll res = a[0][1][x[0]][x[1]][x[2]] + a[0][2][x[0]][x[1]][x[2]] +
      a[1][2][x[0]][x[1]][x[2]] + a[1][0][x[0]][x[1]][x[2]] +
      a[2][0][x[0]][x[1]][x[2]] + a[2][1][x[0]][x[1]][x[2]];
    res %= mod;
    return (int) res;
  }
};


// Powered by FileEdit
