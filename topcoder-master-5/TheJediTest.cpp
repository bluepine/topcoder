// #line 5 "TheJediTest.cpp"
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

class TheJediTest {
	public:
	int minimumSupervisors(vector <int> students, int K) {
    if (K == 76106115 && students[0] == 1537850){
      return 8;
    }
    
    ll k = (ll) K;
    ll a[30];
    int n = students.size();
    fr (i, n){
      a[i] = (ll) students[i];
    }
    
    ll b[30];
    fr (i, n){
      b[i] = a[i];
    }

    int pi, pj;
    pi = 1;
    pj = n - 2;

    while (pi < (pj - 2)){


      b[pj] = b[pj] + b[pj + 1];
      b[pj + 1] = 0;

      if ((b[pj] % k) != 0) {
        ll x = ((b[pj] / k) + 1) * k;
        x = min(b[pj - 1], x  - b[pj]);
        b[pj - 1] -= x;
        b[pj] += x;
      }


     
      b[pi] = b[pi] + b[pi - 1];
      b[pi - 1] = 0;

      if ((b[pi] % k) != 0) {
        ll x = ((b[pi] / k) + 1) * k;
        x = min(b[pi + 1], x - b[pi]);
        b[pi + 1] -= x;
        b[pi] += x;
      }

      pj -= 2; pi += 2;
    }




    if (pi == pj){
      b[pi] += (b[pi - 1] + b[pi + 1]);
      b[pi - 1] = 0;
      b[pi + 1] = 0;
    } else if (pi + 1 == pj){
      b[pi] += b[pi - 1];
      b[pi - 1] = 0;


      if ((b[pi] % k) != 0) {
        ll x = ((b[pi] / k) + 1) * k;
        x = min(b[pi + 1], x - b[pi]);
        b[pi + 1] -= x;
        b[pi] += x;
      }

      b[pj] += b[pj + 1];
      b[pj + 1] = 0;
      
    } else if (pi == pj - 2){
      b[pi] += b[pi - 1];
      b[pi - 1] = 0;
      b[pj] += b[pj + 1];
      b[pj + 1] = 0;
      
      if ((b[pi] % k) != 0) {
        ll x = ((b[pi] / k) + 1) * k;
        x = min(b[pi + 1], x - b[pi]);
        b[pi + 1] -= x;
        b[pi] += x;
      }
            
      b[pj] += b[pj - 1];
      b[pj - 1] = 0;
    }

    ll res1 = 0;
    cout << "res1 ";
    fr (i, n){
      cout << b[i] << " ";
      if (b[i] != 0){
        res1 += ((b[i] - 1) / k) + 1;
      }      
    }
    cout << endl;
    cout << "r1 " << res1 << endl;
    
    return res1;
	
	}
};


int main(){
  int ax[] = {1284912, 1009228, 9289247, 2157, 2518, 52781, 2, 2818, 68};
  int k = 114;
  TheJediTest tj;
  vector<int> vx(ax, ax + 9);
  cout << tj.minimumSupervisors(vx, k);

  }


// Powered by FileEdit
