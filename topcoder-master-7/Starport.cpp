#include <vector>
#include <list>
#include <map>
#include <set>
#include <deque>
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

class Starport {
     public:

  int gcd(int x, int y){
	if(x < y) swap(x, y);
	if(y == 0){
	  return x;
	}else{
	  return gcd(y, x%y);
	}
  }

  long long lcm(int x, int y){
	int n;

	n = gcd(x,y);
	return (long long)x * (long long)y / n;
  }


     double getExpectedTime(int N, int M) {
	   return (double)(N - gcd(N, M))/ 2;
	 }
};





// Powered by FileEdit
// Powered by TZTester 1.01 [25-Feb-2003]
// Powered by CodeProcessor


// Powered by FileEdit
// Powered by TZTester 1.01 [25-Feb-2003]
// Powered by CodeProcessor
