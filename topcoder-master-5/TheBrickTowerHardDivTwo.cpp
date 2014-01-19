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
typedef pair<int, ll> pil;

struct st{
  int a[4];
  int np;  
};

bool operator==(const st &left, const st &right){
  bool x = true;
  fr (i, 4){
    x = x & (right.a[i] == left.a[i]);
  }
  return x;
}

bool operator<(const st &left, const st &right){
  fr (i, 4){
    if (left.a[i] < right.a[i]){
      return true;
    }
  }
  return false;
}

bool operator>(const st &left, const st &right){
  fr (i, 4){
      if (right.a[i] < left.a[i]){
	return true;
      }
      return false;
  }
}

int self_eq(st &x){
  int res = 0;
  fr (i, 4){
    f (j, i + 1, 4){
      if (x.a[i] == x.a[j]){
	res++;
      }      
    }
  }
  return res;
}


int dual_eq(st &top, st &bot){
  int res = 0;
  fr (i, 4){
    fr (j, 4){
      if ((i % 4 != (j + 2) % 4) && top.a[i] == bot.a[j]) {
	res++;
      }
    }
  }
  return res;
}

class TheBrickTowerHardDivTwo {
public:
  int find(int c, int k, int h) {
    map<st, pil> m[2];
    
    fr (i, c){
      fr (j, c){
	fr (k, c){
	  fr (p, c){
	    st x;
	    x.a[0] = i; x.a[1] = j;
	    x.a[2] = k; x.a[3] = p;
	    m[0][x] = pil(self_eq(x), 1);
	    m[1][x] = pil(0, 0);
	  }
	}
      }
    }

    map<st,pil>::iterator it, jt;    
    cur = 0;
    f (i, 1, h){
      for (jt = m[cur ^ 1].begin(); jt != m[cur ^ 1].end(); jt++){
	for (it = m[cur].begin(); it != m[cur].end(); it++){
	  
	}
      }
      cur = cur ^ 1;
    }    		
    return 0;
  }
};
