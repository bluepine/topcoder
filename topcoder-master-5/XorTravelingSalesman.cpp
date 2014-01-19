// written by loner dude(dvdreddy)
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
typedef pair<int, int> pi;


class XorTravelingSalesman {
public:
  int maxProfit(vector <int> cityValues, vector <string> roads) {
    bool disc[50][1024];
    queue<pi> q;
    int n = cityValues.size();
    fr (i, n){
      fr (j, 1024){
	disc[i][j] = false;
      }
    }
    disc[0][cityValues[0]] = true;
    int res = cityValues[0];
    q.push(pi(0, cityValues[0]));
    while (!q.empty()){
      pi p = q.front();
      q.pop();
      fr (i, n){
	if (roads[p.first][i] == 'Y'){
	  if (!disc[i][p.second ^ cityValues[i]]){
	    disc[i][p.second ^ cityValues[i]] = true;
	    q.push(pi(i, p.second ^ cityValues[i]));
	    res = max(res, p.second ^ cityValues[i]);
	  }
	}
      }
      
    }
    return res;
  }
};
