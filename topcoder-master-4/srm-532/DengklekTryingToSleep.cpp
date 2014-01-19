// BEGIN CUT HERE
// PROBLEM STATEMENT
// Mr. Dengklek lives in the Kingdom of Ducks, where humans and ducks live 
// together in peace and harmony. The ducks are numbered by distinct positive 
// integers from A to B, inclusive, where A <= B.
// 
// Last night, Mr. Dengklek could not sleep, so he tried to count all the ducks 
// in the kingdom. (It is known that counting ducks can help people to fall 
// asleep.) When counting the ducks, Mr. Dengklek walked across an imaginary 
// meadow and whenever he saw a new duck, he called out its number. He only 
// called out actual duck numbers, i.e., numbers from A to B. He never called the 
// same number twice. The numbers he called out are not necessarily in the 
// numeric order.
// 
// You are given a vector <int> ducks. The elements of ducks are the numbers Mr. 
// Dengklek called out when counting the ducks last night. It is possible that he 
// missed some of the ducks. Obviously, the number of ducks he missed depends on 
// the values A and B. The values of A and B are unknown to you. Compute and 
// return the smallest possible number of ducks Mr. Dengklek might have missed.
// 
// DEFINITION
// Class:DengklekTryingToSleep
// Method:minDucks
// Parameters:vector <int>
// Returns:int
// Method signature:int minDucks(vector <int> ducks)
// 
// 
// CONSTRAINTS
// -ducks will contain between 1 and 50 elements, inclusive.
// -Each element of ducks will be between 1 and 100, inclusive.
// -All element of ducks will be distinct.
// 
// 
// EXAMPLES
// 
// 0)
// {5, 3, 2}
// 
// Returns: 1
// 
// If A=2 and B=5, the only duck Mr. Dengklek missed is the duck number 4.
// 
// 1)
// {58}
// 
// Returns: 0
// 
// If A=B=58, Mr. Dengklek did not miss any ducks.
// 
// 2)
// {9, 3, 6, 4}
// 
// Returns: 3
// 
// In this case, the smallest possible number of missed ducks is 3: the ducks 
// with numbers 5, 7, and 8.
// 
// 3)
// {7, 4, 77, 47, 74, 44}
// 
// Returns: 68
// 
// 
// 
// 4)
// {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}
// 
// Returns: 0
// 
// 
// 
// END CUT HERE
#line 75 "DengklekTryingToSleep.cpp"
#include <iostream>
#include <sstream>
#include <string>
#include <vector>
#include <algorithm>
#include <stdio.h>

using namespace std;

class DengklekTryingToSleep
    {
    public:
    int minDucks(vector <int> ducks)
    {
        int min = 0;
        int max = 0;
        int i;
        for (i=0; i < ducks.size(); i++) {
            int x = ducks[i];
            if (!min || x < min)
                min = x;
            if (!max || x > max)
                max = x;
        }
        return max-min+1 - ducks.size();
    }
    
// BEGIN CUT HERE
	public:
	void run_test(int Case) { if ((Case == -1) || (Case == 0)) test_case_0(); if ((Case == -1) || (Case == 1)) test_case_1(); if ((Case == -1) || (Case == 2)) test_case_2(); if ((Case == -1) || (Case == 3)) test_case_3(); if ((Case == -1) || (Case == 4)) test_case_4(); }
	private:
	template <typename T> string print_array(const vector<T> &V) { ostringstream os; os << "{ "; for (typename vector<T>::const_iterator iter = V.begin(); iter != V.end(); ++iter) os << '\"' << *iter << "\","; os << " }"; return os.str(); }
	void verify_case(int Case, const int &Expected, const int &Received) { cerr << "Test Case #" << Case << "..."; if (Expected == Received) cerr << "PASSED" << endl; else { cerr << "FAILED" << endl; cerr << "\tExpected: \"" << Expected << '\"' << endl; cerr << "\tReceived: \"" << Received << '\"' << endl; } }
	void test_case_0() { int Arr0[] = {5, 3, 2}; vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 1; verify_case(0, Arg1, minDucks(Arg0)); }
	void test_case_1() { int Arr0[] = {58}; vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 0; verify_case(1, Arg1, minDucks(Arg0)); }
	void test_case_2() { int Arr0[] = {9, 3, 6, 4}; vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 3; verify_case(2, Arg1, minDucks(Arg0)); }
	void test_case_3() { int Arr0[] = {7, 4, 77, 47, 74, 44}; vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 68; verify_case(3, Arg1, minDucks(Arg0)); }
	void test_case_4() { int Arr0[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}; vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 0; verify_case(4, Arg1, minDucks(Arg0)); }

// END CUT HERE

    };

// BEGIN CUT HERE
int main()
    {
    DengklekTryingToSleep ___test;
    ___test.run_test(-1);
    }
// END CUT HERE
