// BEGIN CUT HERE
// PROBLEM STATEMENT
//
// Consider the following function:
// f(X) = Waai + X + Akari + X + Daisuki
// Here, X is a string and + denotes string concatenation.
// Waai, Akari and Daisuki are constant non-empty strings.
//
//
//
// You are given a string S.
// Compute and return the number of different tuples (Waai,Akari,Daisuki,X) such
// that f(X) equals S.
//
//
// DEFINITION
// Class:AkariDaisukiDiv2
// Method:countTuples
// Parameters:string
// Returns:int
// Method signature:int countTuples(string S)
//
//
// NOTES
// -Two tuples (A,B,C,D) and (E,F,G,H) are considered equal if and only if A=E,
// B=F, C=G, and D=H.
//
//
// CONSTRAINTS
// -S will contain between 1 and 50 characters, inclusive.
// -Each character of S will be a lowercase letter ('a'-'z').
//
//
// EXAMPLES
//
// 0)
// "topcoderdivtwo"
//
// Returns: 2
//
// The possible tuples of (Waai, Akari, Daisuki, X) are the following ones:
//
// ("t", "pc", "derdivtwo", "o")
// ("topco", "er", "ivtwo", "d")
//
// ("topc", "derdivtw", "", "o") is not allowed since Daisuki is empty.
//
// 1)
// "foxciel"
//
// Returns: 0
//
// The answer can be zero.
//
//
// 2)
// "magicalgirl"
//
// Returns: 4
//
//
//
// 3)
// "waaiusushioakariusushiodaisuki"
//
// Returns: 75
//
//
//
// 4)
// "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//
// Returns: 8924
//
//
//
// END CUT HERE
#line 79 "AkariDaisukiDiv2.cpp"
#include <iostream>
#include <sstream>
#include <string>
#include <vector>
#include <algorithm>
#include <stdio.h>
#include <string.h>

using namespace std;

int str_count(const string &text ,const string &str)
{
        int count=0;
        int pos=text.find(str);
        while(pos!=string::npos)
        {
                count++;
                pos=text.find(str,pos+1);
        }
        return count;
}

class AkariDaisukiDiv2
    {
    public:
    int countTuples(string S)
        {
            int n = S.size();
            if (n <= 4)
                return 0;

            int maxsubstr = (n - 3) / 2;
            int j;
            int i;
            int count = 0;
            for (i = 1; i < n; i++) {
                for (j = 1; j <= maxsubstr; j++) {
                    int next = i + j + 1;
                    if (next >= n - 1)
                        continue;
                    count += str_count(S.substr(next, n - 1 - next), S.substr(i, j));
                }
            }
            return count;
        }

// BEGIN CUT HERE
	public:
	void run_test(int Case) { if ((Case == -1) || (Case == 0)) test_case_0(); if ((Case == -1) || (Case == 1)) test_case_1(); if ((Case == -1) || (Case == 2)) test_case_2(); if ((Case == -1) || (Case == 3)) test_case_3(); if ((Case == -1) || (Case == 4)) test_case_4(); }
	private:
	template <typename T> string print_array(const vector<T> &V) { ostringstream os; os << "{ "; for (typename vector<T>::const_iterator iter = V.begin(); iter != V.end(); ++iter) os << '\"' << *iter << "\","; os << " }"; return os.str(); }
	void verify_case(int Case, const int &Expected, const int &Received) { cerr << "Test Case #" << Case << "..."; if (Expected == Received) cerr << "PASSED" << endl; else { cerr << "FAILED" << endl; cerr << "\tExpected: \"" << Expected << '\"' << endl; cerr << "\tReceived: \"" << Received << '\"' << endl; } }
	void test_case_0() { string Arg0 = "topcoderdivtwo"; int Arg1 = 2; verify_case(0, Arg1, countTuples(Arg0)); }
	void test_case_1() { string Arg0 = "foxciel"; int Arg1 = 0; verify_case(1, Arg1, countTuples(Arg0)); }
	void test_case_2() { string Arg0 = "magicalgirl"; int Arg1 = 4; verify_case(2, Arg1, countTuples(Arg0)); }
	void test_case_3() { string Arg0 = "waaiusushioakariusushiodaisuki"; int Arg1 = 75; verify_case(3, Arg1, countTuples(Arg0)); }
	void test_case_4() { string Arg0 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"; int Arg1 = 8924; verify_case(4, Arg1, countTuples(Arg0)); }

// END CUT HERE

    };

// BEGIN CUT HERE
int main()
    {
    AkariDaisukiDiv2 ___test;
    ___test.run_test(-1);
    }
// END CUT HERE
