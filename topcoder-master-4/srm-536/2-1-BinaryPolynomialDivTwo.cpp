// BEGIN CUT HERE
// PROBLEM STATEMENT
// Warning: This problem statement contains superscripts 
// and/or subscripts. It may not display properly outside of 
// the applet.
// 
// In this problem we will consider binary polynomials. A 
// binary polynomial can be written in the following form:
// 
// 
// P(x) = a[0] * x0 + a[1] * x1 + ... + a[n] * xn
// 
// 
// The values a[i] are integer constants (called 
// coefficients). For a binary polynomial we must have a[n] = 
// 1 and each other a[i] must be either 0 or 1. The number n 
// is called the degree of the polynomial.
// 
// 
// Several examples:
// 
//  P(x) = 1 * x0 + 0 * x1 + 1 * x2 is a binary polynomial of 
// degree 2. 
//  P(x) = 0 * x0 + 1 * x1 + 0 * x2 + 1 * x3 is a binary 
// polynomial of degree 3. 
//  P(x) = 1 * x0 is a binary polynomial of degree 0. 
//  P(x) = 0 * x0 + 3 * x1 - 1 * x2 is not a binary 
// polynomial, because each coefficient must be a 0 or a 1. 
//  P(x) = 0 * x0 is not a valid binary polynomial, because 
// the last of the values a[i] must be 1. 
// 
// We can evaluate a binary polynomial for the inputs x = 0 
// and x = 1. In order to do so, we just have to substitute 0 
// or 1 for x in the above expression, compute the value of 
// the expression and take the remainder it gives when 
// divided by two. For example, if P(x) = 1 * x0 + 0 * x1 + 1 
// * x2, then P(0) = 1 * 00 + 0 * 01 + 1 * 02 = 1 and P(1) = 
// 1 * 10 + 0 * 11 + 1 * 12 = 0 (modulo 2). Note that in this 
// problem we assume that x0 = 1 for all x. In particular, 
// also 00 = 1.
// 
// 
// We call an integer x (where x is 0 or 1) a root of the 
// binary polynomial P if P(x) = 0. You are given a binary 
// polynomial P as the array a of its coefficients. Return 
// the number of roots of that binary polynomial.
// 
// DEFINITION
// Class:BinaryPolynomialDivTwo
// Method:countRoots
// Parameters:vector <int>
// Returns:int
// Method signature:int countRoots(vector <int> a)
// 
// 
// CONSTRAINTS
// -The degree of P will be between 0 and 49, inclusive.
// -a will contain exactly n+1 elements, where n is the 
// degree of P.
// -Each element of a will be either 0 (zero) or 1 (one).
// -a[n] will be equal to 1 (one).
// 
// 
// EXAMPLES
// 
// 0)
// {1, 0, 1}
// 
// Returns: 1
// 
// The example from the problem statement. The only root of 
// this binary polynomial is 1.
// 
// 1)
// {0, 1, 0, 1}
// 
// Returns: 2
// 
// This is the maximum possible answer; both 0 and 1 are 
// roots of this binary polynomial.
// 
// 2)
// {1}
// 
// Returns: 0
// 
// This binary polynomial has no roots - it always evaluates 
// to 1.
// 
// 3)
// {1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 1}
// 
// Returns: 0
// 
// 
// 
// 4)
// {1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 
// 0, 1, 0, 1, 1, 0,
//  0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 
// 0, 1, 0, 0, 1, 1}
// 
// Returns: 1
// 
// 
// 
// END CUT HERE
#line 109 "BinaryPolynomialDivTwo.cpp"
#include <string>
#include <vector>
#include <iostream>
#include <sstream>
#include <algorithm>
#include <stdio.h>

using namespace std;

class BinaryPolynomialDivTwo
    {
    public:
    int countRoots(vector <int> a)
    {
        int ret = 0;
        int i, n;
        if (!a[0])
            ret = 1;
        n=0;
        for (i = 0; i < a.size(); i++)
            n+=a[i];
        if (!(n%2))
            ret++;
        return ret;
    }
    
// BEGIN CUT HERE
	public:
	void run_test(int Case) { if ((Case == -1) || (Case == 0)) test_case_0(); if ((Case == -1) || (Case == 1)) test_case_1(); if ((Case == -1) || (Case == 2)) test_case_2(); if ((Case == -1) || (Case == 3)) test_case_3(); if ((Case == -1) || (Case == 4)) test_case_4(); }
	private:
	template <typename T> string print_array(const vector<T> &V) { ostringstream os; os << "{ "; for (typename vector<T>::const_iterator iter = V.begin(); iter != V.end(); ++iter) os << '\"' << *iter << "\","; os << " }"; return os.str(); }
	void verify_case(int Case, const int &Expected, const int &Received) { cerr << "Test Case #" << Case << "..."; if (Expected == Received) cerr << "PASSED" << endl; else { cerr << "FAILED" << endl; cerr << "\tExpected: \"" << Expected << '\"' << endl; cerr << "\tReceived: \"" << Received << '\"' << endl; } }
	void test_case_0() { int Arr0[] = {1, 0, 1}; vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 1; verify_case(0, Arg1, countRoots(Arg0)); }
	void test_case_1() { int Arr0[] = {0, 1, 0, 1}; vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 2; verify_case(1, Arg1, countRoots(Arg0)); }
	void test_case_2() { int Arr0[] = {1}; vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 0; verify_case(2, Arg1, countRoots(Arg0)); }
	void test_case_3() { int Arr0[] = {1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 1}; vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 0; verify_case(3, Arg1, countRoots(Arg0)); }
	void test_case_4() { int Arr0[] = {1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0,
 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 1, 1}; vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 1; verify_case(4, Arg1, countRoots(Arg0)); }

// END CUT HERE

    };

// BEGIN CUT HERE
int main()
    {
    BinaryPolynomialDivTwo ___test;
    ___test.run_test(-1);
    }
// END CUT HERE
