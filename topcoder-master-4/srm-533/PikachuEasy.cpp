// BEGIN CUT HERE
// PROBLEM STATEMENT
// Pikachu is a well-known character in the Pokemon anime 
// series.
// Pikachu can speak, but only 3 syllables: "pi", "ka", and 
// "chu".
// Therefore Pikachu can only pronounce strings that can be 
// created as a concatenation of one or more syllables he can 
// pronounce. For example, he can pronounce the words 
// "pikapi" and "pikachu".
// 
// 
// 
// You are given a string word.
// Your task is to check whether Pikachu can pronounce the 
// string. If the string can be produced by concatenating 
// copies of the strings "pi", "ka", and "chu", return "YES" 
// (quotes for clarity). Otherwise, return "NO".
// 
// DEFINITION
// Class:PikachuEasy
// Method:check
// Parameters:string
// Returns:string
// Method signature:string check(string word)
// 
// 
// CONSTRAINTS
// -word will contain between 1 and 50 characters, inclusive.
// -Each character of word will be a lowercase letter 
// ('a'-'z').
// 
// 
// EXAMPLES
// 
// 0)
// "pikapi"
// 
// Returns: "YES"
// 
// "pikapi" = "pi" + "ka" + "pi", so Pikachu can say it.
// 
// 1)
// "pipikachu"
// 
// Returns: "YES"
// 
// This time we have "pipikachu" = "pi" + "pi" + "ka" + 
// "chu", so Pikachu can say it as well.
// 
// 2)
// "pikaqiu"
// 
// Returns: "NO"
// 
// Pikachu can't say "pikaqiu" since 'q' does not appear in 
// "pi", "ka", or "chu".
// 
// 3)
// "topcoder"
// 
// Returns: "NO"
// 
// 
// 
// 4)
// "piika"
// 
// Returns: "NO"
// 
// 
// 
// 5)
// "chupikachupipichu"
// 
// Returns: "YES"
// 
// 
// 
// 6)
// "pikapipachu"
// 
// Returns: "NO"
// 
// 
// 
// END CUT HERE
#line 89 "PikachuEasy.cpp"
#include <string>
#include <vector>
#include <iostream>
#include <sstream>
#include <algorithm>
#include <stdio.h>

using namespace std;

class PikachuEasy
    {
    public:
    string check(string w)
    {
        int i;
        int n = w.size();
        for (i = 0; i<n; i++) {
            switch (w[i]) {
                case 'p':
                    if (i < n-1 && w[++i]=='i')
                        ;
                    else
                        return "NO";
                    break;
                case 'k':
                    if (i < n-1 && w[++i]=='a')
                        ;
                    else
                        return "NO";
                    break;
                case 'c':
                    if (i < n-2 && w[++i]=='h' && w[++i]=='u')
                        ;
                    else
                        return "NO";
                    break;
                default:
                    return "NO";
            }
        }
        return "YES";
    }
    
// BEGIN CUT HERE
	public:
	void run_test(int Case) { if ((Case == -1) || (Case == 0)) test_case_0(); if ((Case == -1) || (Case == 1)) test_case_1(); if ((Case == -1) || (Case == 2)) test_case_2(); if ((Case == -1) || (Case == 3)) test_case_3(); if ((Case == -1) || (Case == 4)) test_case_4(); if ((Case == -1) || (Case == 5)) test_case_5(); if ((Case == -1) || (Case == 6)) test_case_6(); }
	private:
	template <typename T> string print_array(const vector<T> &V) { ostringstream os; os << "{ "; for (typename vector<T>::const_iterator iter = V.begin(); iter != V.end(); ++iter) os << '\"' << *iter << "\","; os << " }"; return os.str(); }
	void verify_case(int Case, const string &Expected, const string &Received) { cerr << "Test Case #" << Case << "..."; if (Expected == Received) cerr << "PASSED" << endl; else { cerr << "FAILED" << endl; cerr << "\tExpected: \"" << Expected << '\"' << endl; cerr << "\tReceived: \"" << Received << '\"' << endl; } }
	void test_case_0() { string Arg0 = "pikapi"; string Arg1 = "YES"; verify_case(0, Arg1, check(Arg0)); }
	void test_case_1() { string Arg0 = "pipikachu"; string Arg1 = "YES"; verify_case(1, Arg1, check(Arg0)); }
	void test_case_2() { string Arg0 = "pikaqiu"; string Arg1 = "NO"; verify_case(2, Arg1, check(Arg0)); }
	void test_case_3() { string Arg0 = "topcoder"; string Arg1 = "NO"; verify_case(3, Arg1, check(Arg0)); }
	void test_case_4() { string Arg0 = "piika"; string Arg1 = "NO"; verify_case(4, Arg1, check(Arg0)); }
	void test_case_5() { string Arg0 = "chupikachupipichu"; string Arg1 = "YES"; verify_case(5, Arg1, check(Arg0)); }
	void test_case_6() { string Arg0 = "pikapipachu"; string Arg1 = "NO"; verify_case(6, Arg1, check(Arg0)); }

// END CUT HERE

    };

// BEGIN CUT HERE
int main()
    {
    PikachuEasy ___test;
    ___test.run_test(-1);
    }
// END CUT HERE
