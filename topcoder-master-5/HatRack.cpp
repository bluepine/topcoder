// BEGIN CUT HERE
// END CUT HERE
#line 4 "HatRack.cpp"
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

vector<vector<int> > v;

vector<int> par;
vector<int> bt_queue;
vector<int> depth[2];
vector<int> mode[2];

bool vis[50];

class HatRack {
public:

  void dfs(int p, int x){
    par[x] = p;

    fr (i, v[x].size()){
      int child = v[x][i];
      if (!vis[child]){
	dfs(x, child);
	par[child] = x;
      }
    }
    bt_queue.pb(x);    
  }


  long long countWays(vector <int> knob1, vector <int> knob2) {
    int n = knob1.size() + 1;
    fr (i, n - 1){
      knob1[i]--; knob2[i]--;
    }

    v.resize(n);
    par.resize(n);

    fr (i, n - 1){
      v[knob1[i]].pb(knob2[i]);
      v[knob2[i]].pb(knob1[i]);
    }

    
    
    
    
  }
};
