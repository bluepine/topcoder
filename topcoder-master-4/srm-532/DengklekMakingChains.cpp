// BEGIN CUT HERE
// PROBLEM STATEMENT
// Mr. Dengklek lives in the Kingdom of Ducks, where humans and ducks live 
// together in peace and harmony. 
// 
// 
// 
// Mr. Dengklek works as a chain maker. Today, he would like to make a beautiful 
// chain as a decoration for one of his lovely ducks. He will produce the chain 
// from leftovers he found in his workshop. Each of the leftovers is a chain 
// piece consisting of exactly 3 links. Each link is either clean or rusty. 
// Different clean links may have different degrees of beauty.
// 
// 
// 
// You are given a vector <string> chains describing the leftovers. Each element 
// of chains is a 3-character string describing one of the chain pieces. A rusty 
// link is represented by a period ('.'), whereas a clean link is represented by 
// a digit ('0'-'9'). The value of the digit in the clean link is the beauty of 
// the link. For example, chains = {".15", "7..", "532", "..3"} means that Mr. 
// Dengklek has 4 chain pieces, and only one of these ("532") has no rusty links.
// 
// 
// 
// All links have the same shape, which allows Mr. Dengklek to concatenate any 
// two chain pieces. However, the link shape is not symmetric, therefore he may 
// not reverse the chain pieces. E.g., in the above example he is able to produce 
// the chain "532.15" or the chain ".15..37..", but he cannot produce "5323..".
// 
// 
// 
// To produce the chain, Mr. Dengklek will follow these steps:
// 
// Concatenate all chain pieces in any order.
// Pick a contiguous sequence of links that contains no rusty links. Remove and 
// discard all the remaining links.
// 
// The beauty of the new chain is the total beauty of all the links picked in the 
// second step. Of course, Mr. Dengklek would like to create the most beautiful 
// chain possible.
// 
// 
// 
// Return the largest possible beauty a chain can have according to the above 
// rules.
// 
// DEFINITION
// Class:DengklekMakingChains
// Method:maxBeauty
// Parameters:vector <string>
// Returns:int
// Method signature:int maxBeauty(vector <string> chains)
// 
// 
// NOTES
// -Mr. Dengklek is not allowed to remove and discard individual links before 
// concatenating the chain pieces.
// -If all links in the input are rusty, Mr. Dengklek is forced to select an 
// empty sequence of links. The beauty of an empty sequence is 0.
// 
// 
// CONSTRAINTS
// -chains will contain between 1 and 50 elements, inclusive.
// -Each element of chains will contain exactly 3 characters.
// -Each character in each element of chains will be either a '.' or one of 
// '0'-'9'.
// 
// 
// EXAMPLES
// 
// 0)
// {".15", "7..", "402", "..3"}
// 
// Returns: 19
// 
// One possible solution:
// 
// 
// In the first step, concatenate the chain pieces in the order "..3", ".15", 
// "402", "7.." to obtain the chain "..3.154027..".
// In the second step, pick the subsequence "154027".
// 
// The beauty of the chain in this solution is 1+5+4+0+2+7 = 19.
// 
// 1)
// {"..1", "7..", "567", "24.", "8..", "234"}
// 
// Returns: 36
// 
// One possible solution is to concatenate the chain pieces in this order:
// 
// "..1", "234", "567", "8..", "24.", "7.." -> "..12345678..24.7..",
// 
// and then to pick the subsequence "12345678". Its beauty is 1+2+3+4+5+6+7+8 = 36.
// 
// 2)
// {"...", "..."}
// 
// Returns: 0
// 
// Mr. Dengklek cannot pick any links.
// 
// 3)
// {"16.", "9.8", ".24", "52.", "3.1", "532", "4.4", "111"}
// 
// Returns: 28
// 
// 
// 
// 4)
// {"..1", "3..", "2..", ".7."}
// 
// Returns: 7
// 
// 
// 
// 5)
// {"412", "..7", ".58", "7.8", "32.", "6..", "351", "3.9", "985", "...", ".46"}
// 
// Returns: 58
// 
// 
// 
// END CUT HERE
#line 126 "DengklekMakingChains.cpp"
#include <iostream>
#include <sstream>
#include <string>
#include <vector>
#include <algorithm>
#include <stdio.h>

using namespace std;

class DengklekMakingChains
    {
    public:
    int maxBeauty(vector <string> chains)
    {
        int i;
        int mhead = 0;
        int m2head = 0;
        int mtail = 0;
        int m2tail = 0;
        int mh=0, mt=0;
        int mid = 0;
        int m = 0;
        int ret;
        for (i = 0; i < chains.size(); i++) {
            char a, b, c;
            int h=0, t=0;
            a = chains[i][0];
            b = chains[i][1];
            c = chains[i][2];
            if (a != '.' && b != '.' && c != '.') {
                mid += a + b + c - 3*'0';
                continue;
            }
            if (c != '.') {
                h = c - '0';
                if (b != '.') {
                    h += b - '0';
                }
            }
            if (a != '.') {
                t = a - '0';
                if (b != '.')
                    t += b - '0';
            }
            if (h > mhead) {
                m2head = mhead;
                mhead = h;
                mh = i;
            } else if (h > m2head)
                m2head = h;

            if (t > mtail) {
                m2tail = mtail;
                mtail = t;
                mt = i;
            } else if (t > m2tail)
                m2tail = t;

            if (h > m)
                m = t;
            if (t > m)
                m = t;
            if (b!='.' && b-'0' > m)
                m = b-'0';
        }
        cout << mhead << mtail << m2head << m2tail << endl;
        if (mh != mt)
            ret = mhead + mtail + mid;
        else
            ret = mid + (mhead+m2tail>m2head+mtail? mhead+m2tail: m2head+mtail);
        return ret > m? ret: m;
    }
    
// BEGIN CUT HERE
	public:
	void run_test(int Case) { if ((Case == -1) || (Case == 0)) test_case_0(); if ((Case == -1) || (Case == 1)) test_case_1(); if ((Case == -1) || (Case == 2)) test_case_2(); if ((Case == -1) || (Case == 3)) test_case_3(); if ((Case == -1) || (Case == 4)) test_case_4(); if ((Case == -1) || (Case == 5)) test_case_5(); if ((Case == -1) || (Case == 6)) test_case_6(); if ((Case == -1) || (Case == 7)) test_case_7();
        if ((Case == -1) || (Case == 8)) test_case_8();
	}
	private:
	template <typename T> string print_array(const vector<T> &V) { ostringstream os; os << "{ "; for (typename vector<T>::const_iterator iter = V.begin(); iter != V.end(); ++iter) os << '\"' << *iter << "\","; os << " }"; return os.str(); }
	void verify_case(int Case, const int &Expected, const int &Received) { cerr << "Test Case #" << Case << "..."; if (Expected == Received) cerr << "PASSED" << endl; else { cerr << "FAILED" << endl; cerr << "\tExpected: \"" << Expected << '\"' << endl; cerr << "\tReceived: \"" << Received << '\"' << endl; } }
	void test_case_0() { string Arr0[] = {".15", "7..", "402", "..3"}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 19; verify_case(0, Arg1, maxBeauty(Arg0)); }
	void test_case_1() { string Arr0[] = {"..1", "7..", "567", "24.", "8..", "234"}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 36; verify_case(1, Arg1, maxBeauty(Arg0)); }
	void test_case_2() { string Arr0[] = {"...", "..."}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 0; verify_case(2, Arg1, maxBeauty(Arg0)); }
	void test_case_3() { string Arr0[] = {"16.", "9.8", ".24", "52.", "3.1", "532", "4.4", "111"}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 28; verify_case(3, Arg1, maxBeauty(Arg0)); }
	void test_case_4() { string Arr0[] = {"..1", "3..", "2..", ".7."}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 7; verify_case(4, Arg1, maxBeauty(Arg0)); }
	void test_case_5() { string Arr0[] = {"412", "..7", ".58", "7.8", "32.", "6..", "351", "3.9", "985", "...", ".46"}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 58; verify_case(5, Arg1, maxBeauty(Arg0)); }
	void test_case_6() { string Arr0[] = {"3.6"}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 6; verify_case(6, Arg1, maxBeauty(Arg0)); }
	void test_case_7() { string Arr0[] = {"24.", ".7.", "0..", "829", ".9.", "849", ".7.", "..4", "743", "8.2", "...", "9.7", ".85", "71.", ".34"}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 76; verify_case(7, Arg1, maxBeauty(Arg0)); }
	void test_case_8() { string Arr0[] = {"111", "8.8", "9.9"}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 20; verify_case(7, Arg1, maxBeauty(Arg0)); }

// END CUT HERE

    };

// BEGIN CUT HERE
int main()
    {
    DengklekMakingChains ___test;
    ___test.run_test(-1);
    }
// END CUT HERE
