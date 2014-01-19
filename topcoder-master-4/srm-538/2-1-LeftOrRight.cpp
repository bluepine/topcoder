#include <iostream>
#include <string>
#include <algorithm>

using namespace std;

class LeftOrRight
{
  public:
	int maxDistance(string program)
	{
		int c1 = 0;
		int c2 = 0;
		int s = 0;
		int i;
		for (i = 0; i < program.length(); i++) {
			if (program[i] == 'L') {
				c1--;
                                c2--;
			}
			else if (program[i] == 'R') {
				c1++;
                                c2++;
			}
			else {
			    c1--;
			    c2++;
			}
			s = max(abs(c1), s);
			s = max(abs(c2), s);
		}
		return s;
	};
};

int main()
{
    LeftOrRight c;
    string s = "LLLRLRRR";
    cout << c.maxDistance(s) << endl;
}
