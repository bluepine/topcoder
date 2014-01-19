/*
BEGINCUT$
PROBLEMDESC$
ENDCUT$
#line 6 "EasyConversionMachine.cpp"
*/
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



class EasyConversionMachine {
public:
  string isItPossible(string originalWord, string finalWord, int k) {
    int res = 0;
    fr (i, originalWord.size()){
      res += fabs(originalWord[i] - finalWord[i]);
      
    }
    if ( res <= k && (res - k) % 2 == 0){
      return "POSSIBLE";
    } else {
      return "IMPOSSIBLE";
    }
  }
};


// Powered by FileEdit
