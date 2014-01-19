// BEGIN CUT HERE
// PROBLEM STATEMENT
// Our lab has just run into a dilemma. All our data about
// the lab's platypus population seems to have gotten mixed
// up. Each platypus in the lab is either a male adult, a
// female adult, or a child. The children are divided into
// several sibling groups. We know this division exactly.
// What we are missing are the parent-child relations. In
// order to reconstruct these, we conducted some genetic
// tests. We are going to do what some other lab did 4 years
// ago and hire you to make a program to help us fix the
// situation. You will be given three vector <string>s
// containing the results of these tests and the description
// of all sibling groups. The three vector <string>s are:
//
// femaleCompatibility: The j-th character of the i-th
// element of femaleCompatibility is 'Y' if the i-th female
// adult is genetically compatible with the j-th child (i.e.,
// the i-th female adult may be the mother of the j-th
// child). Otherwise, it is 'N'.
// maleCompatibility: The j-th character of the i-th element
// of maleCompatibility is 'Y' if the i-th male adult is
// genetically compatible with the j-th child (i.e., the i-th
// male adult may be the father of the j-th child).
// Otherwise, it is 'N'.
// siblingGroups: The j-th character of the i-th element of
// siblingGroups is 'Y' if the j-th child belongs to the i-th
// sibling group. Otherwise, it is 'N'. Each child belongs
// into exactly one sibling group. Two children are siblings
// if and only if they belong to the same sibling group.
//
// The lab has defined a valid family as a set containing one
// female adult (mother), one male adult (father), and one
// sibling group. The mother and the father must be
// genetically compatible with each of the children in the
// sibling group. Return the maximum size of a family that
// follows these conditions or 0 if no such family exists.
//
//
// DEFINITION
// Class:PlatypusPaternity
// Method:maxFamily
// Parameters:vector <string>, vector <string>, vector <string>
// Returns:int
// Method signature:int maxFamily(vector <string>
// femaleCompatibility, vector <string> maleCompatibility,
// vector <string> siblingGroups)
//
//
// CONSTRAINTS
// -The input will represent data for F females, M males, C
// children and S sibling groups, with F, M, C and S each
// being between 1 and 50, inclusive.
// -femaleCompatibility will contain F elements.
// -maleCompatibility will contain M elements.
// -siblingGroups will contain S elements.
// -Each element of femaleCompatibility, maleCompatibility
// and siblingGroups will contain C characters.
// -Each character of femaleCompatibility, maleCompatibility
// and siblingGroups will be 'Y' or 'N' (quotes for clarity).
// -For each child j there will be exactly one sibling group
// i such that siblingGroups[i][j] is 'Y'.
// -For each sibling group i, there will exist at least one
// child that belongs to that group.
//
//
// EXAMPLES
//
// 0)
// {"YYYY", "YYYY"}
// {"NNYN", "YYYN"}
// {"YYYN", "NNNY"}
//
// Returns: 5
//
// One possible family is formed by the female adult 0 (0-
// indexed), the male adult 1, and the sibling group 0
// (containing children 0, 1, and 2).
// The size of this family is: 1 female + 1 male + 3 children
// = 5 platypuses.
// There is no family of size more than 5.
// Another valid family of size 5 contains the female adult 1
// instead of the female adult 0.
//
// 1)
// {"NNYYY"}
// {"YYNNN"}
// {"YYNNN", "NNYYY"}
//
// Returns: 0
//
// Each adult is compatible with a different sibling group.
// There is no couple that is compatible with the same
// sibling group
//
// 2)
// {"YYYYYYYYN"}
// {"YYYYYYYYY"}
// {"YNYNYNYNY",
//  "NNNYNYNNN",
//  "NYNNNNNYN"}
//
// Returns: 4
//
// The largest sibling group has size 5. However, there is no
// valid family that contains this sibling group, as the only
// available female adult is not compatible with one of the
// children in this group.
//
// 3)
// {"YY"}
// {"YY"}
// {"YN", "NY"}
//
// Returns: 3
//
//
//
// 4)
// {"YYNNYYNNYYNN",
//  "YNYNYNYNYNYN",
//  "YYYNNNYYYNNN"}
// {"NYYNNYYNNYYN",
//  "YYNYYYNYYYNY",
//  "NNNNNNYYYYYY"}
// {"NYNNNYNNNNNN",
//  "NNNNNNNNYNNN",
//  "NNYNNNNNNNYN",
//  "YNNNNNNYNNNN",
//  "NNNNNNNNNYNY",
//  "NNNYYNYNNNNN"}
//
// Returns: 4
//
//
//
// END CUT HERE
#line 139 "PlatypusPaternity.cpp"
#include <string>
#include <vector>
#include <iostream>
#include <sstream>
#include <algorithm>
#include <stdio.h>

using namespace std;

int compatible(string parent, string group)
{
    int i;
    int compat = true;
    for (i = 0; i < group.size(); i++) {
        if (group[i] == 'Y' && parent[i] != 'Y') {
            compat = false;
            break;
        }
    }
    return compat;
}

int solve(vector <string> females, vector <string> males, string group)
{
    int i;
    int groupsize;

    for (i = 0; i < females.size() && !compatible(females[i], group); i++)
        ;
    if (i >= females.size())
        return 0;

    for (i = 0; i < males.size() && !compatible(males[i], group); i++)
        ;
    if (i >= males.size())
        return 0;

    groupsize = 0;
    for (i = 0; i < group.size(); i++)
        if (group[i] == 'Y')
            groupsize++;
    return groupsize + 2;
}

class PlatypusPaternity
    {
    public:
    int maxFamily(vector <string> femaleCompatibility, vector <string> maleCompatibility, vector <string> siblingGroups)
        {
            int i;
            int count = 0;
            for (i = 0; i < siblingGroups.size(); i++ ){
                int x = solve(femaleCompatibility, maleCompatibility, siblingGroups[i]);
                if (x > count)
                    count = x;
            }
            return count;
        }

// BEGIN CUT HERE
	public:
	void run_test(int Case) { if ((Case == -1) || (Case == 0)) test_case_0(); if ((Case == -1) || (Case == 1)) test_case_1(); if ((Case == -1) || (Case == 2)) test_case_2(); if ((Case == -1) || (Case == 3)) test_case_3(); if ((Case == -1) || (Case == 4)) test_case_4(); }
	private:
	template <typename T> string print_array(const vector<T> &V) { ostringstream os; os << "{ "; for (typename vector<T>::const_iterator iter = V.begin(); iter != V.end(); ++iter) os << '\"' << *iter << "\","; os << " }"; return os.str(); }
	void verify_case(int Case, const int &Expected, const int &Received) { cerr << "Test Case #" << Case << "..."; if (Expected == Received) cerr << "PASSED" << endl; else { cerr << "FAILED" << endl; cerr << "\tExpected: \"" << Expected << '\"' << endl; cerr << "\tReceived: \"" << Received << '\"' << endl; } }
	void test_case_0() { string Arr0[] = {"YYYY", "YYYY"}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); string Arr1[] = {"NNYN", "YYYN"}; vector <string> Arg1(Arr1, Arr1 + (sizeof(Arr1) / sizeof(Arr1[0]))); string Arr2[] = {"YYYN", "NNNY"}; vector <string> Arg2(Arr2, Arr2 + (sizeof(Arr2) / sizeof(Arr2[0]))); int Arg3 = 5; verify_case(0, Arg3, maxFamily(Arg0, Arg1, Arg2)); }
	void test_case_1() { string Arr0[] = {"NNYYY"}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); string Arr1[] = {"YYNNN"}; vector <string> Arg1(Arr1, Arr1 + (sizeof(Arr1) / sizeof(Arr1[0]))); string Arr2[] = {"YYNNN", "NNYYY"}; vector <string> Arg2(Arr2, Arr2 + (sizeof(Arr2) / sizeof(Arr2[0]))); int Arg3 = 0; verify_case(1, Arg3, maxFamily(Arg0, Arg1, Arg2)); }
	void test_case_2() { string Arr0[] = {"YYYYYYYYN"}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); string Arr1[] = {"YYYYYYYYY"}; vector <string> Arg1(Arr1, Arr1 + (sizeof(Arr1) / sizeof(Arr1[0]))); string Arr2[] = {"YNYNYNYNY",
 "NNNYNYNNN",
 "NYNNNNNYN"}; vector <string> Arg2(Arr2, Arr2 + (sizeof(Arr2) / sizeof(Arr2[0]))); int Arg3 = 4; verify_case(2, Arg3, maxFamily(Arg0, Arg1, Arg2)); }
	void test_case_3() { string Arr0[] = {"YY"}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); string Arr1[] = {"YY"}; vector <string> Arg1(Arr1, Arr1 + (sizeof(Arr1) / sizeof(Arr1[0]))); string Arr2[] = {"YN", "NY"}; vector <string> Arg2(Arr2, Arr2 + (sizeof(Arr2) / sizeof(Arr2[0]))); int Arg3 = 3; verify_case(3, Arg3, maxFamily(Arg0, Arg1, Arg2)); }
	void test_case_4() { string Arr0[] = {"YYNNYYNNYYNN",
 "YNYNYNYNYNYN",
 "YYYNNNYYYNNN"}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); string Arr1[] = {"NYYNNYYNNYYN",
 "YYNYYYNYYYNY",
 "NNNNNNYYYYYY"}; vector <string> Arg1(Arr1, Arr1 + (sizeof(Arr1) / sizeof(Arr1[0]))); string Arr2[] = {"NYNNNYNNNNNN",
 "NNNNNNNNYNNN",
 "NNYNNNNNNNYN",
 "YNNNNNNYNNNN",
 "NNNNNNNNNYNY",
 "NNNYYNYNNNNN"}; vector <string> Arg2(Arr2, Arr2 + (sizeof(Arr2) / sizeof(Arr2[0]))); int Arg3 = 4; verify_case(4, Arg3, maxFamily(Arg0, Arg1, Arg2)); }

// END CUT HERE

    };

// BEGIN CUT HERE
int main()
    {
    PlatypusPaternity ___test;
    ___test.run_test(-1);
    }
// END CUT HERE
