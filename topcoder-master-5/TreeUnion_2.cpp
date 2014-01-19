// BEGIN CUT HERE

// END CUT HERE
// #line 5 "TreeUnion.cpp"
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
#include <sstream>

using namespace std;

#define si(x) scanf("%d",&x)
#define sll(x) scanf("%lld",&x)
#define sf(x) scanf("%lf",&x)
#define ss(x) scanf("%s",&x)

#define f(i,a,b) for(int i=a;i<b;i++)
#define fr(i,n)  f(i,0,n)

typedef long long ll;


int INF = 300000

class TreeUnion {
	public:
	double expectedCycles(vector <string> tree1, vector <string> tree2, int K) {
    int a[300][300];
    int b[300][300];
    string stemp = "";
    fr (i, tree1.size()){
      stemp += tree1[i];
    }
    istringstream buff(stemp);
    vector<int> tr1;
    while (!buff.eof()){
      int x; buff >> x;
      tr1.push_back(x);
    }
    int n1 = tr1.size() + 1;
    fr (i, n1){
      fr (j, n1){
        a[i][j] = INF;
      }
    }

    fr (i, tr1.size()){
      a[i + 1][tr1[i]] = 1;
      a[tr1[i]][i + 1] = 1;
    }
    fr (p, n1){
      fr (i, n1){
        fr (j, n1){
          if (a[i][p] + a[p][j] < a[i][j]){
            a[i][j] = a[i][p] + a[p][j];
          }
        }
      }
    }

    stemp = "";
    fr (i, tree2.size()){
      stemp += tree1[i];
    }
    istringstream buff1(stemp);
    tr1.clear();
    while (!buff1.eof()){
      int x; buff1 >> x;
      tr1.push_back(x);
    }
    int n2 = tr1.size() + 1;
    fr (i, n2){
      fr (j, n2){
        b[i][j] = INF;
      }
    }

    fr (i, tr1.size()){
      b[i + 1][tr1[i]] = 1;
      b[tr1[i]][i + 1] = 1;
    }
    fr (p, n2){
      fr (i, n2){
        fr (j, n2){
          if (b[i][p] + b[p][j] < b[i][j]){
            b[i][j] = b[i][p] + b[p][j];
          }
        }
      }
    }

    map<int, int> mp1;
    map<int, int> mp2;
    
    fr (i, n1){
      f (j, i + 1, n1){
        mp1[a[i][j]]++;
      }
    }

    fr (i, n2){
      fr (j, i + 1, n2){
        mp2[b[i][j]]++;
      }
    }



    long double xx = 0.0;

    f (i, 1, 7){
      f (j, 1, 7){
        if (i + j == K - 1){
          xx += (2.0 / (n1 * (n1 - 1))) * (mp1[i] + 0.0) * (mp2[i] + 0.0);          
        } 
      }
    }
    

	}
};
