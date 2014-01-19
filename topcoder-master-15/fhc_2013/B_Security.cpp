﻿
//
// B. Security
//

/*
問題

文字列の一部を?で置き換える関数fと、文字列をシャッフルする関数gがある
文字列keyは、l文字毎に区切られている
k1=f(key)、k2=f(g(key))が与えられる
kとして可能性のある文字列のうち辞書順最小のものを求める

*/

#include <algorithm>
#include <string>
#include <iostream>
#include <sstream>
#include <vector>

using namespace std;

typedef vector<int> IntVec;
typedef vector<string> StrVec;

IntVec G[200];
int matched[200];
bool used[200];

void add_edge(int u, int v) {
	G[u].push_back(v);
	G[v].push_back(u);
}

bool dfs(int v) {
	used[v] = true;
	int i;
	for (i = 0; i < (int)G[v].size(); ++i) {
		int u = G[v][i];
		int w = matched[u];
		if (w < 0 || !used[w] && dfs(w)) {
			matched[v] = u;
			matched[u] = v;
			return true;
		}
	}
	return false;
}

bool match(StrVec a, StrVec b)
{
	int i, j, k;

	for (i = 0; i < 200; ++i) {
		G[i].clear();
	}
	memset(matched, -1, sizeof(matched));

	int m = (int)a.size();
	int length = (int)a[0].length();
	for (i = 0; i < m; ++i) {
		for (j = 0; j < m; ++j) {
			for (k = 0; k < length; ++k) {
				char p = a[i][k], q = b[j][k];
				if (p != q && p != '?' && q != '?') {
					break;
				}
			}
			if (k >= length) {
				add_edge(i, 100+j);
			}
		}
	}

	for (i = 0; i < m; ++i) {
		if (matched[i] < 0) {
			memset(used, 0, sizeof(used));
			if (!dfs(i)) {
				return false;
			}
		}
	}

	return true;
}

string solve(StrVec a, StrVec b)
{
	int i, j, k;
	int m = (int)a.size();
	int length = (int)a[0].length();
	for (i = 0; i < m; ++i) {
		for (j = 0; j < length; ++j) {
			if (a[i][j] == '?') {
				bool f;
				for (k = 'a'; k <= 'f'; ++k) {
					a[i][j] = k;
					f = match(a, b);
					if (f) {
						break;
					}
				}
				if (!f) {
					return "";
				}
			}
		}
	}

	if (!match(a, b)) {
		return "";
	}

	string ans;
	for (i = 0; i < m; ++i) {
		ans += a[i];
	}
	return ans;
}

StrVec split(int m, string s)
{
	StrVec v;
	int length = (int)s.length() / m;
	int i;
	for (i = 0; i < m; ++i) {
		v.push_back(s.substr(i * length, length));
	}
	return v;
}

int main(int argc, char *argv[])
{
	string s;
	getline(cin, s);
	int T = atoi(s.c_str());
	for (int t = 0; t < T; ++t) {
		getline(cin, s);
		int m = atoi(s.c_str());
		string a, b;
		getline(cin, a);
		getline(cin, b);
		string ans = solve(split(m, a), split(m, b));
		if (ans.empty()) {
			ans = "IMPOSSIBLE";
		}
		cout << "Case #" << (t+1) << ": " << ans << endl;
	}

	return 0;
}

