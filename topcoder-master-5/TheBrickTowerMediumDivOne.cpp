/*
#line 82 "TheBrickTowerMediumDivOne.cpp"
*/
#include <vector>
#include <queue>
#include <deque>
#include <map>
#include <set>
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



class TheBrickTowerMediumDivOne {
public:
  vector <int> find(vector <int> heights) {
    vector<vector<int> > v;
    v.resize(51);
    fr(i, heights.size()){
      v[heights[i]].push_back(i);
    }
    set<int> vn(heights.begin(), heights.end());
    if (vn.size() == 1){
      vector<int> res;
      fr(i, heights.size()){
	res.push_back(i);
      }
      return res;
    } else {
      set<int>::iterator it;
      it = vn.begin();
      int used[100];
      fr (i, 100){
	used[i] = false;
      }
      vector<int> res;
      
      int t = 1;
      res.push_back(0);
      v[heights[0]].erase(v[heights[0]].begin());
      int prev = 0;
      while (t < heights.size()){
	if (heights[t] <= heights[prev]){
	  res.push_back(t);
	  v[heights[t]].erase(v[heights[t]].begin());
	  prev = t;
	}
	t++;
      }

      it = vn.begin();
      while (it != vn.end()){
	fr (i, v[*it].size()){
	  res.push_back(v[*it][i]);
	}
	it++;
      }
      return res;
    }

  }
};


// Powered by FileEdit
