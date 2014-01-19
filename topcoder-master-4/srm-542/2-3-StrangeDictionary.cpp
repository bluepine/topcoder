// BEGIN CUT HERE
// PROBLEM STATEMENT
// A standard way of ordering words in a dictionary involves comparison of two
// words character by character, from left to right, until some two characters
// disagree. Jim thinks it's pretty boring, so he introduced a more complicated
// and less predictable scheme.
//
// Given is a vector <string> words that contains N distinct words and each
// element is a single word. The length of each word is the same -- exactly L
// characters. To order words according to his scheme, Jim first generates a
// random permutation p[0], p[1], ..., p[L-1] of integers between 0 and L-1,
// inclusive. This permutation is generated only once and then considered to be
// fixed for the rest of the procedure. Using the permutation p, Jim can compare
// any two words as follows:
//
//
// // returns -1, if A<B, 0, if A=B, and 1, if A>B
// function Compare(Word A, Word B):
// 	for i = 0, 1, ..., L-1:
// 		a := character at position p[i] (0-based) in A
// 		b := character at position p[i] (0-based) in B
// 		if (a < b), return -1
// 		if (b < a), return 1
//
// 	return 0
//
//
// Let Sorted be the list of the given words ordered according to Compare
// operator defined above. In other words, Compare(Sorted[i], Sorted[j]) = -1,
// for any i, j, 0 <= i < j < N. Let pos[i] be the 0-based position of words[i]
// within Sorted.
//
// Obviously, the value pos[i] depends on the choice of the random permutation p.
// Assume that p is chosen uniformly at random. Return a vector <double> that
// contains N elements. Element i of the return value must be the expected value
// of pos[i].
//
// DEFINITION
// Class:StrangeDictionary
// Method:getExpectedPositions
// Parameters:vector <string>
// Returns:vector <double>
// Method signature:vector <double> getExpectedPositions(vector <string> words)
//
//
// NOTES
// -Each element of the return value must have an absolute or relative error of
// less than 1e-9.
//
//
// CONSTRAINTS
// -words will contain between 1 and 50 elements, inclusive.
// -Each element of words will contain between 1 and 50 characters, inclusive.
// -All elements of words will contain the same number of characters.
// -Each character in each element of words will be a lowercase letter ('a'-'z').
// -All elements of words will be distinct.
//
//
// EXAMPLES
//
// 0)
// {"hardesttestever"}
//
// Returns: {0.0 }
//
// One word will always be at position 0 regardless of the permutation p.
//
// 1)
// {"ab", "ba"}
//
// Returns: {0.5, 0.5 }
//
// If p = {0, 1}, then "ab" < "ba". If p = {1, 0}, then "ba" < "ab".
//
// 2)
// {"aza", "aab", "bba"}
//
// Returns: {1.0, 0.8333333333333333, 1.1666666666666665 }
//
//
// p		Sorted
// {0,1,2}		aab, aza, bba
// {0,2,1}		aza, aab, bba
// {1,0,2}		aab, bba, aza
// {1,2,0}		aab, bba, aza
// {2,0,1}		aza, bba, aab
// {2,1,0}		bba, aza, aab
//
//
// 3)
// {"abababab", "babababa", "acacacac", "cacacaca", "bcbcbcbc", "cbcbcbcb"}
//
// Returns: {1.0, 1.0, 2.5, 2.5, 4.0, 4.0 }
//
//
//
// END CUT HERE
#line 99 "StrangeDictionary.cpp"
#include <iostream>
#include <sstream>
#include <string>
#include <vector>
#include <algorithm>
#include <stdio.h>

using namespace std;

class StrangeDictionary
    {
    public:
    vector <double> getExpectedPositions(vector <string> words)
    {
        vector<double> ret(words.size(), 0);
        int i, j;
        int n = words[0].size();
        for (i = 0; i < words.size(); i++)
            for (j = i+1; j < words.size(); j++) {
                int k;
                int pi = 0, pj = 0, q = 0;
                for (k = 0; k < n; k++) {
                    if (words[i][k] == words[j][k])
                        continue;
                    q++;
                    if (words[i][k] > words[j][k])
                        pi++;
                    else
                        pj++;
                }
                ret[i] += (double)pi / q;
                ret[j] += (double)pj / q;
            }
        return ret;
    }

// BEGIN CUT HERE
	public:
	void run_test(int Case) { if ((Case == -1) || (Case == 0)) test_case_0(); if ((Case == -1) || (Case == 1)) test_case_1(); if ((Case == -1) || (Case == 2)) test_case_2(); if ((Case == -1) || (Case == 3)) test_case_3(); }
	private:
	template <typename T> string print_array(const vector<T> &V) { ostringstream os; os << "{ "; for (typename vector<T>::const_iterator iter = V.begin(); iter != V.end(); ++iter) os << '\"' << *iter << "\","; os << " }"; return os.str(); }
	void verify_case(int Case, const vector <double> &Expected, const vector <double> &Received) { cerr << "Test Case #" << Case << "..."; if (Expected == Received) cerr << "PASSED" << endl; else { cerr << "FAILED" << endl; cerr << "\tExpected: " << print_array(Expected) << endl; cerr << "\tReceived: " << print_array(Received) << endl; } }
	void test_case_0() { string Arr0[] = {"hardesttestever"}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); double Arr1[] = {0.0 }; vector <double> Arg1(Arr1, Arr1 + (sizeof(Arr1) / sizeof(Arr1[0]))); verify_case(0, Arg1, getExpectedPositions(Arg0)); }
	void test_case_1() { string Arr0[] = {"ab", "ba"}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); double Arr1[] = {0.5, 0.5 }; vector <double> Arg1(Arr1, Arr1 + (sizeof(Arr1) / sizeof(Arr1[0]))); verify_case(1, Arg1, getExpectedPositions(Arg0)); }
	void test_case_2() { string Arr0[] = {"aza", "aab", "bba"}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); double Arr1[] = {1.0, 0.8333333333333333, 1.1666666666666665 }; vector <double> Arg1(Arr1, Arr1 + (sizeof(Arr1) / sizeof(Arr1[0]))); verify_case(2, Arg1, getExpectedPositions(Arg0)); }
	void test_case_3() { string Arr0[] = {"abababab", "babababa", "acacacac", "cacacaca", "bcbcbcbc", "cbcbcbcb"}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); double Arr1[] = {1.0, 1.0, 2.5, 2.5, 4.0, 4.0 }; vector <double> Arg1(Arr1, Arr1 + (sizeof(Arr1) / sizeof(Arr1[0]))); verify_case(3, Arg1, getExpectedPositions(Arg0)); }

// END CUT HERE

    };

// BEGIN CUT HERE
int main()
    {
    StrangeDictionary ___test;
    ___test.run_test(-1);
    }
// END CUT HERE
