// BEGIN CUT HERE

// END CUT HERE
// #line 5 "SurveillanceSystem.cpp"
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

class SurveillanceSystem {
	public:
	string getContainerInfo(string containers, vector <int> reports, int L) {
    map<int, vector<int> > mp;
    int n = containers.size();
    // int l = L;
    fr (i, n - L + 1){
      int cnt = 0;
      fr (j, L){
        if (containers[i + j] == 'X'){
          cnt++;
        }
      }
      mp[cnt].push_back(i);
    }


    map<int, int> rep_cnt;

    fr (i, reports.size()){
      rep_cnt[reports[i]]++;
    }

    string st = "";
    fr (i, n){
      int stat = -1;
      map<int, int>::iterator it;
      for (it = rep_cnt.begin(); it != rep_cnt.end(); it++){
        int cnt = it->second;
        int val = it->first;
        // cout << val << " " << cnt;
        if (mp[val].size() == cnt){
          // cout << " if\n";
          fr (j, mp[val].size()){
            if ((i >= mp[val][j])  && (i < mp[val][j] + L)){
              stat = 1;
            }
          }
        } else {
          bool bx = false, bx1 = false; int cntx = 0;
          // cout << " else\n";
          if (val == 1 && i == n - 3){
            cout << "v ";
            fr (j, mp[val].size()){
              cout << mp[val][j] << " ";
            }
            cout << endl;
            cout << "i " << i << endl;
            cout << "l " << L << endl;
          }
          fr (j, mp[val].size()){
            if ((i >= mp[val][j])  && (i < mp[val][j] + L)){
              bx1 = true; cntx++;
            } else {
              bx = true;
            }
          }
          if (!bx){
            stat = 1;
          } else if (cnt > (mp[val].size() - cntx)){ 
            stat = 1;
          } else if (bx1 && stat == -1){
            stat = 0;
          }
        }
      }
      if (stat == 1){
        st += '+';
      } else if (stat == 0){
        st += '?';
      } else if (stat == -1){
        st += '-';
      } else {
        st += 'B';
      }
    }

    return st;
	}
};
