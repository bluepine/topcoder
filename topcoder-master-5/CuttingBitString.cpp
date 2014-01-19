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

typedef unsigned long long ll;



class CuttingBitString {
public:
  bool chk(ll px){
    while (px != 1){
      if (px % 5 != 0){
	return false;
      }
      px /= 5;
    }
    return true;
  }

  int len(ll px){
    int res = 0;
    while (px != 0){
      res++;
      px /= 2;
    }
    return res;
  }

  int gt(ll x){
    if (x % 2 == 0){
      return -1;
    }
    if (chk(x)){
      return 1;
    } else {
      ll st = 1;
      int cut = 100;
      while (st < x){
	int l = len(st);
	ll y = x>>l;
	ll xtemp = y<<l;
	if ((x - xtemp) == st){
	  int cut1 = gt(y);
	  if (cut1 != -1){
	    cut = min(cut, cut1 + 1);
	  }
	}
	st = st * 5;
      }
      if (cut == 100){
	return -1;
      } else {
	return cut;
      }
    }
  }
  

  int getmin(string S) {
    int len = S.size();
    ll res = 0;
    if (S[0] == '0'){
      return -1;
    }
    fr (i, len){
      if (S[i] == '1'){
	res = res * 2 + 1;
      } else {
	res = res * 2;
      }
    }
    cout << "len " << len << endl;
    cout << "res " << res << endl;
    return gt(res);
  }
};


// Powered by FileEdit
