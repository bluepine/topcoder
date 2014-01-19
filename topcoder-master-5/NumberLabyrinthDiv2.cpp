#include <vector>
#include <list>
#include <map>
#include <set>
// #include <deque>
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
#include <queue>

using namespace std;

struct t{
	int x;
	int y;
	int c;
	int k;
};

int graph[50][50];
int dp[50][50][51];

class NumberLabyrinthDiv2 {
public:
	int getMinimumNumberOfMoves(vector <string>, int, int, int, int, int);
};

int NumberLabyrinthDiv2::getMinimumNumberOfMoves(vector<string> b, int r1, int c1, int r2, int c2, int K) {
	queue<t> bfs;
	t start;
	start.x=r1;start.y=c1;start.c=0;start.k=K;
	int n=b.size();int m=b[0].size();
	
	for(int i=0;i<n;i++){
		for(int j=0;j<m;j++){
			if(b[i][j]=='.') graph[i][j]=-1;
			else graph[i][j]=(int)b[i][j]-48;	
		}
	}
	
	for(int i=0;i<n;i++){
		for(int j=0;j<m;j++){
			for(int k=0;k<=K;k++){
				dp[i][j][k]=-1;
			
			}
		}	
	}
	
	bfs.push(start);
	dp[r1][c1][K]=0;
	while(!bfs.empty() && bfs.front().c<=n*m){
		int x,y,c,k;x=bfs.front().x;y=bfs.front().y;c=bfs.front().c;k=bfs.front().k;
		if(x==r2 && y==c2) return c;
		if(graph[x][y]==-1){
			if(k>0){
				for(int i=1;i<=9;i++){
					if(x-i>=0) {t temp;temp.x=x-i;temp.y=y;temp.c=c+1;temp.k=k-1;if(dp[x-i][y][k-1]==-1){bfs.push(temp);dp[x-i][y][k-1]=0;}}
					if(x+i<n) {t temp;temp.x=x+i;temp.y=y;temp.c=c+1;temp.k=k-1;if(dp[x+i][y][k-1]==-1){bfs.push(temp);dp[x+i][y][k-1]=0;}}
					if(y-i>=0) {t temp;temp.x=x;temp.y=y-i;temp.c=c+1;temp.k=k-1;if(dp[x][y-i][k-1]==-1){bfs.push(temp);dp[x][y-i][k-1]=0;}}
					if(y+i<m) {t temp;temp.x=x;temp.y=y+i;temp.c=c+1;temp.k=k-1;if(dp[x][y+i][k-1]==-1){bfs.push(temp);dp[x][y+i][k-1]=0;}}
				}
			}	
		}
		else {
			int i=graph[x][y];
			if(x-i>=0) {t temp;temp.x=x-i;temp.y=y;temp.c=c+1;temp.k=k;if(dp[x-i][y][k]==-1){bfs.push(temp);dp[x-i][y][k]=0;}}
			if(x+i<n) {t temp;temp.x=x+i;temp.y=y;temp.c=c+1;temp.k=k;if(dp[x+i][y][k]==-1){bfs.push(temp);dp[x+i][y][k]=0;}}
			if(y-i>=0) {t temp;temp.x=x;temp.y=y-i;temp.c=c+1;temp.k=k;if(dp[x][y-i][k]==-1){bfs.push(temp);dp[x][y-i][k]=0;}}
			if(y+i<m) {t temp;temp.x=x;temp.y=y+i;temp.c=c+1;temp.k=k;if(dp[x][y+i][k]==-1){bfs.push(temp);dp[x][y+i][k]=0;}}
		}
		bfs.pop();
	}
	return -1;
}


//Powered by [KawigiEdit] 2.0!
