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
ll mod  = 555555555;

class XorBoard {
public:
  int count(int h, int w, int Rcount, int Ccount, int S) {
    int n = max(h + Rcount, w + Ccount);
    cout << n << endl;
    cout << " seg sev2\n";
    int* bin[n + 1];
    cout << " seg sev\n";
    fr (i, n + 1){
      bin[i] = new int[n + 1];
      fr (j, n + 1){
	bin[i][j] = 0;
      }
    }
    fr (i, n + 1){
      bin[i][0] = 1;
    }


    f (i, 1, n + 1){
      f (j, 1, i + 1){
	bin[i][j] = (bin[i - 1][j - 1] + bin[i - 1][j]) % mod;;
      }      
    }

    
    ll res = 0;
    ll temp = 0;

    for (int i = (Rcount % 2); i <= min(Rcount, h); i += 2){
      for (int j = (Ccount % 2); j <= min(Ccount, w); j += 2){
	if ((i * w + j * h - 2 * i * j) == S){
	  temp = (ll)bin[w][j] * (ll)bin[h][i];
	  temp %= mod; temp = temp * (ll)bin[(Rcount - i) / 2 + h - 1][h - 1];
	  temp %= mod; temp = temp * (ll)bin[(Ccount - j) / 2 + w - 1][w - 1];
	  temp %= mod; res += temp; res %= mod;
	}
      }      
    }

    return res;
  }
};


// Powered by FileEdit
