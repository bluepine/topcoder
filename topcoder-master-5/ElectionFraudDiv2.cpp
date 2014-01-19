/*
// BEGIN CUT HERE
// PROBLEM STATEMENT
// In a normal election, one expects the percentages received by each of the candidates to sum to exactly 100 percent.  There are two ways this might not be the case: if the election is fraudulent, or if the reported percentages are rounded.
In a recent election, the number of voters was known to be exactly 10000.  Assuming the election was run fairly, each voter voted for exactly one candidate.  The percentage of the vote received by each candidate was rounded to the nearest whole number before being reported.  Percentages lying halfway between two consecutive whole numbers were rounded up.
The ministry of voting is looking for a proof of election fraud.  You'll be given a vector <int> percentages, giving the reported percentage of the vote that went to each candidate.  Return a string containing "YES" if the election is definitely fraudulent, otherwise return "NO" (quotes for clarity only).  That is, return "YES" if it could not be the case that each of the 10000 voters voted for exactly one candidate, and "NO" otherwise.

DEFINITION
Class:ElectionFraudDiv2
Method:IsFraudulent
Parameters:vector <int>
Returns:string
Method signature:string IsFraudulent(vector <int> percentages)


CONSTRAINTS
-percentages will contain between 1 and 50 elements, inclusive.
-Each element of percentages will be between 0 and 100, inclusive.


EXAMPLES

0)
{100}

Returns: "NO"

If there's only one candidate, that candidate will receive 100% of the votes in a fair election.

1)
{34, 34, 34}

Returns: "YES"

Even accounting for rounding, these numbers are too high.

2)
{12, 12, 12, 12, 12, 12, 12, 12}

Returns: "YES"

These numbers are too low.

3)
{13, 13, 13, 13, 13, 13, 13, 13}

Returns: "NO"

Each candidate could have received exactly 1250 votes.

4)
{0, 1, 100}

Returns: "NO"

The only valid possibility is that the candidates received 0, 50, and 9950 votes, respectively.

5)
{3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7, 9, 3, 2, 3, 8}

Returns: "NO"



// END CUT HERE
#line 67 "ElectionFraudDiv2.cpp"
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



class ElectionFraudDiv2 {
public:
  string IsFraudulent(vector <int> percentages) {
    int elec_tot = 10000;
    ll bot = 0;
    ll top = 0;
    fr(i, percentages.size()){
      if (percentages[i] != 0){
	bot += percentages[i] * 100 - 50;
      }
      top += percentages[i] * 100 + 49;
    }
    if (top >= 10000 && bot <= 10000){
      return "NO";
    } else {
      return "YES";
    }
  }

};
