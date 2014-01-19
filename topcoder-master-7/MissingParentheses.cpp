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

class MissingParentheses {
     public:

     int countCorrections(string par) {
       stack<char> st;

       for(int i=0;i<par.size();i++){
	 if(par[i] == ')'){
	   if(st.empty() || st.top() == ')'){
	     st.push(')');
	   }else{
	     st.pop();
	   }
	 }else{
	   st.push('(');
	 }
       }
       return st.size();
     }

     


};





// Powered by FileEdit
// Powered by TZTester 1.01 [25-Feb-2003]
// Powered by CodeProcessor
