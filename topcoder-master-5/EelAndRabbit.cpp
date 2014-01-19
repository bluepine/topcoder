// BEGIN CUT HERE

// END CUT HERE
// #line 5 "EelAndRabbit.cpp"
// written by lonerdude(dvdreddy)
// all rights reserved
//the template by dvdreddy
#include <vector>
#include <queue>
#include <set>
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

struct tmx{
  int time;
  int eelno;
  int type;
};

bool cmp(tmx x, tmx y){
  if (x.time < y.time){
    return true;
  } else if (x.time > y.time){
    return false;
  } else {
    return (x.type < y.type);
  }

}

class EelAndRabbit {
public:
  int getmax(vector <int> l, vector <int> t) {
    
    tmx events[110]; int n = 0;
    fr (i, l.size()){
      events[n].time = t[i]; 
      events[n].eelno = i;
      events[n].type = 0; n++;

      events[n].time = t[i] + l[i]; 
      events[n].eelno = i;
      events[n].type = 1; n++;
      
    }

    sort(events, events + n, cmp);

    int maxi = 0;

    vector<set<int> > v; int cnt = 0;
    vector<int> vt;
    set<int> cur;

    for (int i = 0; i < n; ){
      int curx = i, cury = i; 
      while (events[curx].time == events[cury].time && cury < n){
	cur.insert(events[cury].eelno); cury++;
      }
      set<int> cur2(cur);
      v.push_back(cur2); cnt++; vt.push_back(events[i].time);
      while (events[curx].time == events[i].time && i < n){
	if (events[i].type){
	  cur.erase(events[i].eelno);
	}
	i++;
      }
    }

    cout << "there\n";
    fr (i, cnt){
      set<int>::iterator it = v[i].begin();
      cout << "t " << vt[i] << " | ";
      while (it != v[i].end()){
	cout << " " << *it; it++;
      }
      cout << endl;
    }

    fr (i, cnt){      
      f (j, i + 1, cnt){
	set<int> st(v[i]);
	set<int>::iterator it = v[j].begin();
	while (it != v[j].end()){
	  st.insert(*it); it++;
	}
	maxi = max(maxi, (int) st.size());
      }
    }

    return maxi;
  }
};
