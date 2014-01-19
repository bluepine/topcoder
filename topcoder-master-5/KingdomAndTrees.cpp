
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



class KingdomAndTrees {
public:
  int minLevel(vector <int> a) {
    vector<int> b;
    int n = a.size();
    int l = 0, h = 0;
    b.push_back(0);
    fr (i, n){
      b.push_back(a[i]);
      h = max(h, a[i]);
    }
    h = min(h + n, 1000000000);
    a.clear();
    while (l < h){
      //cout << l << " " << h << endl;
      int mid = (l + h)/ 2;
      bool pass = true;
      a.clear();
      a.push_back(0);
      fr (i, n){
	if (a[i] - b[i + 1] >=  mid){
	  pass = false;
	  break;
	}
	a.push_back(max(a[i] + 1, b[i + 1] - mid));
      }
      if (pass){
	h = mid;
      } else {
	l = mid + 1;
      }
    }
    return l;
  }
};
