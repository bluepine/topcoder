#include <vector>
#include <list>
#include <map>
#include <set>
#include <deque>
#include <queue>
#include <stack>
#include <bitset>
#include <algorithm>
#include <functional>
#include <numeric>
#include <utility>
#include <sstream>
#include <iostream>
#include <iomanip>
#include <cstdio>
#include <cmath>
#include <cstdlib>
#include <cctype>
#include <string>
#include <cstring>
#include <ctime>

using namespace std;

#define dump(x)  cerr << #x << " = " << (x) << endl;
inline int toInt(string s){int v;istringstream sin(s);sin>>v;return v;}
template<class T> inline string toString(T x){ostringstream sout;sout<<x;return sout.str();}

class SRMCodingPhase {
     public:

  int solve(vector<int> points, vector<int> skills, int x, int y, int z){
    int ret=0;
    skills[0] -= x;
    skills[1] -= y;
    skills[2] -= z;

    if(skills[0] <= 0) skills[0] = 1;
    if(skills[1] <= 0) skills[1] = 1;
    if(skills[2] <= 0) skills[2] = 1;

    if(skills[0]+skills[1]+skills[2] <= 75){
      return (points[0] - 2*skills[0])+(points[1] - 4*skills[1])+(points[2] - 8*skills[2]);
    }
    
    if(skills[0]+skills[1] <= 75){
      ret = max(ret, (points[0] - 2*skills[0])+(points[1] - 4*skills[1]));
    }
    if(skills[0]+skills[2] <= 75){
      ret = max(ret, (points[0] - 2*skills[0])+(points[2] - 8*skills[2]));
    }
    if(skills[1]+skills[2] <= 75){
      ret = max(ret, (points[1] - 4*skills[1])+(points[2] - 8*skills[2]));
    }
    if(skills[0] <= 75){
      ret = max(ret, points[0] - 2 * skills[0]);
    }
    if(skills[1] <= 75){
      ret = max(ret, points[1] - 4 * skills[1]);
    }
    if(skills[2] <= 75){
      ret = max(ret, points[2] - 8 * skills[2]);
    }
    
    return ret;
  }

     int countScore(vector <int> points, vector <int> skills, int luck) {
       int ret=0;
       
       for(int i=0;i<=luck;i++){
	 for(int j=0;j<=luck-i;j++){
	   for(int k=0;k<=luck-i-j;k++){
	     ret = max(ret, solve(points, skills, i,j,k));
	   }
	 }
       }
       return ret;
     }

     


};





// Powered by FileEdit
// Powered by TZTester 1.01 [25-Feb-2003]
// Powered by CodeProcessor
