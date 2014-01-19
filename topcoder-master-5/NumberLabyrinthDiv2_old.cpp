#include <vector>
#include <list>
#include <map>
#include <set>
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

int dp[50][50];
int graph[50][50];

int sx,sy,dx,dy;
int n,m;
class NumberLabyrinthDiv2 {

public:
	int func(int x,int y,int k){
		if(1){
			if(x==dx && y==dy) dp[x][y]=0;
			else if(graph[x][y]==0) dp[x][y]=2501;
			else if(graph[x][y]==-1 && k<=0) dp[x][y]=2501;
			else{
				if(graph[x][y]==-1){
					int temp = 2505;
					for(int i=1;i<10;i++){
						if(x-i>=0)	temp=min(temp,func(x-i,y,k-1));
						if(x+i<n) 	temp=min(temp,func(x+i,y,k-1));
						if(y-i>=0)	temp=min(temp,func(x,y-i,k-1));
						if(y+i<m)   temp=min(temp,func(x,y+i,k-1));
					}
					dp[x][y]=temp<2500?(1+temp):2501;
				}
				else{
					int i=graph[x][y];int temp=2505;
					if(x-i>=0)	temp=min(temp,func(x-i,y,k));
					if(x+i<n) 	temp=min(temp,func(x+i,y,k));
					if(y-i>=0)	temp=min(temp,func(x,y-i,k));
					if(y+i<m)   temp=min(temp,func(x,y+i,k));
					dp[x][y]=temp<2500?(1+temp):2501;
				}
			}
		}
		cout << x << " "<<y<<" "<<" "<<k<<" "<<dp[x][y]<<endl;
		return dp[x][y];		
	}
	int getMinimumNumberOfMoves(vector <string>, int, int, int, int, int);
};

int NumberLabyrinthDiv2::getMinimumNumberOfMoves(vector <string> b, int r1, int c1, int r2, int c2, int k) {
	n=b.size();m=b[0].size();
	for(int i=0;i<n;i++){
		for(int j=0;j<m;j++){
			dp[i][j]=-2;
			if(b[i][j]=='.') graph[i][j]=-1;
			else graph[i][j]=(int)b[i][j]-48;		
			cout<<graph[i][j]<<" ";
		}
		cout<<endl;
	}
	for(int i=0;i<n;i++){
		for(int j=0;j<m;j++){
		
		}
	}
	sx=r1;sy=c1;dx=r2;dy=c2;
	cout<<sx<<" "<<sy<<" "<<dx<<" "<<dy<<" "<<k<<endl;
	int res=func(sx,sy,k);
	return (res>=2501?-1:res);	
	
}



//Powered by [KawigiEdit] 2.0!
