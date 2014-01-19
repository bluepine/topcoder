/*

Qualification Round 2011
Problem A. Bot Trust


Problem

Blue and Orange are friendly robots. An evil computer mastermind has
locked them up in separate hallways to test them, and then possibly
give them cake.

Each hallway contains 100 buttons labeled with the positive integers
{1, 2, ..., 100}. Button k is always k meters from the start of the hallway,
and the robots both begin at button 1. Over the period of one second,
a robot can walk one meter in either direction, or it can press the button
at its position. To complete the test, the robots need to push a certain
sequence of buttons in a certain order. Both robots know the full sequence
in advance. How fast can they complete it?

For example, let's consider the following button sequence:

   O 2, B 1, B 2, O 4

Here, O 2 means button 2 in Orange's hallway, B 1 means button 1 in Blue's
hallway, and so on. The robots can push this sequence of buttons in
6 seconds using the strategy shown below:

Time | Orange           | Blue
-----+------------------+-----------------
  1  | Move to button 2 | Stay at button 1
  2  | Push button 2    | Stay at button 1
  3  | Move to button 3 | Push button 1
  4  | Move to button 4 | Move to button 2
  5  | Stay at button 4 | Push button 2
  6  | Push button 4    | Stay at button 2
Note that Blue has to wait until Orange has completely finished pushing O 2
before it can start pushing B 1.


Input

The first line of the input gives the number of test cases, T.
T test cases follow.

Each test case consists of a single line beginning with a positive integer N,
representing the number of buttons that need to be pressed. This is
followed by N terms of the form "Ri Pi" where Ri is a robot color
(always 'O' or 'B'), and Pi is a button position.


Output

For each test case, output one line containing "Case #x: y",
where x is the case number (starting from 1) and y is the minimum number
of seconds required for the robots to push the given buttons, in order.


Limits

1 <= Pi <= 100 for all i.


Small dataset

1 <= T <= 20.
1 <= N <= 10.


Large dataset

1 <= T <= 100.
1 <= N <= 100.


Sample

Input 

3
4 O 2 B 1 B 2 O 4
3 O 5 O 8 B 100
2 B 2 B 1


Output 

Case #1: 6
Case #2: 100
Case #3: 4

*/


#include <stdio.h>
#include <string.h>
#include <list>
#include <vector>

using namespace std;

typedef __int64 __ll;
typedef list<int> LI;

struct Command {
	bool blue;
	int button;
};

typedef vector<Command> CommandList;

class GCJ {
	static const size_t BufferSize = 1024000;
	FILE *m_Source;
	size_t m_BufferSize;
	char *m_Buffer;

public:
	GCJ(FILE *source, size_t buffer_size = BufferSize) {
		m_Source = source;
		m_BufferSize = buffer_size;
		m_Buffer = new char[m_BufferSize];
	}

	~GCJ() {
		delete [] m_Buffer;
	}

	bool solve(void) {
		int cases;
		if (!getInt(cases) || cases < 1) {
			return false;
		}

		int solved = 0;
		int i;
		for (i = 0; i < cases; ++i) {
			if (solve(i)) {
				++solved;
			}
		}
		return solved >= cases;
	}

private:
	bool solve(int case_index) {
		CommandList command_list;
		char *p = getSingleLine();
		if (!p) return false;
		const char *delim = "\t ";
		p = strtok(p, delim);
		if (!p) return false;
		int sequences = atoi(p);
		if (sequences < 1) return false;
		while (--sequences >= 0) {
			p = strtok(NULL, delim);
			if (!p) return false;
			Command c;
			if (*p == 'B') {
				c.blue = true;
			} else if (*p == 'O') {
				c.blue = false;
			} else {
				return false;
			}
			p = strtok(NULL, delim);
			if (!p) return false;
			c.button = atoi(p);
			if (c.button < 1) return false;
			command_list.push_back(c);
		}

		__ll total = 0;

		int moved[2] = { 0 };
		int pos[2] = { 1, 1 };
		CommandList::const_iterator it;
		for (it = command_list.begin(); it != command_list.end(); ++it) {
			const Command &c = *it;
			int index = c.blue ? 0 : 1;
			int other = c.blue ? 1 : 0;
			int distance = abs(c.button - pos[index]);
			distance -= min(moved[other], distance);	// move
			++distance;		// press
			moved[other] = 0;
			moved[index] += distance;		// can move the other
			pos[index] = c.button;		// set abs pos
			total += distance;
		}

		printf("Case #%d: %lld\n", case_index + 1, total);
		return true;
	}

	int getCases(void) {
		int cases;
		char *p = getSingleLine();
		if (p != NULL && sscanf(p, "%d", &cases) >= 1) {
			return cases;
		}
		return -1;
	}

	bool getInt(int &i) {
		char *p = getSingleLine();
		return p != NULL && sscanf(p, "%d", &i) >= 1;
	}

	char *getSingleLine(void) {
		char *p = fgets(m_Buffer, m_BufferSize, m_Source);
		if (!p) {
			return NULL;
		}
		char *term = p + strlen(p) - 1;
		if (term > p && *term == '\n') {
			*term = 0;
		}
		return p;
	}

};


int main()
{
	GCJ gcj(stdin);
	return gcj.solve() ? 0 : -1;
}

