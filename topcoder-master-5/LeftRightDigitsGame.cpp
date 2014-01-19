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



class LeftRightDigitsGame {
public:
  string minNumber(string digits) {
    int n = digits.size();
    int a[50];
    int min_dig = 9;
    fr (i, n){  
      a[i] = digits[i] - '0';
      if (a[i] != 0){
	min_dig = min(min_dig, a[i]);
      }
    }
    
    int min_pos[50];
    int min_count = 0;
    
    string min_res[50];
    int min_res_count = 0; 
     
    fr (i, n){
      if (a[i] == min_dig){
	min_pos[min_count++] = i;
      }
    }
    
    fr (i, min_count){
      string temp (digits, 0, 1);
      cout << " t " << temp << endl;
      f (j, 1, min_pos[i]){
	if (temp[0] - '0' >= digits[j] -'0'){
	  temp = digits[j] + temp;
	} else {
	  temp = temp + digits[j];
	}
      }
      if (min_pos[i] != 0) {
	temp = digits[min_pos[i]] + temp;
      }
      f (j, min_pos[i] + 1, n){
	temp = temp + digits[j];
      }
      min_res[min_res_count++] = temp;      
      cout << temp << endl;
    }

    sort(min_res, min_res + min_res_count);

    return min_res[0];
  }
};
