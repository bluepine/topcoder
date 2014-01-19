/*
// BEGIN CUT HERE
// PROBLEM STATEMENT
// You are a wizard.
You are facing N fairies, numbered 0 through N-1.
Your goal is to defeat all of them.
You can only attack one fairy at a time.
Moreover, you must fight the fairies in order: you can only attack fairy X+1 after you defeat fairy X.
On the other hand, all fairies that have not been defeated yet will attack you all the time.



Each fairy has two characteristics: her damage per second (dps) and her amount of hit points.
Your damage per second is 1.
That is, you are able to reduce an opponent's hit points by 1 each second.
In other words, if a fairy has H hit points, it takes you H seconds to defeat her.



You are given two vector <int>s, each of length N: dps and hp.
For each i, dps[i] is the damage per second of fairy i, and hp[i] is her initial amount of hit points.
We assume that your number of hit points is sufficiently large to avoid defeat when fighting the fairies.
Compute and return the total number of hit points you'll lose during the fight.
In other words, return the total amount of damage the attacking fairies will deal.

DEFINITION
Class:GreatFairyWar
Method:minHP
Parameters:vector <int>, vector <int>
Returns:int
Method signature:int minHP(vector <int> dps, vector <int> hp)


NOTES
-Damage per second (dps) of a person P is the rate at which P can deal damage to their opponents.


CONSTRAINTS
-dps will contain between 1 and 30 elements, inclusive.
-dps and hp will contain the same number of elements.
-Each element in dps will be between 1 and 30, inclusive.
-Each element in hp will be between 1 and 30, inclusive.


EXAMPLES

0)
{1,2}
{3,4}

Returns: 17

It will take you 3 seconds to defeat fairy 0 and then 4 seconds to defeat fairy 1.
During the first 3 seconds both fairies attack you, dealing 1+2 = 3 damage each second.
During the next 4 seconds fairy 1 continues to attack you, dealing 2 damage each second.
The total number of hit points you lose is 3*(1+2) + 4*2 = 9 + 8 = 17.

1)
{1,1,1,1,1,1,1,1,1,1}
{1,1,1,1,1,1,1,1,1,1}

Returns: 55

The answer is 10+9+...+3+2+1 = 55.

2)
{20,12,10,10,23,10}
{5,7,7,5,7,7}

Returns: 1767



3)
{5,7,7,5,7,7}
{20,12,10,10,23,10}

Returns: 1998



4)
{30,2,7,4,7,8,21,14,19,12}
{2,27,18,19,14,8,25,13,21,30}

Returns: 11029



5)
{1}
{1}

Returns: 1



// END CUT HERE
#line 100 "GreatFairyWar.cpp"
*/
#include <vector>
#include <queue>
#include <deque>
#include <map>
#include <iostream>
#include <cstring>
#include <string>
#include <math.h>
#include <cstdio>
#include <cstdlib>
#include <algorithm>

using namespace std;

#define s(x) scanf("%d",&x)
#define sll(x) scanf("%lld",&x)
#define sf(x) scanf("%lf",&x)
#define ss(x) scanf("%s",&x)

#define f(i,a,b) for(int i=a;i<b;i++)
#define fr(i,n)  f(i,0,n)

typedef long long ll;



class GreatFairyWar {
public:
  int minHP(vector <int> dps, vector <int> hp) {
    int n = dps.size();
    int res = 0;
    fr (i, n){
      f (j, i, n){
	res += dps[j] * hp[i];
      }
    }
    return res;
  }
};
