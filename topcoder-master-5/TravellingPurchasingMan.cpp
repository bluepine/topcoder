// BEGIN CUT HERE

// END CUT HERE
// #line 5 "TravellingPurchasingMan.cpp"
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

typedef long ll;

ll INF = 100000000l;

int BitCount(unsigned int u)
{
  unsigned int uCount;

  uCount = u - ((u >> 1) & 033333333333) - ((u >> 2) & 011111111111);
  return ((uCount + (uCount >> 3)) & 030707070707) % 63;
}

bool comp(int x, int y){
  if (BitCount(x) < BitCount(y)){
    return true;
  } else {
    return false;
  }
}


class TravellingPurchasingMan {
public:
  int maxStores(int N, vector <string> interestingStores, vector <string> roads) {
    int n = N;
    ll dist[n][n];
    int m = interestingStores.size();
    ll st_time[m]; ll end_time[m]; ll dur_time[m];
    fr (i, m){
      string temp = interestingStores[i];
      istringstream buff(temp);
      buff >> st_time[i] >> end_time[i] >> dur_time[i];      
    }
    int k = roads.size();
    fr (i, n){
      fr (j, n){
	dist[i][j] = INF;	
      }
      dist[i][i] = 0;
    }
    
    fr (i, k){
      string temp = roads[i];
      istringstream buff(temp);
      int a, b;
      buff >> a >> b; buff >> dist[a][b];
      dist[b][a] = dist[a][b];
    }

    fr (k, n){
      fr (i, n){
	fr (j, n){
	  dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j]);
	  // dist[i][j] = dist[j][i];
	}
      }
    }
    /*
    fr (i, n){
      fr (j, n){
	cout << dist[i][j] << " ";
      }
      cout << endl;
    }
    */
    ll dp[1 << m][m];
    cout << "there \n";
    int all_poss = 1<<m;
    int all_ones = all_poss - 1;
    fr (j, all_poss){
      fr (i, m){
	dp[j][i] = INF;
      }
    }
    if (m == n){
      dp[0][n - 1] = 0;
    } else {
      fr (i, m){
	dp[0][i] = dist[n - 1][i];
      }
    }
    
    int order[all_poss];
    fr (i, all_poss){
      order[i] = i;
    }
    sort(order, order + all_poss, comp);
    /*
    fr (i, all_poss){
      cout << order[i] << " ";
    }
    cout << endl;
    */

    f (i, 1, all_poss){
      int cur = order[i];
      fr (j, m){
	if (cur & (1<<j)){
	  int prev = cur & (all_ones - (1 << j));
	  // cout << cur << " " << prev << endl;
	  ll time = INF;
	  fr (k, m){
	    if (dp[prev][k] + dist[k][j] <= end_time[j]){
	      ll add  = max(dp[prev][k] + dist[k][j], st_time[j]);
	      time = min(time, add + dur_time[j]);
	    }
	  }
	  if (time > INF) time = INF;
	  dp[cur][j] = time;
	}
      }
      /*
      fr (i, m){
	cout << "i ";
	fr (j, all_poss){
	  cout << dp[i][j] << " ";
	}
	cout << endl;
      }    
      cout << "-----------------\n";
      */
    }
    


    int res = 0;
    
    fr (i, all_poss){
      fr (j, m){
	if (dp[i][j] < INF){
	  res = max(res, BitCount(i));
	}
      }
    }

    return res;
  }
};
