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

map<int, map<int, int > > graph;
int car_num;
int park_num;
int lower;
int upper;

class Parking {
public:
	int minTime(vector <string> park) {
        graph.clear();
        lower = 50;
        upper = 0;
        build(park);
        //map<int, map<int, int > >::iterator it1 = graph.begin();
        //while (it1 != graph.end()) {
            //cout << it1->first << ": ";
            //map<int, int >::iterator it2 = it1->second.begin();
            //while (it2 != it1->second.end()) {
                //cout << it2->first << ":" << it2->second << " "; 
                //it2++;
            //}
            //cout << endl;
            //it1++;
        //}
        
        //cout << "lower:" << lower << endl;
        //cout << "upper:" << upper << endl;

        if (car_num == 0) return 0;
        if (car_num > park_num || !match(upper)) return -1;
        while (lower+1 < upper) {
            int mid = (lower+upper)/2;
            if (match(mid)) upper = mid;
            else lower = mid;
        }
        
        return (match(lower))?lower:upper;
    }

    bool match(int dist) {
        if (dist == 0) return false;
        vector<int> car_match(car_num, -1);
        vector<int> park_match(park_num, -1);
        for (int src = 0; src < car_num; src++) {
            if(!bfs(src, dist, car_match, park_match)) {
                return false;
            }
        }
        return true;
    } 

    bool bfs(int src, int dist, vector<int> & car_match, vector<int> & park_match) {
        vector<int> from(car_num, -1);
        queue<int> q;
        q.push(src);
        from[src] = src;
        bool found = false;
        int where = -1, match = -1; 
        while (!q.empty() && !found) {
            where = q.front(); q.pop();
            //cout << "where: " << where << endl;
            for (map<int, int>::iterator it = graph[where].begin(); it != graph[where].end(); it++) {
                match = it->first;
                //cout << "match: " << match << " dist:" << it->second << endl;
                if (it->second > dist) continue;
                int next = park_match[match];
                if (where == next) continue;
                if (next == -1) {
                    found = true;
                    break;
                }   
                if (from[next] == -1) {
                    q.push(next);
                    from[next] = where;
                    //cout << "push: " << next << endl;
                }
            }   
        }

        //cout << "from:" << endl;
        //for (int i = 0; i < (int)graph.size(); i++)
        //    cout << from[i] << " ";
        //cout << endl;

        if (!found) return false;
        while (from[where] != where) {
            int tmp = car_match[where];
            car_match[where] = match;
            park_match[match] = where;
            where = from[where];
            match = tmp;
        }

        car_match[where] = match;
        park_match[match] = where;

        //cout << "require match:" << endl;
        //for (int i = 0; i < (int)graph.size(); i++)
        //    cout << car_match[i] << " ";
        //cout << endl;
        //cout << "class match:" << endl;
        //for (int j = 33; j <= 126; j++ )
        //    cout << park_match[j] << " ";
        //cout << endl;

        return true;
    }


    void build(vector<string> & park) {
        int board[100][100];
        int M = park.size();
        int N = park[0].size();
        map<int, int> cars;
        map<int, int> parks;
        car_num = 0;
        park_num = 0;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (park[i][j] == 'C')
                    cars[i*100+j] = car_num++;
                if (park[i][j] == 'P')
                    parks[i*100+j] = park_num++;
            }
        }
        //cout << "car_num:" << car_num << endl;
        //cout << "park_num:" << park_num << endl;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (park[i][j] == 'C') {
                    for (int m = 0; m < M; m++)
                        for (int n = 0; n < N; n++)
                            board[m][n] = -1;
                    board[i][j] = 0;
                    queue<int> q;
                    int src = i*100+j;
                    q.push(src);
                    while (!q.empty()) {
                        int where = q.front(); q.pop();
                        int row = where/100;
                        int col = where%100;
                        if (park[row][col] == 'P') {
                            graph[cars[src]][parks[where]] = board[row][col];
                            lower = min(lower, board[row][col]);
                            upper = max(upper, board[row][col]);
                        }
                        if (row > 0 && park[row-1][col] != 'X' && board[row-1][col] == -1) {
                            board[row-1][col] = board[row][col] + 1;
                            q.push((row-1)*100+col);
                        }
                        if (row < M-1 && park[row+1][col] != 'X' && board[row+1][col] == -1) {
                            board[row+1][col] = board[row][col] + 1;
                            q.push((row+1)*100+col);
                        }
                        if (col > 0 && park[row][col-1] != 'X' && board[row][col-1] == -1) {
                            board[row][col-1] = board[row][col] + 1;
                            q.push(row*100+col-1);
                        }
                        if (col < N-1 && park[row][col+1] != 'X' && board[row][col+1] == -1) {
                            board[row][col+1] = board[row][col] + 1;
                            q.push(row*100+col+1);
                        }
                    }
                }
            }
        }
    }
};


// BEGIN KAWIGIEDIT TESTING
// Generated by KawigiEdit 2.1.8 (beta) modified by pivanof
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
    Parking *obj;
    int answer;
    obj = new Parking();
    clock_t startTime = clock();
    answer = obj->minTime(p0);
    clock_t endTime = clock();
    delete obj;
    bool res;
    res = true;
    cout << "Time: " << double(endTime - startTime) / CLOCKS_PER_SEC << " seconds" << endl;
    if (hasAnswer) {
        cout << "Desired answer:" << endl;
        cout << "\t" << p1 << endl;
    }
    cout << "Your answer:" << endl;
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

    //{
        //// ----- test 0 -----
        //string t0[] = {"C.....P","C.....P","C.....P"};
        //p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        //p1 = 6;
        //all_right = KawigiEdit_RunTest(0, p0, true, p1) && all_right;
        //// ------------------
    //}

    //{
    //// ----- test 1 -----
    //string t0[] = {"C.X.....","..X..X..","..X..X..",".....X.P"};
    //p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
    //p1 = 16;
    //all_right = KawigiEdit_RunTest(1, p0, true, p1) && all_right;
    //// ------------------
    //}

    //{
    //// ----- test 2 -----
    //string t0[] = {"XXXXXXXXXXX","X......XPPX","XC...P.XPPX","X......X..X","X....C....X","XXXXXXXXXXX"};
    //p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
    //p1 = 5;
    //all_right = KawigiEdit_RunTest(2, p0, true, p1) && all_right;
    //// ------------------
    //}

    //{
    //// ----- test 3 -----
    //string t0[] = {".C.","...","C.C","X.X","PPP"};
    //p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
    //p1 = 4;
    //all_right = KawigiEdit_RunTest(3, p0, true, p1) && all_right;
    //// ------------------
    //}

    //{
    //// ----- test 4 -----
    //string t0[] = {"CCCCC",".....","PXPXP"};
    //p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
    //p1 = -1;
    //all_right = KawigiEdit_RunTest(4, p0, true, p1) && all_right;
    //// ------------------
    //}

    //{
    //// ----- test 5 -----
    //string t0[] = {"..X..","C.X.P","..X.."};
    //p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
    //p1 = -1;
    //all_right = KawigiEdit_RunTest(5, p0, true, p1) && all_right;
    //// ------------------
    //}

    {
    // ----- test 5 -----
    string t0[] = {"XX.XX.X.X.XX.XX.XXXXC...X.XX.XXXX...XX.XX.XXP...XX", ".XXXX..X....XP.X.CX.PXX.C....XXXX.XXX.X.XX.XX.XXXC", "PXX..XC.X.XC.X.XX..XX.XX..XX..XCX.......XC...X.X..", "XPXXX.......X..P.X.XX.P.X.X.X.CX.X....XX.....X.X..", "X..X..X..XX..X.XC.CX.X.XXXX..X.CX.XXX.XX.X.PXX.PP.", ".P.C.X..X.P..XX....PX....PXC..XXX......X..X.XX.X..", "....XXXXXCC..XX..X..X.P.X..XX....X...X.X...XXXX.X.", "XXXX.PP.XPX.XCX..XX.X.P.X.XXX.....XXX.X..P.XXXX.XX", "X..X.X.XXXX...PXX.PX.PX..XXX.C.XXX.X..XXXXXX.C..XX", ".CX.X.P.XXXX....X..XP.XX.CXC.X...X.X..X..XX.X....X", "C.PX.X...XX.XCX..X..X.X.XXXX...X..X.XXX.X.XX..XX.X", ".X..X....X...X.XX....XX.P.X.XPX..X.X......XX.....X", "...XXX...X.PXXXXX..XX.PPP.X.XX.X.X...XXX.XXX.X..X.", "C.X..XX....XP.P.XC..X.X....P..X..X...XXXXXX.X.XXXX", "...PX..XXXXX..X..X.X..X.XP.XXX..CXP.XX.X....X.X...", "CX.XX.XP.CX..XXXP.X.XX.XXXPX.X.X..XCX..XX...XX..XX", "XXXX.X...X...X.....XX.X.XX...X.X.C.X..XX..X.XPCXX.", "P..XXXX..XX.C..X.X...XX.X.X.P.P.XC.XXX.XC..PX..X.X", "XXX..X.XXX....XX...CXX....X...X.XXX...XPX...X.C.X.", ".X.XX.X..X.....X..X..XX.X.....X.XX.X.X..X.XXX..X..", "XXX.....X...X..XXPXX..XX.....PXXXXX.........XX.CX.", ".......X......X.CX...XX.XX..XXXX..X..CXXXX.P..X..C", "XX..XX.XX..X..XX.XX..X..XX...PP..X.X.X..XXP..XXCX.", ".XX.XXX..X.XXX..XXX..X.X..X.XX.X..X...XXX.PXXX.X.X", ".XXXC..X......XX.X...XXXX.P.XX...XX..XXX.XX..X.X.X", "..X...C..XXX.....XX....XX....XXXXX.X..PXC.X.X.XX..", "....X..XX.PCX...PXXC.X..X..X.X.X..X.X.XX..X.X.XX..", ".CXX...XX...X..XP.X..XX..XX...XXC...XX.X.X.X...X..", "C..XX....X..X..PX.XXX.X..C.XXC.X.XX.XXX..X..XPX.XX", "XXX...X...X.XXC.X.....X....XX.X..X..XPX.XXX.....X.", "XX..XXX.X..X.CX..X..XXPXXX......XX.XPP...XX..C...X", "..XX.X..X.XXXX.X.....XX.XXX....XXXXXXCX.X.X....X..", "XXX.X.XXXXP.C..PXX..XXXXXPXXXXCXXXCX.X..XXX..P..P.", "X.XXXC..P.XX..XX..XXXX..XXX..C..X..P.XX..XX....PXX", "XP.X.X..XXCX..X.X.X.......XXXX.X...X.X.XXXXX...X.X", "...XX...PX....XX..X.X..XX....CXXX..XXX.X....XXX.X.", ".X....X...X.X.XC.X..XC....XPXX.P.X.XX.XX......XXX.", "XPX..X..XX..C..XXXX...CXP.X..XX.XXX..C..X.X.X.....", "X..XXXXX..X.X..XXCX.XX.XXX.XX..XP.X....XCXX.X.P.X.", "..X...XXXC.X.....PX.X.XXP..XX.X.XP..X.XXX..XXXXX..", "X.C.X.CX....C.X..PX...X..XX.PXP..XXC..X......P.XXP", ".X.XX.XX.X.XPXX......XX.X.C....CXX..X..X...X..XXX.", "XXXXX.XX.X.X...PXXX.....X...XX.X...C...P..XXX.X..X", ".XXX.X.......X..XXX..X.XXXXX.X..XXX.X.CCXXX......X", ".P....X.X..X.XXX.X.X...X.CXX.....X...XPX....X.X..X", ".XXC.X.XXXP.XC.P.X.X....X...CXX.X..XX.........X...", "XXXX.X..X.XXCCX....X.X..XXXX..XX..XXC...XXC..XX...", ".XXX.X.X..XX....XXX.X.X....X...X.XXX..X.XXXXXX..X.", "XXX.PXX..C...X...XX.X...XX.XX..X.X.PX.........C...", "..X...X..X...X..X....XCX.XP..X.X......X.XC.....X.X"};
    p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
    p1 = -1;
    all_right = KawigiEdit_RunTest(5, p0, true, p1) && all_right;
    // ------------------
    }

    if (all_right) {
        cout << "You're a stud (at least on the example cases)!" << endl;
    } else {
        cout << "Some of the test cases had errors." << endl;
    }
    return 0;
}
// END KAWIGIEDIT TESTING
//Powered by KawigiEdit 2.1.8 (beta) modified by pivanof!
