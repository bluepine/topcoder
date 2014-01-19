// BEGIN CUT HERE
// PROBLEM STATEMENT
// Magical Girl Sayaka just learned about Conway's Game of Life. She is now 
// thinking about new rules for this game.
// In the Game of Life, an infinite plane is divided into a grid of unit square 
// cells. 
// At any moment, each cell is either alive or dead.
// Every second the state of each cell changes according to a fixed rule.
// In Sayaka's version of the game the following rule is used: 
// 
//  Consider any cell C. Look at the current states of the cell C and all four 
// cells that share a side with C. 
//  If at least one of these five cells are alive, cell C will be alive in the 
// next second. Otherwise, cell C will be dead in the next second. 
//  Note that each second the rule is applied on all cells at the same time.  
// Sayaka wants to know how many cells are alive after K seconds.
// You are given the int K and a vector <string> field that describes the initial 
// state of the plane. field describes only some rectangular area of the plane. 
// More precisely, character j of element i of field is 'o' if the cell in the i-
// th row of the j-th column of the rectangular area is alive, and it is '.' 
// otherwise. Cells which aren't described in field is initially all dead. 
// Return the number of alive cells after K seconds.
// 
// DEFINITION
// Class:NonXorLife
// Method:countAliveCells
// Parameters:vector <string>, int
// Returns:int
// Method signature:int countAliveCells(vector <string> field, int K)
// 
// 
// CONSTRAINTS
// -field will contain between 1 and 50 elements, inclusive.
// -Each elements of field will contain between 1 and 50 characters, inclusive.
// -All elements of field will contain the same number of characters.
// -Each character in each element of field will be either 'o' or '.'.
// -K will be between 1 and 1500, inclusive.
// 
// 
// EXAMPLES
// 
// 0)
// {"oo"
// ,"o."}
// 3
// 
// Returns: 36
// 
// The status after 3 seconds is below.
// ...oo...
// ..oooo..
// .oooooo.
// oooooooo
// ooooooo.
// .ooooo..
// ..ooo...
// ...o....
// 
// 1)
// {".."
// ,".."}
// 23
// 
// Returns: 0
// 
// All cells of the plane can be dead.
// 
// 2)
// {"o"}
// 1000
// 
// Returns: 2002001
// 
// 
// 
// 3)
// {"o.oo.ooo"
// ,"o.o.o.oo"
// ,"ooo.oooo"
// ,"o.o..o.o"
// ,"o.o..o.o"
// ,"o..oooo."
// ,"..o.o.oo"
// ,"oo.ooo.o"}
// 1234
// 
// Returns: 3082590
// 
// 
// 
// END CUT HERE
#line 93 "NonXorLife.cpp"
#include <iostream>
#include <sstream>
#include <string>
#include <vector>
#include <algorithm>
#include <stdio.h>
#include <string.h>
#include <queue>

using namespace std;

struct life {
    int x;
    int y;
    int t;
};

int dir[4][2] = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

bool map[3100][3100] = {{false}};
class NonXorLife
    {
    public:
    int countAliveCells(vector <string> field, int K)
    {
        int off = 3100 / 2;
        int i, j;
        queue<struct life> q;
        struct life life = {0};
        int ret;
        memset(map, 0, sizeof(map));
        for (i = 0; i < field.size(); i++) {
            for (j = 0; j < field[0].size(); j++) {
                int x = i + off;
                int y = j + off;
                int t = 0;
                if (field[i][j] != 'o')
                    continue;
                life.x = x, life.y = y, life.t = t;
                q.push(life);
                map[x][y] = true;
            }
        }
        while (!q.empty()) {
            struct life curr = q.front();
            q.pop();

            if (curr.t >= K)
                continue;

            for (i = 0; i < 4; i++) {
                int x = curr.x + dir[i][0];
                int y = curr.y + dir[i][1];
                if (map[x][y])
                    continue;
                map[x][y] = true;
                if (curr.t == K)
                    continue;
                life.x = x, life.y = y, life.t = curr.t + 1;
                q.push(life);
            }
        }
        ret = 0;
        for (i = 0; i < 3100; i++)
            for (j = 0; j < 3100; j++)
                if (map[i][j])
                    ret++;
        return ret;
    }
    
// BEGIN CUT HERE
	public:
	void run_test(int Case) { if ((Case == -1) || (Case == 0)) test_case_0(); if ((Case == -1) || (Case == 1)) test_case_1(); if ((Case == -1) || (Case == 2)) test_case_2(); if ((Case == -1) || (Case == 3)) test_case_3(); }
	private:
	template <typename T> string print_array(const vector<T> &V) { ostringstream os; os << "{ "; for (typename vector<T>::const_iterator iter = V.begin(); iter != V.end(); ++iter) os << '\"' << *iter << "\","; os << " }"; return os.str(); }
	void verify_case(int Case, const int &Expected, const int &Received) { cerr << "Test Case #" << Case << "..."; if (Expected == Received) cerr << "PASSED" << endl; else { cerr << "FAILED" << endl; cerr << "\tExpected: \"" << Expected << '\"' << endl; cerr << "\tReceived: \"" << Received << '\"' << endl; } }
	void test_case_0() { string Arr0[] = {"oo"
,"o."}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 3; int Arg2 = 36; verify_case(0, Arg2, countAliveCells(Arg0, Arg1)); }
	void test_case_1() { string Arr0[] = {".."
,".."}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 23; int Arg2 = 0; verify_case(1, Arg2, countAliveCells(Arg0, Arg1)); }
	void test_case_2() { string Arr0[] = {"o"}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 1000; int Arg2 = 2002001; verify_case(2, Arg2, countAliveCells(Arg0, Arg1)); }
	void test_case_3() { string Arr0[] = {"o.oo.ooo"
,"o.o.o.oo"
,"ooo.oooo"
,"o.o..o.o"
,"o.o..o.o"
,"o..oooo."
,"..o.o.oo"
,"oo.ooo.o"}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 1234; int Arg2 = 3082590; verify_case(3, Arg2, countAliveCells(Arg0, Arg1)); }

// END CUT HERE

    };

// BEGIN CUT HERE
int main()
    {
    NonXorLife ___test;
    ___test.run_test(-1);
    }
// END CUT HERE
