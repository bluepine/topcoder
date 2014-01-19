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
#define PB push_back
#define MP make_pair

class RedAndGreen {
     public:

     int minPaints(string row) {
       int ret = 1 << 30;
       for(int i=0;i<row.size()+1;i++){
	 int tmp=0;
	 for(int j=0;j<row.size();j++){
	   if(j < i){
	     if(row[j] == 'G'){
	       tmp++;
	     }
	   }else{
	     if(row[j] == 'R'){
	       tmp++;
	     }
	   }
	 }
	 ret = min(ret,tmp);
       }
       return ret;

     }

     


};





// Powered by FileEdit
// Powered by TZTester 1.01 [25-Feb-2003]
// Powered by CodeProcessor
