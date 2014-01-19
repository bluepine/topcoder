#line 4 "ColorfulBuilding.cpp"
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

ll mod = 1000000009;

class ColorfulBuilding {
public:
  int count(vector <string> color1, vector <string> color2, int L) {
    map<string, int> mp;
    int c = 0;
    string s1 = "", s2 = "";
    fr (i, color1.size()){
      s1 += color1[i];
    }
    fr (i, color2.size()){
      s2 += color2[i];
    }

    int n = s1.size();
    int color[n];
    map<string, int>::iterator it;
    fr (i, n){      
      string xx = ""; xx += s1[i]; xx += s2[i];
      it = mp.find(xx);      
      if (it == mp.end()){
	mp[xx] = c; c++; color[i] = c - 1;
      } else {
	color[i] = it->second;
      }
    }


    fr (i, n / 2){
      swap(color[i], color[n - 1 - i]);
    }

    fr (i, n){
      cout << color[i] << " ";
    }
    cout << endl;


    cout << "color count " << c << endl;

    ll dp[n][c]; ll mult[c]; ll sum[2][n]; ll temp_arr[n];
    
    memset(dp, 0, sizeof(dp));
    memset(mult, 1, sizeof(mult));
    memset(sum, 0, sizeof(sum));

    dp[0][color[0]] = 1LL;
    sum[0][0] = 1LL;

    fr (i, c){
      mult[i] = 1LL;
    }

    f (i, 1, n){
      cout << "-----------------------\n";
      cout << "cc " << color[i] << " " << endl;

      fr (j, c){
	if (j == color[i]){
	  continue;
	}
	mult[j] = mult[j] * ((ll) i); mult[j] %= mod;
      }


      fr (j, n){
	dp[j][color[i]] *= mult[color[i]];
	dp[j][color[i]] %= mod;
      }
      mult[color[i]] = 1LL;      

      fr (j, n){
	sum[1][j] = sum[0][j] * ((ll) i); sum[1][j] %= mod;
	sum[1][j] += dp[j][color[i]]; sum[1][j] %= mod;
	if (j){
	  sum[1][j] += (sum[0][j - 1] - dp[j - 1][color[i]] + mod);
	  sum[1][j] %= mod;
	}
      }

      fr (j, n){
	temp_arr[j] = dp[j][color[i]];
	dp[j][color[i]] *= ((ll) (i + 1)); dp[j][color[i]] %= mod;
      }


      f (j, 1, n){
	dp[j][color[i]] += (sum[0][j - 1] -  temp_arr[j - 1] + mod);
	dp[j][color[i]] %= mod;
      }

      fr (j, n){
	sum[0][j] = sum[1][j];
      }
    }

    return sum[0][L - 1];
                
  }
};

/*
int main(){
  string xx[] = {"ab", "ba", "a", "aab"};
  string xx2[] = {"bb", "ba", "a", "aba"};

  vector<string> vxx(xx, xx + 4);
  vector<string> vxx2(xx2, xx2 + 4);


  int n = 5;
  ColorfulBuilding x;
  cout << x.count(vxx, vxx2, n) << endl;

  

  return 0;
  }*/


// Powered by FileEdit
