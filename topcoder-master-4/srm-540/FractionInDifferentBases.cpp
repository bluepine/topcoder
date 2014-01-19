// BEGIN CUT HERE
// PROBLEM STATEMENT
//
// This problem statement contains superscripts and/or
// subscripts. It may not display properly outside the applet.
//
// It is well-known that when writing a fraction as a
// rational number, we will either get a finite expansion or
// an infinite (but periodic) expansion.
// For example, 1/8 written in base 10 is 0.125, and 1/9
// written in base 10 is 0.1111...
//
// The same fraction can have a finite expansion in some
// bases and an infinite one in other bases.
// For example, 1/9 had an infinite expansion in base 10, but
// 1/9 written in base 3 is 0.01 and 1/9 in base 9 is 0.1.
//
// Little Arthur loves numbers and especially the ones that
// are infinitely long. For a given fraction P/Q he would
// like to find all integer bases between A and B, inclusive,
// such that the fraction has an infinite expansion.
//
// Given ints P, Q, A, and B return the number of such bases.
//
// DEFINITION
// Class:FractionInDifferentBases
// Method:getNumberOfGoodBases
// Parameters:long long, long long, long long, long long
// Returns:long long
// Method signature:long long getNumberOfGoodBases(long long
// P, long long Q, long long A, long long B)
//
//
// NOTES
// -Number X written in an integer base b is a sequence of
// digits (containing a single separator point, if the number
// is not an integer) dndn-1..d1d0.d-1..d-m where each di has
// an integer value between 0 and b-1, inclusive.
// -The notation effectively means that X = dn*bn + dn-1*bn-1
// + .. + d1*b1 + d0*b0 + d-1*b-1 + .. + d-m*b-m.
// -Note that X having an infinite expansion in base b means
// that number X cannot be expressed as a sum with finitely
// many powers of b.
//
//
// CONSTRAINTS
// -P will be between 0 and 1000000000000 (1012), inclusive.
// -Q will be between 1 and 1000000000000 (1012), inclusive.
// -A and B will each be between 2 and 1000000000000000000
// (1018), inclusive.
// -A will be less than or equal to B.
//
//
// EXAMPLES
//
// 0)
// 1
// 2
// 10
// 10
//
// Returns: 0
//
// 1/2 in base 10 is 0.5, thus, there is no base in the
// interval where 1/2 has an infinite expansion.
//
// 1)
// 1
// 9
// 9
// 10
//
// Returns: 1
//
// From the problem statement we see that 1/9 has an infinite
// expansion in base 10, but not in base 9.
//
// 2)
// 5
// 6
// 2
// 10
//
// Returns: 8
//
// 3)
// 2662
// 540
// 2
// 662
//
// Returns: 639
//
// 4)
// 650720
// 7032600
// 2012
// 2012540
//
// Returns: 2010495
//
// END CUT HERE
#line 104 "FractionInDifferentBases.cpp"
#include <string>
#include <vector>
#include <iostream>
#include <sstream>
#include <algorithm>
#include <stdio.h>

using namespace std;

long long gcd(long long a, long long b)
{
    if (!a)
        return b;
    return gcd(b%a, a);
}

long long product_of_factors(long long n)
{
    long long prod = 1;
    long long i;
    for (i = 2; i*i <= n; i++) {
        if (!(n % i)) {
            prod *= i;
            do {
                n /= i;
            } while (!(n % i));
        }
    }
    prod *= n;
    return prod;
}

class FractionInDifferentBases
    {
    public:
    long long getNumberOfGoodBases(long long P, long long Q, long long A, long long B)
    {
        long long g = gcd(P, Q);
        long long prod;
        Q /= g;
        prod = product_of_factors(Q);
        return (B-A+1) - (B/prod - (A-1)/prod);
    }

// BEGIN CUT HERE
	public:
	void run_test(int Case) { if ((Case == -1) || (Case == 0)) test_case_0(); if ((Case == -1) || (Case == 1)) test_case_1(); if ((Case == -1) || (Case == 2)) test_case_2(); if ((Case == -1) || (Case == 3)) test_case_3(); if ((Case == -1) || (Case == 4)) test_case_4(); }
	private:
	template <typename T> string print_array(const vector<T> &V) { ostringstream os; os << "{ "; for (typename vector<T>::const_iterator iter = V.begin(); iter != V.end(); ++iter) os << '\"' << *iter << "\","; os << " }"; return os.str(); }
	void verify_case(int Case, const long long &Expected, const long long &Received) { cerr << "Test Case #" << Case << "..."; if (Expected == Received) cerr << "PASSED" << endl; else { cerr << "FAILED" << endl; cerr << "\tExpected: \"" << Expected << '\"' << endl; cerr << "\tReceived: \"" << Received << '\"' << endl; } }
	void test_case_0() { long long Arg0 = 99999999999LL; long long Arg1 = 99999999977LL; long long Arg2 = 123456789LL; long long Arg3 = 999999999769999999LL; long long Arg4 = 999999999636543212LL; verify_case(0, Arg4, getNumberOfGoodBases(Arg0, Arg1, Arg2, Arg3)); }
	void test_case_1() { long long Arg0 = 362136128913LL; long long Arg1 = 9478364712LL; long long Arg2 = 44728LL; long long Arg3 = 7428164817412LL; long long Arg4 = 7428164763281LL; verify_case(1, Arg4, getNumberOfGoodBases(Arg0, Arg1, Arg2, Arg3)); }
	void test_case_2() { long long Arg0 = 5LL; long long Arg1 = 6LL; long long Arg2 = 2LL; long long Arg3 = 10LL; long long Arg4 = 8LL; verify_case(2, Arg4, getNumberOfGoodBases(Arg0, Arg1, Arg2, Arg3)); }
	void test_case_3() { long long Arg0 = 2662LL; long long Arg1 = 540LL; long long Arg2 = 2LL; long long Arg3 = 662LL; long long Arg4 = 639LL; verify_case(3, Arg4, getNumberOfGoodBases(Arg0, Arg1, Arg2, Arg3)); }
	void test_case_4() { long long Arg0 = 650720LL; long long Arg1 = 7032600LL; long long Arg2 = 2012LL; long long Arg3 = 2012540LL; long long Arg4 = 2010495LL; verify_case(4, Arg4, getNumberOfGoodBases(Arg0, Arg1, Arg2, Arg3)); }

// END CUT HERE

    };

// BEGIN CUT HERE
int main()
    {
    FractionInDifferentBases ___test;
    ___test.run_test(-1);
    }
// END CUT HERE
