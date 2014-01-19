#include <algorithm>
#include <cfloat>
#include <climits>
#include <cmath>
#include <complex>
#include <cstdio>
#include <cstdlib>
#include <functional>
#include <iostream>
#include <list>
#include <map>
#include <memory>
#include <queue>
#include <set>
#include <sstream>
#include <stack>
#include <string>
#include <utility>
#include <vector>

typedef int int_type;

// -- utility --
// C-style loop
#define FOR(x, a, b) for(int_type x = static_cast<int_type>(a); x < static_cast<int_type>(b); ++x)
// Ruby-style loop
#define TIMES(x, n) FOR(x, 0, n)
#define STEP(x, a, b, s) for(int_type x = static_cast<int_type>(a); s > 0 ? x <= static_cast<int_type>(b) : x >= static_cast<int_type>(b); x += static_cast<int_type>(s) )
#define UPTO(x, a, b) for(int_type x = static_cast<int_type>(a); x <= static_cast<int_type>(b); ++x)
#define DOWNTO(x, a, b) for(int_type x = static_cast<int_type>(a); x >= static_cast<int_type>(b); --x)
#define EACH(c, i) for(__typeof((c).begin()) i = (c).begin(); i != (c).end(); ++i)
#define ALL(cont, it, cond, ret) \
  bool ret = true; EACH(cont, it) { if(!(cond)) {ret=false;break;} }
#define ANY(cont, it, cond, ret) \
  bool ret = false; EACH(cont, it) { if(cond) {ret=true;break;} }

using namespace std;
// debug
// BEGIN CUT HERE
#define DUMP(x) std::cerr << #x << " = " << to_s(x) << std::endl;
template<typename T> string to_s(const T& v);
template<> string to_s(const string& v);
template<> string to_s(const bool& v);
template<typename T> string to_s(const vector<T>& v);
template<typename T> string to_s(const list<T>& v);
template<typename T> string to_s(const set<T>& v);
template<typename F, typename S> string to_s(const pair<F,S>& v);
template<typename K, typename V> string to_s(const map<K,V>& v);
// END CUT HERE
#ifndef DUMP
#define DUMP(x) 
#endif

class KSubstring {

 public:
  vector <int> maxSubstring(int A0, int X, int Y, int M, int n)
  {
    vector <int> result;
    // -- main code --

    typedef long long ll;
    vector<ll> a(n, 0);

    a[0] = A0;
    FOR(i, 1, n) { a[i] = (a[i-1] * X) % M; }

    int mk = 0;
    int md = INT_MAX;

    vector< vector< ll > > dd;
    
    
    FOR(k, 2, n) {
      dd.push_back(vector<ll>());
      
    }
    
    return result;
  }

// BEGIN CUT HERE
  void debug()
  {
  }
/*
// PROBLEM STATEMENT
// 
	You are given numbers A0, X, Y, M and n. 
	Generate a list A of length n using the following recursive definition:
	A[0] = A0
	A[i] = (A[i - 1] * X + Y) MOD M (note that A[i - 1] * X + Y may overflow 32-bit integer)



	Let s(i, k) be a sum of k consecutive elements of the list A starting with the element at
	position i (0 indexed).
	More formally, s(i, k) = A[i] + A[i + 1] + ... + A[i + k - 1].
	
	Your task is to find the smallest difference between s(i, k) and s(j, k) 
	(where difference is defined as abs(s(i, k) - s(j, k))) such that
	i + k <= j. 
	In other words, you must find the smallest difference between two subsequences of the same 
	length that do not overlap.
	Call this smallest difference val, and return a vector <int> containing exactly two elements.
	The first element should be k, and the second element should be val.
	If there are multiple solutions with the same val, return the one among them with the highest k.



DEFINITION
Class:KSubstring
Method:maxSubstring
Parameters:int, int, int, int, int
Returns:vector <int>
Method signature:vector <int> maxSubstring(int A0, int X, int Y, int M, int n)


CONSTRAINTS
-M will be between 1 and 1,000,000,000, inclusive.
-A0 will be between 0 and M-1, inclusive.
-X will be between 0 and M-1, inclusive.
-Y will be between 0 and M-1, inclusive.
-n will be between 2 and 3,000, inclusive.


EXAMPLES

0)
5
3
4
25
5

Returns: {2, 1 }

The elements of the list are {5, 19, 11, 12, 15}. There is no way to find two subsequences that have equal sums and do not overlap, so there is no way to obtain 0 as a difference. |s(0, 2) - s(2, 2)| = 1 and that is the minimal difference. Note that |s(2, 1) - s(3, 1)| is also 1, but we don't choose these subsequences because they have a lower value for k.

1)
8
19
17
2093
12

Returns: {5, 161 }

The elements of the list are {8, 169, 1135, 652, 1940, 1296, 1618, 1457, 491, 974, 1779, 330}. The smallest difference is |s(1, 5) - s(7, 5)| = 161.

2)
53
13
9
65535
500

Returns: {244, 0 }



3)
12
34
55
7890
123

Returns: {35, 4 }



*/
// END CUT HERE
  
  
// BEGIN CUT HERE
	public:
	void run_test(int Case) { if ((Case == -1) || (Case == 0)) test_case_0(); if ((Case == -1) || (Case == 1)) test_case_1(); if ((Case == -1) || (Case == 2)) test_case_2(); if ((Case == -1) || (Case == 3)) test_case_3(); }
	private:
	template <typename T> string print_array(const vector<T> &V) { ostringstream os; os << "{ "; for (typename vector<T>::const_iterator iter = V.begin(); iter != V.end(); ++iter) os << '\"' << *iter << "\","; os << " }"; return os.str(); }
	void verify_case(int Case, const vector <int> &Expected, const vector <int> &Received) { cerr << "Test Case #" << Case << "..."; if (Expected == Received) cerr << "PASSED" << endl; else { cerr << "FAILED" << endl; cerr << "\tExpected: " << print_array(Expected) << endl; cerr << "\tReceived: " << print_array(Received) << endl; } }
	void test_case_0() { int Arg0 = 5; int Arg1 = 3; int Arg2 = 4; int Arg3 = 25; int Arg4 = 5; int Arr5[] = {2, 1 }; vector <int> Arg5(Arr5, Arr5 + (sizeof(Arr5) / sizeof(Arr5[0]))); verify_case(0, Arg5, maxSubstring(Arg0, Arg1, Arg2, Arg3, Arg4)); }
	void test_case_1() { int Arg0 = 8; int Arg1 = 19; int Arg2 = 17; int Arg3 = 2093; int Arg4 = 12; int Arr5[] = {5, 161 }; vector <int> Arg5(Arr5, Arr5 + (sizeof(Arr5) / sizeof(Arr5[0]))); verify_case(1, Arg5, maxSubstring(Arg0, Arg1, Arg2, Arg3, Arg4)); }
	void test_case_2() { int Arg0 = 53; int Arg1 = 13; int Arg2 = 9; int Arg3 = 65535; int Arg4 = 500; int Arr5[] = {244, 0 }; vector <int> Arg5(Arr5, Arr5 + (sizeof(Arr5) / sizeof(Arr5[0]))); verify_case(2, Arg5, maxSubstring(Arg0, Arg1, Arg2, Arg3, Arg4)); }
	void test_case_3() { int Arg0 = 12; int Arg1 = 34; int Arg2 = 55; int Arg3 = 7890; int Arg4 = 123; int Arr5[] = {35, 4 }; vector <int> Arg5(Arr5, Arr5 + (sizeof(Arr5) / sizeof(Arr5[0]))); verify_case(3, Arg5, maxSubstring(Arg0, Arg1, Arg2, Arg3, Arg4)); }

// END CUT HERE

};

// BEGIN CUT HERE

int main(int argc, char *argv[])
{
  
  KSubstring test;

  if(argc == 1) {
    test.run_test(-1);
  }else {
    std::string arg(argv[1]);
    if(arg[0] != '-') {
      test.run_test(arg[0] - '0');
    }else {
      test.debug();
    }
  }
  
  return 0;
}

template<typename T> string to_s(const T& v) { ostringstream oss; oss << v; return oss.str(); }
template<> string to_s(const string& v) { ostringstream oss; oss << '"' << v << '"'; return oss.str(); }
template<> string to_s(const bool& v) { ostringstream oss; oss << ( v ? "true" : "false") ; return oss.str(); } 
template<typename T> string to_s(const vector<T>& v) { ostringstream oss; oss << "["; EACH(v,i) oss << to_s(*i) << ","; oss << "]"; return oss.str(); }
template<typename T> string to_s(const list<T>& v) { ostringstream oss; oss << "("; EACH(v,i) oss << to_s(*i) << ","; oss << ")"; return oss.str(); }
template<typename T> string to_s(const set<T>& v) { ostringstream oss; oss << "{"; EACH(v,i) oss << to_s(*i) << ","; oss << "}"; return oss.str(); }
template<typename F, typename S> string to_s(const pair<F,S>& v) { ostringstream oss; oss << "<" << to_s(v.first) << " " << to_s(v.second) << ">"; return oss.str(); }
template<typename K, typename V> string to_s(const map<K,V>& v) { ostringstream oss; oss << "{"; EACH(v,i) oss << to_s(i->first) << " => " << to_s(i->second) << ","; oss << "}"; return oss.str(); }

// END CUT HERE
