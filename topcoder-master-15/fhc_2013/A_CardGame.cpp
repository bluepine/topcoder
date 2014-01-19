﻿
//
// A. Card Game
//

/*
問題

数値が書かれたN枚のカードがある
K枚配り、最大の数が手の強さとなる
平均の手の強さを求めるため、手の強さの合計値を求める

*/

#include <algorithm>
#include <string>
#include <iostream>
#include <sstream>
#include <vector>

using namespace std;

#define COMBSZ 10001
#define MOD 1000000007LL

typedef long long LL;
typedef vector<int> IntVec;

int main(int argc, char *argv[])
{
	// generate combination table
	static LL C[COMBSZ][COMBSZ];
	memset(C, 0, sizeof(C));
	int i, j;
	for (i = 0; i < COMBSZ; ++i) {
		C[i][0] = 1;
	}
	for (i = 1; i < COMBSZ; ++i) {
		for (j = 1; j < COMBSZ; ++j) {
			C[i][j] = (C[i-1][j-1] + C[i-1][j]) % MOD;
		}
	}

	string s;
	getline(cin, s);
	int T = atoi(s.c_str());
	for (int t = 0; t < T; ++t) {
		static LL v[10000];
		int N=0, K=0;
		getline(cin, s);
		stringstream sa(s);
		sa >> N >> K;
		getline(cin, s);
		stringstream sb(s);
		int i, j, k;
		for (i = 0; i < N; ++i) {
			sb >> v[i];
		}
		sort(v, v+N);

		LL ans = 0;
		while (N >= K) {
			ans = (ans + v[N-1] * C[N-1][K-1]) % MOD;
			--N;
		}

		cout << "Case #" << (t+1) << ": " << ans << endl;
	}

	return 0;
}

