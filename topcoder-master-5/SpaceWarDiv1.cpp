// BEGIN CUT HERE
// END CUT HERE
#line 4 "SpaceWarDiv1.cpp"
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


struct st{
  int str;
  ll cnt;
};

bool cmp(st x, st y){
  if (x.str > y.str){
    return false;
  } else if (x.str < y.str){
    return true;
  } else {
    return (x.cnt < y.cnt);
  }
}

int n;
int mgs[50];
int m;
int ens[50];
ll enc[50];
st val[50];
st val1[50];
ll tot;

bool is_poss(ll mid){

  fr (i, m){
    val1[i].str = val[i].str;
    val1[i].cnt = val[i].cnt;
  }

  ll tot1 = tot;

  fr (i, n){
    int val = m - 1;
    fr (j, m){
      if (val1[j].str > mgs[i]){
	val = j - 1;
	break;
      }
    }
    ll cnt = mid;
    for (int j = val; j >= 0; j--){
      ll x = min(cnt, val1[j].cnt);
      val1[j].cnt -= x;
      cnt -= x;
      if (cnt == 0){
	break;
      }
    }
    tot1 -= (mid - cnt);
    if (tot1 == 0){
      break;
    }
  }
  return (tot1 == 0);
}

class SpaceWarDiv1 {
public:
  long long minimalFatigue(vector <int> magicalGirlStrength, vector <int> enemyStrength, vector<long long> enemyCount) {
    n = magicalGirlStrength.size();
    m = enemyStrength.size();

    tot = 0;

    int maxi1 = 0, maxi2 = 0;
    fr (i, n){
      mgs[i] = magicalGirlStrength[i];
      maxi1 = max(mgs[i], maxi1);
    }

    fr (i, m){
      ens[i] = enemyStrength[i];
      enc[i] = enemyCount[i];
      val[i].str = ens[i];
      val[i].cnt = enc[i];
      tot += enc[i];
      maxi2 = max(ens[i], maxi2);
    }

    if (maxi1 < maxi2){
      return -1;
    }
    
    sort (val, val + m, cmp);

    ll lo = 1; ll hi = 10000000000000001LL;

    while (hi > lo){
      ll mid = (lo + hi) / (2LL);
      cout << "l1 " << mid << " " << is_poss(mid) << endl;
      if (is_poss(mid)){
	hi = mid;
      } else {
	lo = mid + 1;
      }

    }
    return hi;
    
  }
};
