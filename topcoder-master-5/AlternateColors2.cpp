// BEGIN CUT HERE
// END CUT HERE
#line 4 "AlternateColors2.cpp"
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

class AlternateColors2 {
public:
  
  long long countWays(int n, int k) {
    int st = 0;
    ll res = 0;
    ll nx = (ll) n;
    while (st < n){
      if (st < k){
	int rem = k - st;
	if (rem % 2 == 1){
	  int vi = (rem + 1) / 2;
	  ll tot = (ll) (n - vi + 1 - st);
	  res += tot; res += tot; 
	  if (tot != 0){
	    res--;
	  }
	} else {
	  ll vi = (ll)((rem - 1) / 2);
	  ll tot = vi + 1;
	  res += tot; res += tot; res--;
	}
      } else if (k % 3 == 1){
	ll tot = (ll) (n - st + 1);
	res += tot; res += tot; res += tot;
	res -= 3;
      } else {
	break;
      }
      st += 3;
    }
    
    if (st == n && k % 3 == 1){
      res++;
    }
    
    fr (i, n){
      int rd = n - 2 * i, gr = i, bl = i;
      
    }


    return res;
  }
};


/*
int main(){
  AlternateColors2 cx;
  int nx[] = {1, 3, 6, 6, 1000, 100000, 4077, 86101, 88732};
  int kx[] = {1, 3, 4, 1, 2, 100000, 1875, 38775, 18249};
  fr (i, 9){
    int n = nx[i], k = kx[i];
    cout << cx.countWays(n, k) << endl;
  }

  return 0;
  }*/
