// BEGIN CUT HERE
// PROBLEM STATEMENT
// Magical Girl Lein is observing ants.
// There are N ants. At first, they have integer coordinates
// in the Cartesian plane. More precisely, ant i starts at
// the point (x[i], y[i]).
// All ants move at the same speed. Each ant moves in one of
// the four basic directions. (I.e., either parallel to the x
// axis or parallel to the y axis.)
// When 2 or more ants meet at the same time, these ants
// disappear.
// You are given two vector <int>s x and y, containing N
// elements each,
// and a string direction, containing N characters.
// Character i of direction encodes the direction in which
// ant i is going:
//
//  'N' means north (y coordinate increases),
//  'E' means east (x coordinate increases),
//  'S' means south (y coordinate decreases),
//  and 'W' means west (x coordinate decreases).
//
// Return the number of ants that still exist after the last
// meeting occurs.
//
// DEFINITION
// Class:AntsMeet
// Method:countAnts
// Parameters:vector <int>, vector <int>, string
// Returns:int
// Method signature:int countAnts(vector <int> x, vector
// <int> y, string direction)
//
//
// CONSTRAINTS
// -x will contain between 1 and 50 elements, inclusive.
// -y will contain the same number of elements as x.
// -The number of characters in direction will be the same as
// the number of elements in x.
// -Each element of x and y will be between -1000 and 1000,
// inclusive.
// -No pair of points in the input will be equal.
// -Each character of direction will be one of 'N', 'E', 'W'
// and 'S'.
//
//
// EXAMPLES
//
// 0)
// {0,10,20,30}
// {0,10,20,30}
// "NWNE"
//
// Returns: 2
//
// The ants that start at (0,0) and (10,10) will meet at (0,
// 10) at time 10.
// The remaining two ants will never meet.
//
// 1)
// {-10,0,0,10}
// {0,-10,10,0}
// "NEWS"
//
// Returns: 0
//
// More than two ants can meet at the same time.
//
// 2)
// {-1,-1,-1,0,0,0,1,1,1}
// {-1,0,1,-1,0,1,-1,0,1}
// "ESEWNNEWW"
//
// Returns: 4
//
// The ants that start at (-1,-1) and (0,-1) will meet at
// (-0.5,-1).
// The ants that start at (-1,1), (0,0) and (1,1) will meet
// at (0,1).
// Thus, 4 ants will remain after all meetings.
// Note that ants that start at (-1,0) and (0,-1) won't meet
// at (-1,1) because one of them will disappear before
// reaching the meeting point.
//
// 3)
// {4,7,6,2,6,5,7,7,8,4,7,8,8,8,5,4,8,9,1,5,9,3,4,0,0,1,0,7,2,6,9,6,3,0,5,5,1,2,0,4,9,7,7,1,8,1,9,2,7,3}

// {2,3,0,6,8,4,9,0,5,0,2,4,3,8,1,5,0,7,3,7,0,9,8,1,9,4,7,8,1,1,6,6,6,2,8,5,1,9,0,1,1,1,7,0,2,5,4,7,5,3}

// "SSNWSWSENSWSESWEWSWSENWNNNESWSWSWWSSWEEWWNWWWNWENN"
//
// Returns: 25
//
//
//
// 4)
// {478,-664,759,434,-405,513,565,-396,311,-174,56,993,251,
// -341,993,-112,242,129,383,513,-78,-341,-148,129,423
// ,493,434,-405,478,-148,929,251,56,242,929,-78,423,
// -664,802,251,759,383,-112,-591,-591,-248,660,660,735,493}
// {-186,98,948,795,289,-678,948,-170,-195,290,-354,-424,289,
// -157,-166,150,706,-678,684,-294,-234,36,36,-294,-216
// ,-234,427,945,265,-157,265,715,275,715,-186,337,798,
// -170,427,706,754,961,286,-216,798,286,961,684,-424,337}
// "WNSNNSSWWWEENWESNSWSWSEWWEWEWWWNWESNSSNNSNNWWWNESE"
//
// Returns: 44
//
//
//
// END CUT HERE
#line 113 "AntsMeet.cpp"
#include <string>
#include <vector>
#include <iostream>
#include <sstream>
#include <algorithm>
#include <stdio.h>

using namespace std;

struct collision {
    int a1;
    int a2;
    double t;
};

struct dir {
    int dx;
    int dy;
};

int collision_cmp(struct collision c1, struct collision c2)
{
    return c1.t <= c2.t;
}

struct dir char_to_dir(char d)
{
    struct dir ret;
    switch (d) {
        case 'N': ret.dx = 0; ret.dy = 1; break;
        case 'S': ret.dx = 0; ret.dy = -1; break;
        case 'E': ret.dx = 1; ret.dy = 0; break;
        case 'W': ret.dx = -1; ret.dy = 0; break;
    }
    return ret;
}

double meet_time(int x1, int y1, int x2, int y2, char d1i, char d2i)
{
    if (d1i == d2i)
        return 0.0;

    struct dir d1 = char_to_dir(d1i);
    struct dir d2 = char_to_dir(d2i);
    double t;

    if (x1 == x2) {
        if (!d1.dy || !d2.dy)
            return 0.0;
        t = (double)(y1 - y2) / (double)(d2.dy - d1.dy);
        if (t > 0)
            return t;
        else
            return 0.0;
    } else if (y1 == y2) {
        if (!d1.dx || !d2.dx)
            return 0.0;
        t = (double)(x1 - x2) / (double)(d2.dx - d1.dx);
        if (t > 0)
            return t;
        else
            return 0.0;
    } else {
        int t1, t2;
        if ((!d1.dx && !d2.dx) || (!d1.dy && !d2.dy))
            return 0.0;
        t1 = (double)(x1 - x2) / (double)(d2.dx - d1.dx);
        t2 = (double)(y1 - y2) / (double)(d2.dy - d1.dy);
        if (t1 > 0 && t1 == t2)
            return t1;
        else
            return 0.0;
    }
}

class AntsMeet
    {
    public:
    int countAnts(vector <int> x, vector <int> y, string direction)
        {
            int N = x.size();
            int i;
            int j;
            int ret;
            vector<struct collision> collisions;
            vector<bool> live(N, true);
            int cn;

            for (i = 0; i < N; i++) {
                for (j = i + 1; j < N; j++) {
                    double t = meet_time(x[i], y[i], x[j], y[j], direction[i], direction[j]);
                    if (!t)
                        continue;
                    struct collision c = {i, j, t};
                    collisions.push_back(c);
                }
            }

            sort(collisions.begin(), collisions.end(), collision_cmp);

            for (i = 0; i < collisions.size(); i++) {
                //printf("collision: %d and %d, at time %f\n", collisions[i].a1, collisions[i].a2, collisions[i].t);
            }

            cn = collisions.size();
            for (i = 0; i < cn; i++) {
                double t = collisions[i].t;
                vector<int> to_die;

                while (i < cn && collisions[i].t == t) {
                    int a1 = collisions[i].a1;
                    int a2 = collisions[i].a2;
                    if (live[a1] && live[a2]) {
                        to_die.push_back(a1);
                        to_die.push_back(a2);
                    }
                    i++;
                }
                i--;

                for (j = 0; j < to_die.size(); j++)
                    live[to_die[j]] = false;
            }

            ret = 0;
            for (i = 0; i < N; i++) {
                if (live[i])
                    ret++;
            }

            return ret;
        }

// BEGIN CUT HERE
	public:
	void run_test(int Case) { if ((Case == -1) || (Case == 0)) test_case_0(); if ((Case == -1) || (Case == 1)) test_case_1(); if ((Case == -1) || (Case == 2)) test_case_2(); if ((Case == -1) || (Case == 3)) test_case_3(); if ((Case == -1) || (Case == 4)) test_case_4(); }
	private:
	template <typename T> string print_array(const vector<T> &V) { ostringstream os; os << "{ "; for (typename vector<T>::const_iterator iter = V.begin(); iter != V.end(); ++iter) os << '\"' << *iter << "\","; os << " }"; return os.str(); }
	void verify_case(int Case, const int &Expected, const int &Received) { cerr << "Test Case #" << Case << "..."; if (Expected == Received) cerr << "PASSED" << endl; else { cerr << "FAILED" << endl; cerr << "\tExpected: \"" << Expected << '\"' << endl; cerr << "\tReceived: \"" << Received << '\"' << endl; } }
	void test_case_0() { int Arr0[] = {0,10,20,30}; vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arr1[] = {0,10,20,30}; vector <int> Arg1(Arr1, Arr1 + (sizeof(Arr1) / sizeof(Arr1[0]))); string Arg2 = "NWNE"; int Arg3 = 2; verify_case(0, Arg3, countAnts(Arg0, Arg1, Arg2)); }
	void test_case_1() { int Arr0[] = {-10,0,0,10}; vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arr1[] = {0,-10,10,0}; vector <int> Arg1(Arr1, Arr1 + (sizeof(Arr1) / sizeof(Arr1[0]))); string Arg2 = "NEWS"; int Arg3 = 0; verify_case(1, Arg3, countAnts(Arg0, Arg1, Arg2)); }
	void test_case_2() { int Arr0[] = {-1,-1,-1,0,0,0,1,1,1}; vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arr1[] = {-1,0,1,-1,0,1,-1,0,1}; vector <int> Arg1(Arr1, Arr1 + (sizeof(Arr1) / sizeof(Arr1[0]))); string Arg2 = "ESEWNNEWW"; int Arg3 = 4; verify_case(2, Arg3, countAnts(Arg0, Arg1, Arg2)); }
	void test_case_3() { int Arr0[] = {4,7,6,2,6,5,7,7,8,4,7,8,8,8,5,4,8,9,1,5,9,3,4,0,0,1,0,7,2,6,9,6,3,0,5,5,1,2,0,4,9,7,7,1,8,1,9,2,7,3}; vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arr1[] = {2,3,0,6,8,4,9,0,5,0,2,4,3,8,1,5,0,7,3,7,0,9,8,1,9,4,7,8,1,1,6,6,6,2,8,5,1,9,0,1,1,1,7,0,2,5,4,7,5,3}; vector <int> Arg1(Arr1, Arr1 + (sizeof(Arr1) / sizeof(Arr1[0]))); string Arg2 = "SSNWSWSENSWSESWEWSWSENWNNNESWSWSWWSSWEEWWNWWWNWENN" ; int Arg3 = 25; verify_case(3, Arg3, countAnts(Arg0, Arg1, Arg2)); }
	void test_case_4() { int Arr0[] = {478,-664,759,434,-405,513,565,-396,311,-174,56,993,251,-341,993,-112,242,129,383,513,-78,-341,-148,129,423
,493,434,-405,478,-148,929,251,56,242,929,-78,423,-664,802,251,759,383,-112,-591,-591,-248,660,660,735,493}; vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arr1[] = {-186,98,948,795,289,-678,948,-170,-195,290,-354,-424,289,-157,-166,150,706,-678,684,-294,-234,36,36,-294,-216
,-234,427,945,265,-157,265,715,275,715,-186,337,798,-170,427,706,754,961,286,-216,798,286,961,684,-424,337}; vector <int> Arg1(Arr1, Arr1 + (sizeof(Arr1) / sizeof(Arr1[0]))); string Arg2 = "WNSNNSSWWWEENWESNSWSWSEWWEWEWWWNWESNSSNNSNNWWWNESE"; int Arg3 = 44; verify_case(4, Arg3, countAnts(Arg0, Arg1, Arg2)); }

// END CUT HERE

    };

// BEGIN CUT HERE
int main()
    {
    AntsMeet ___test;
    ___test.run_test(-1);
    }
// END CUT HERE
