// BEGIN CUT HERE

// END CUT HERE
// #line 5 "UndoHistory.cpp"
// written by lonerdude(dvdreddy)
// all rights reserved
//the template by dvdreddy
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

#define si(x) scanf("%d",&x)
#define sll(x) scanf("%lld",&x)
#define sf(x) scanf("%lf",&x)
#define ss(x) scanf("%s",&x)

#define f(i,a,b) for(int i=a;i<b;i++)
#define fr(i,n)  f(i,0,n)

typedef long long ll;

int INF = 88888;

class UndoHistory {
public:
  int minPresses(vector <string> lines) {
    set<string> dict;
    dict.insert("");
    string cur_buff = "";
    set<string>::iterator it;
    int res = 0;
    fr (px, lines.size()){
      string cur = lines[px];
      int n = cur.size();
      int match = 0;
      fr (i, min((int) cur_buff.size(), n)){
	if (cur_buff[i] == cur[i]){
	  match++;
	}	
      }
      string emp = "";      
      int maxi = 0;
      fr (i, n){
	emp += cur[i];
	it = dict.find(emp);
	if (it != dict.end()){
	  maxi = i + 1;
	}
      }
      if (match == cur_buff.size() && match + 2 >= maxi){
	maxi = match; res -= 2;
      }
      res += (2 + n - (maxi));
      emp = "";
      fr (i, maxi){
	emp += cur[i];
      }
      f (i, maxi, n){
	emp += cur[i];
	dict.insert(emp);
      }
      cur_buff = cur;
      res += 1;
    }

    return res;
  }
};
