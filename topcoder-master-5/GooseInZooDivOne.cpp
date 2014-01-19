// BEGIN CUT HERE

// END CUT HERE
// #line 5 "GooseInZooDivOne.cpp"
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
#include <algorithm>

using namespace std;

#define si(x) scanf("%d",&x)
#define sll(x) scanf("%lld",&x)
#define sf(x) scanf("%lf",&x)
#define ss(x) scanf("%s",&x)

#define f(i,a,b) for(int i=a;i<b;i++)
#define fr(i,n)  f(i,0,n)

typedef long long ll;
typedef pair<int, int> pi;
int wt[250];
int par[250];


ll mod = 1000000007;

int full_par (int x){
  while(x!=par[x]){
    int temp=par[x];
    par[x]= par[par[x]];
    x=temp;
  }
  return x;
}

class GooseInZooDivOne {
public:
  int count(vector <string> field, int dist) {
    int n = field.size();
    int m = field[0].size();
    
    int a[50][50];
    fr (i, n){
      fr (j, m){
	a[i][j] = 0;
      }
    }

    vector<ll> xx;

    fr (i, n){
      fr (j, m){
	if (!a[i][j] && field[i][j] != '.'){
	  queue<pi> q;
	  int cur = 1;
	  q.push(pi(i, j)); a[i][j] = 1; 
	  while (!q.empty()){
	    pi curx = q.front(); q.pop();
	    f (i1, max(curx.first - dist - 1, 0), min(curx.first + dist + 1, n)){
	      f (j1, max(curx.second - dist - 1, 0), min(curx.second + dist + 1, m)){
		int fx = abs(i1 - curx.first) + abs(j1 - curx.second);
		if (fx <= dist && field[i1][j1] == 'v' && !a[i1][j1]){
		  q.push(pi(i1, j1)); a[i1][j1] = 1; cur++;
		}
	      }
	    }	    
	  }
	  xx.push_back(cur);
	}
      }
    }
      
    cout << "ii";
    fr (i, xx.size()){      
      cout << " " << xx[i];
    }
    cout << endl;

    ll res[2][2];
    res[1][0] = 1; res[0][0] = 0;
    res[1][1] = 0; res[0][1] = 0;
    int bx = -1;
    fr (i, xx.size()){
      if (xx[i] % 2 == 0 && xx[i] != 0 && bx <= 0){
	bx += 2;
      }
      if (xx[i] % 2 == 1 && bx <= 0){
	  bx += 1;
      }
      res[i & 1][xx[i] & 1] += res[(i + 1) & 1][0];
      res[i & 1][(xx[i] + 1) & 1] += res[(i + 1) & 1][1];
      res[i & 1][0] += res[(i + 1) & 1][0];
      res[i & 1][1] += res[(i + 1) & 1][1];
      cout << "----------\n";
      fr (p, 2){
	fr (q, 2){
	  res[p][q] %= mod;
	  cout << res[p][q] << " ";
	}
	cout << endl;
      }
      fr (p, 2){
	res[(i + 1) & 1][p] = 0;
      }
    }
    
    cout << "bx " << bx << endl;
    if (bx > 0) {      
      return (res[(xx.size() + 1) & 1][0] - 1);
    } else {
      return 0;
    }
  }
};
