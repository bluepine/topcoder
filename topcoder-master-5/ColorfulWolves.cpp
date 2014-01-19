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

int a[50][50];
int imax = 3000;

class ColorfulWolves {
public:
  int getmin(vector <string> colormap) {
    int n = colormap.size();
    fr (i, n){
      fr (j, n){
	a[i][j] = imax;
      }
    }
    
    fr (i, n){
      int cnt = 0;
      fr (j, n){
	if (colormap[i][j] == 'Y'){
	  a[i][j] = cnt;
	  cnt++;
	}
      }
    }
    
    fr (k, n){
      fr (i, n){
	fr (j, n){
	  a[i][j] = min(a[i][k] + a[k][j], a[i][j]);
	}
      }
    }

    if (a[0][n - 1] == imax){
      return -1;
    }
    return a[0][n - 1];
  }
};
