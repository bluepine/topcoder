/*
BEGINCUT$
PROBLEMDESC$
ENDCUT$
#line 6 "RotatingBot.cpp"
*/
#include <vector>
#include <queue>
#include <deque>
#include <map>
#include <set>
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
typedef pair<int,int> pi;


class RotatingBot {
public:
  int minArea(vector <int> moves) {
    set<pi> s;
    set<pi>::iterator it;
    int x[] = {1, 0, -1, 0};
    int y[] = {0, 1, 0, -1};
    pi cur = pi(0, 0);
    int xrange = 1;
    int yrange = 1;
    int tempx , tempy;
    int n = moves.size();
    if (n == 1){
      return moves[0] + 1;
    } else if (n == 2){      
      return (moves[0] + 1) * (moves[1] + 1);
    } else if (n == 3){
      return (max(moves[2], moves[0]) + 1) * (moves[1] + 1);
    } else if (n == 4){ 
      if (moves[2] < moves[0])
	return  -1;
      if (moves[2] == moves[0] && moves[3] >= moves[1])
	return -1;
      return (moves[2] + 1) * (max(moves[1], moves[3]) + 1);
    } else {      
      if (moves[2] == moves[0]){
	if (moves[3] != moves[1] - 1){
	  return -1;
	} else {
	  tempx = moves[2] - 1;
	  tempy = moves[3] - 1;
	  cout << "a " << tempx << " " << tempy << endl;
	  for (int i = 4; i < n; i += 2){
	    if (i == n -1){
	      if (moves[i] > tempx){
		return -1;
	      }
	      continue;
	    }
	    if (moves[i] != tempx){
	      return -1;
	    }
	    tempx -=1;
	  }
	  
	  for (int i = 5; i < n; i += 2){
	    if (i == n -1){
	      if (moves[i] > tempy){
		return -1;
	      }
	      continue;
	    }
	    if (moves[i] != tempy){
	      return -1;
	    }
	    tempy -=1;
	  }
	  return (moves[2] + 1) * (moves[1] + 1);
	}
      } else {
	if (moves[3] < moves[1])
	  return -1;
	if (moves[3] == moves[1]){
	  tempx = moves[2] - moves[0] -1;
	  tempy = moves[3] - 1;
	} else {
	  tempx = moves[2];
	  tempy = moves[3] - moves[1] - 1;
	}
	cout << "b " << tempx << " " << tempy << endl;
	for (int i = 4; i < n; i += 2){
	  if (i == n -1){
	    if (moves[i] > tempx){
	      return -1;
	    }
	    continue;
	  }
	  if (moves[i] != tempx){
	    return -1;
	  }
	  tempx -=1;
	}
	
	for (int i = 5; i < n; i += 2){
	  if (i == n -1){
	    if (moves[i] > tempy){
	      return -1;
	    }
	    continue;
	  }

	  if (moves[i] != tempy){
	    return -1;
	  }
	  tempy -=1;
	}
	return (moves[2] + 1) * (moves[3] + 1);		
      }
    }
  }
};


// Powered by FileEdit
