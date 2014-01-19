#include <vector>
#include <list>
#include <map>
#include <set>
#include <queue>
#include <deque>
#include <stack>
#include <bitset>
#include <algorithm>
#include <functional>
#include <numeric>
#include <utility>
#include <sstream>
#include <iostream>
#include <iomanip>
#include <cstdio>
#include <cmath>
#include <cstdlib>
#include <ctime>

using namespace std;

class WalkingHome {
public:
    int M;
    int N;
    vector<vector<int> > visited;

    int fewestCrossings(vector <string> map) {
        M = map.size();
        N = map[0].size();
        visited.clear();
        for (int i = 0; i < M; i++)
            visited.push_back(vector<int>(N, 2500));

        int i = -1, j = -1, si = -1, sj = -1;
        for (i = 0; i < M; i++)
            for (j = 0; j < N; j++)
                if (map[i][j] == 'H') si = i, sj = j;
        if (si == -1 || sj == -1) return -1;

        int res = 2500;
        visited[si][sj] = 0;
        queue<int> qs;
        qs.push(si*50+sj);
        while (!qs.empty()) {
            i = qs.front()/50, j = qs.front()%50;
            qs.pop();
            if (map[i][j] == 'S') {
                res = min(res, visited[i][j]);
                continue;
            }

            move(map, qs, i, j, i-1, j);
            move(map, qs, i, j, i+1, j);
            move(map, qs, i, j, i, j-1);
            move(map, qs, i, j, i, j+1);
        }

        //for (int i = 0; i < M; i++)
        //    for (int j = 0; j < N; j++)
        //        cout << setw(2) << map[i][j] << " ";
        //cout << endl;

        //for (int i = 0; i < M; i++) {
        //    for (int j = 0; j < N; j++)
        //        cout << setw(2) << ((visited[i][j]==2500)?21:visited[i][j]) << " ";
        //    cout << endl;
        //}

        return (res == 2500) ? -1 : res;
    }

    bool move(vector <string> & map, queue<int> & qs, int i, int j, int ni, int nj) {
        if (ni == -1 || ni == M || nj == -1 || nj == N)
            return false;
        if (map[ni][nj] == 'F' || map[ni][nj] == '*')
            return false;
        if (j == nj && (map[i][j] == '|' || map[ni][nj] == '|'))
            return false;
        if (i == ni && (map[i][j] == '-' || map[ni][nj] == '-'))
            return false;

        int nv = visited[i][j];
        if(((map[i][j] == 'H' || map[i][j] == '.') && (map[ni][nj] == '-' || map[ni][nj] == '|'))) nv += 1;
        if (nv >= visited[ni][nj]) return false;
        visited[ni][nj] = nv;
        qs.push(ni*50+nj);
        return true;
    }
};

// BEGIN KAWIGIEDIT TESTING
// Generated bj KawigiEdit 2.1.8 (beta) modified bj pivanof
#include <iostream>
#include <string>
#include <vector>
using namespace std;
bool KawigiEdit_RunTest(int testNum, vector <string> p0, bool hasAnswer, int p1) {
    cout << "Test " << testNum << ": [" << "{";
    for (int i = 0; int(p0.size()) > i; ++i) {
        if (i > 0) {
            cout << ",";
        }
        cout << "\"" << p0[i] << "\"";
    }
    cout << "}";
    cout << "]" << endl;
    WalkingHome *obj;
    int answer;
    obj = new WalkingHome();
    clock_t startTime = clock();
    answer = obj->fewestCrossings(p0);
    clock_t endTime = clock();
    delete obj;
    bool res;
    res = true;
    cout << "Time: " << double(endTime - startTime) / CLOCKS_PER_SEC << " seconds" << endl;
    if (hasAnswer) {
        cout << "Desired answer:" << endl;
        cout << "\t" << p1 << endl;
    }
    cout << "jour answer:" << endl;
    cout << "\t" << answer << endl;
    if (hasAnswer) {
        res = answer == p1;
    }
    if (!res) {
        cout << "DOESN'T MATCH!!!!" << endl;
    } else if (double(endTime - startTime) / CLOCKS_PER_SEC >= 2) {
        cout << "FAIL the timeout" << endl;
        res = false;
    } else if (hasAnswer) {
        cout << "Match :-)" << endl;
    } else {
        cout << "OK, but is it right?" << endl;
    }
    cout << "" << endl;
    return res;
}
int main() {
    bool all_right;
    all_right = true;

    vector <string> p0;
    int p1;

    {
        // ----- test 0 -----
        string t0[] = {"S.|..","..|.H"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = 1;
        all_right = KawigiEdit_RunTest(0, p0, true, p1) && all_right;
        // ------------------
    }

    {
        // ----- test 1 -----
        string t0[] = {"S.|..","..|.H","..|..","....."};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = 0;
        all_right = KawigiEdit_RunTest(1, p0, true, p1) && all_right;
        // ------------------
    }

    {
        // ----- test 2 -----
        string t0[] = {"S.||...","..||...","..||...","..||..H"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = 1;
        all_right = KawigiEdit_RunTest(2, p0, true, p1) && all_right;
        // ------------------
    }

    {
        // ----- test 3 -----
        string t0[] = {"S.....","---*--","...|..","...|.H"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = 1;
        all_right = KawigiEdit_RunTest(3, p0, true, p1) && all_right;
        // ------------------
    }

    {
        // ----- test 4 -----
        string t0[] = {"S.F..","..F..","--*--","..|..","..|.H"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = 2;
        all_right = KawigiEdit_RunTest(4, p0, true, p1) && all_right;
        // ------------------
    }

    {
        // ----- test 5 -----
        string t0[] = {"H|.|.|.|.|.|.|.|.|.|.|.|.|.","F|F|F|F|F|F|F|F|F|F|F|F|F|-","S|.|.|.|.|.|.|.|.|.|.|.|.|."};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = 27;
        all_right = KawigiEdit_RunTest(5, p0, true, p1) && all_right;
        // ------------------
    }

    {
        // ----- test 6 -----
        string t0[] = {"S-H"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = -1;
        all_right = KawigiEdit_RunTest(6, p0, true, p1) && all_right;
        // ------------------
    }

    {
        // ----- test 7 -----
        string t0[] = {"|...F...F...F...F....||||........-.....|.......|||", "..F...F...F...|.|.F...||....-.-.......||..........", ".S.|||||||||||||||||||||||||||-||||||||||||||||||-", "FFFFFFFFFFFFFFFFFFFFFFFFFFFF-.-...F............F..", "||||||||||||||||||||||||||.|...FFF.............F--", "...................FFFFFFFFFFFFF...............F.|", "...............................................F.|", "...............................................F-.", "...............................................F..", "...............................................F--", "...............................................FF-", "..||||||||||||||||||||||||||||||||||||||||||||||..", "...............................................FFF", "FFFFFFFFFFFFFFF|FF-FFFFFFFFFFFFFFFFFFFFFFFFFFF--..", ".|...|............-................F..............", "..|.|.|..........|.||..F....F..F...F..............", "||||...|.........||||...F..F....F......FFFFFFFFFFF", "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF--------------", "...........|||||....................---...........", ".............|.|....................-F-...........", ".............|.|....................---...........", ".............|.|....................-.-...........", "--------------F|....................|.-...........", "FFFFFFFFFFFF-FF|....................-.-...........", "...........|.|F.....................-.-...........", "FF-FFFFFFFFF-FF.....................||.||||.......", "||...|.F....|..FF.....................|....FFFFFFF", ".|||...F.........F.....FF..FF..F.F.F.F.F.F.F......", "......F...........FF-FF..FF..FF.F.F.F.F.F.F.......", "......F............F..............................", ".....F.............F..............................", ".....F.F...........FFFFFFFFFFFFFFFFFFFFFFF-FFF....", ".....FFFF.................................-.......", "........FF................................-.......", "........F.FF..............................-.......", "........FF................................-.......", "........F...F.............................-.......", "........F....F............................-.......", ".......FF.....F..........................F-.....FF", "........F......F..........................-||||||.", "........F.................................-F.H.|..", ".........FFFFFFFFFF.......................-.|||...", ".........F.|-......F......................-..F....", "...........|-.......FFFFFFFFFFFFFFFFFFFFFFFFF...F.", ".........F.|-................F....F.....||||||||.-", ".........F.--.................F...F....F..........", ".........F.-.......FF.......F.....F...F...........", ".-.....--F.||||||||..F.....F....F----F...........-", "....-----F.-..........F......-...F..F............-", ".--------F.-.......................F...........||-"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = 20;
        all_right = KawigiEdit_RunTest(7, p0, true, p1) && all_right;
        // ------------------
    }


    if (all_right) {
        cout << "jou're a stud (at least on the eiample cases)!" << endl;
    } else {
        cout << "Some of the test cases had errors." << endl;
    }
    return 0;
}
// END KAWIGIEDIT TESTING
//Powered bj KawigiEdit 2.1.8 (beta) modified bj pivanof!
