// BEGIN CUT HERE
// PROBLEM STATEMENT
// There are exactly X*Y places in the Planar Kingdom: For 
// each pair of integers (x, y) such that 0 <= x < X and 0 <= 
// y < Y there is a place with coordinates (x, y). When a 
// citizen of the kingdom wants to move from (x1, y1) to (x2, 
// y2), the required time is |x1 - x2| + |y1 - y2| (where |t| 
// denotes the absolute value of t).
// 
// In order to improve stability in the kingdom, the police 
// wants to introduce a specific patrol route. The route will 
// contain exactly three places A, B, and C.
// A policeman will visit these three places and verify that 
// everything is as it should be. The three places that 
// determine a valid route must satisfy the following 
// criteria::
// 
// x-coordinates of A, B and C are pairwise distinct.
// y-coordinates of A, B and C are pairwise distinct.
// Let T be the total time required to follow along the 
// route: first from A to B, then from B to C and finally 
// from C back to A. T must be between minT and maxT, 
// inclusive.
// 
// 
// You are given the ints X, Y, minT, and maxT. Return the 
// number of different patrol routes that satisfy these 
// criteria, modulo 1,000,000,007. Two routes are considered 
// to be different if there is a place that belongs to one of 
// them, but does not belong to the other one.
// 
// DEFINITION
// Class:PatrolRoute
// Method:countRoutes
// Parameters:int, int, int, int
// Returns:int
// Method signature:int countRoutes(int X, int Y, int minT, 
// int maxT)
// 
// 
// CONSTRAINTS
// -X and Y will each be between 3 and 4,000, inclusive.
// -minT will be between 1 and 20,000, inclusive.
// -maxT will be between minT and 20,000, inclusive.
// 
// 
// EXAMPLES
// 
// 0)
// 3
// 3
// 1
// 20000
// 
// Returns: 6
// 
// The time requirement is very flexible in this case. There 
// are 6 patrol routes where both x and y coordinates of 
// places are pairwise distinct.
// 
// 1)
// 3
// 3
// 4
// 7
// 
// Returns: 0
// 
// The time of 7 is too small for any patrol route.
// 
// 2)
// 4
// 6
// 9
// 12
// 
// Returns: 264
// 
// 
// 
// 3)
// 7
// 5
// 13
// 18
// 
// Returns: 1212
// 
// 
// 
// 4)
// 4000
// 4000
// 4000
// 14000
// 
// Returns: 859690013
// 
// Don't forget about the modulo!
// 
// END CUT HERE
#line 103 "PatrolRoute.cpp"
#include <string>
#include <vector>
#include <iostream>
#include <sstream>
#include <algorithm>
#include <stdio.h>

using namespace std;

class PatrolRoute
    {
    public:
    int countRoutes(int X, int Y, int minT, int maxT)
    {
        int px[4000] = {0};
        int py[4000] = {0};
        int i;
        int j;
        int m, n;
        long long ret;
        for (i = 0; i < X; i++)
            for (j = i + 2; j < X; j++)
                px[j - i] += j - i - 1;
        for (i = 0; i < Y; i++)
            for (j = i + 2; j < Y; j++)
                py[j - i] += j - i - 1;
        ret = 0;
        for (i = 2; i < 4000; i++) {
            for (j = 2; j < 4000; j++) {
                if (!px[i] || !py[j])
                    continue;
                int dist = 2 * (i + j);
                if (dist >= minT && dist <= maxT) {
                    ret += (long long)px[i] * py[j];
                    ret %= 1000000007LL;
                }
            }
        }
        ret *= 6;
        ret %= 1000000007LL;
        return ret;
    }
    
// BEGIN CUT HERE
	public:
	void run_test(int Case) { if ((Case == -1) || (Case == 0)) test_case_0(); if ((Case == -1) || (Case == 1)) test_case_1(); if ((Case == -1) || (Case == 2)) test_case_2(); if ((Case == -1) || (Case == 3)) test_case_3(); if ((Case == -1) || (Case == 4)) test_case_4(); }
	private:
	template <typename T> string print_array(const vector<T> &V) { ostringstream os; os << "{ "; for (typename vector<T>::const_iterator iter = V.begin(); iter != V.end(); ++iter) os << '\"' << *iter << "\","; os << " }"; return os.str(); }
	void verify_case(int Case, const int &Expected, const int &Received) { cerr << "Test Case #" << Case << "..."; if (Expected == Received) cerr << "PASSED" << endl; else { cerr << "FAILED" << endl; cerr << "\tExpected: \"" << Expected << '\"' << endl; cerr << "\tReceived: \"" << Received << '\"' << endl; } }
	void test_case_0() { int Arg0 = 3; int Arg1 = 3; int Arg2 = 1; int Arg3 = 20000; int Arg4 = 6; verify_case(0, Arg4, countRoutes(Arg0, Arg1, Arg2, Arg3)); }
	void test_case_1() { int Arg0 = 3; int Arg1 = 3; int Arg2 = 4; int Arg3 = 7; int Arg4 = 0; verify_case(1, Arg4, countRoutes(Arg0, Arg1, Arg2, Arg3)); }
	void test_case_2() { int Arg0 = 4; int Arg1 = 6; int Arg2 = 9; int Arg3 = 12; int Arg4 = 264; verify_case(2, Arg4, countRoutes(Arg0, Arg1, Arg2, Arg3)); }
	void test_case_3() { int Arg0 = 7; int Arg1 = 5; int Arg2 = 13; int Arg3 = 18; int Arg4 = 1212; verify_case(3, Arg4, countRoutes(Arg0, Arg1, Arg2, Arg3)); }
	void test_case_4() { int Arg0 = 4000; int Arg1 = 4000; int Arg2 = 4000; int Arg3 = 14000; int Arg4 = 859690013; verify_case(4, Arg4, countRoutes(Arg0, Arg1, Arg2, Arg3)); }

// END CUT HERE

    };

// BEGIN CUT HERE
int main()
    {
    PatrolRoute ___test;
    ___test.run_test(-1);
    }
// END CUT HERE
