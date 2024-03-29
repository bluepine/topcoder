// BEGIN CUT HERE
// PROBLEM STATEMENT
// Elly wants to write a program that lists all the files in 
// a given directory.
// She already has the list of all the files.
// You will be given this list as a vector <string> files.
// 
// In addition to the names of files, the variable files will 
// contain exactly two additional elements:
// the current directory (the string "."), and the parent 
// directory (the string "..").
// These two elements may be anywhere in files.
// However, Elly wants them to be the last two elements.
// 
// In order to move the two directories to the last two 
// positions in files, she wants you to
// perform the following steps:
// 
// 
// If "." and ".." are the last two elements of files (in any 
// order), you are done.
// Find the first element of files that is either "." or 
// "..". Swap it with the last element of files.
// If "." and ".." are now the last two elements of files (in 
// any order), you are done.
// Find the first element of files that is either "." or 
// "..". Swap it with the element of files that is one 
// position before the last one.
// 
// 
// Your method must perform the above steps and return a 
// vector <string> containing the modified
// order of elements in files.
// 
// DEFINITION
// Class:EllysDirectoryListing
// Method:getFiles
// Parameters:vector <string>
// Returns:vector <string>
// Method signature:vector <string> getFiles(vector <string> 
// files)
// 
// 
// NOTES
// -Swapping elements at positions i and j means that the 
// element at position i is moved to the position j and vice 
// versa.
// 
// 
// CONSTRAINTS
// -files will contain between 2 and 50 elements, inclusive.
// -All elements of files will be distinct.
// -Exactly one element of files will be ".".
// -Exactly one element of files will be "..".
// -Each element of files will contain between 1 and 20 
// characters, inclusive.
// -Each element of files will contain only uppercase and 
// lowercase letters and dots ('A'-'Z', 'a'-'z', '.').
// 
// 
// EXAMPLES
// 
// 0)
// {"ContestApplet.jnlp", ".", "Image.jpg", "..", "Book.pdf", 
// "Movie.avi"}
// 
// Returns: {"ContestApplet.jnlp", "Movie.avi", "Image.jpg", 
// "Book.pdf", "..", "." }
// 
// The directories are not the last two elements, so we 
// search for the first one (in this case "."), and swap it 
// with the last element (in this case "Movie.avi"). As "." 
// and ".." are still not the last two elements, we search 
// for the second one (in  this case "..") and swap it with 
// "Book.pdf".
// 
// 1)
// {"Image.jpg", "..", "."}
// 
// Returns: {"Image.jpg", "..", "." }
// 
// In this case the directories are already the last two 
// elements, so we do nothing.
// 
// 2)
// {"..", ".", "Image.jpg"}
// 
// Returns: {"Image.jpg", ".", ".." }
// 
// After swapping ".." with "Image.jpg", the directories are 
// already the last two elements, so we are done.
// 
// 3)
// {"No", "..", "Zaphod", ".", "Just", "very", "very...", 
// "Improbable"}
// 
// Returns: {"No", "Improbable", "Zaphod", "very...", "Just", 
// "very", ".", ".." }
// 
// 
// 
// 4)
// {"www.topcoder.com", "Ever.tried", ".", "Ever.failed", 
// "..", "No", "Matter.", "Try", "Again.", "Fail", "Again..", 
// "Fail.Better"}
// 
// Returns: {"www.topcoder.com", "Ever.tried", "Fail.Better", 
// "Ever.failed", "Again..", "No", "Matter.", "Try", 
// "Again.", "Fail", "..", "." }
// 
// 5)
// {"This", ".", "is", "tricky", "test", ".."}
// 
// Returns: {"This", "test", "is", "tricky", "..", "." }
// 
// The first swap moves ".." to position 1, so we need to 
// move it again to index 4 (indexed from zero).
// 
// END CUT HERE
#line 121 "EllysDirectoryListing.cpp"
#include <string>
#include <vector>
#include <iostream>
#include <sstream>
#include <algorithm>
#include <stdio.h>

using namespace std;

class EllysDirectoryListing
    {
    public:
    vector <string> getFiles(vector <string> files)
    {
        int i;
        int j = files.size()-1;
        int k=2;
        while(k-->0)
            for (i = 0; i<files.size()-2; i++)
                if (files[i]=="." || files[i]=="..")
                    swap(files[i], files[j--]);
        return files;
    }
    
// BEGIN CUT HERE
	public:
	void run_test(int Case) { if ((Case == -1) || (Case == 0)) test_case_0(); if ((Case == -1) || (Case == 1)) test_case_1(); if ((Case == -1) || (Case == 2)) test_case_2(); if ((Case == -1) || (Case == 3)) test_case_3(); if ((Case == -1) || (Case == 4)) test_case_4(); if ((Case == -1) || (Case == 5)) test_case_5(); }
	private:
	template <typename T> string print_array(const vector<T> &V) { ostringstream os; os << "{ "; for (typename vector<T>::const_iterator iter = V.begin(); iter != V.end(); ++iter) os << '\"' << *iter << "\","; os << " }"; return os.str(); }
	void verify_case(int Case, const vector <string> &Expected, const vector <string> &Received) { cerr << "Test Case #" << Case << "..."; if (Expected == Received) cerr << "PASSED" << endl; else { cerr << "FAILED" << endl; cerr << "\tExpected: " << print_array(Expected) << endl; cerr << "\tReceived: " << print_array(Received) << endl; } }
	void test_case_0() { string Arr0[] = {"ContestApplet.jnlp", ".", "Image.jpg", "..", "Book.pdf", "Movie.avi"}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); string Arr1[] = {"ContestApplet.jnlp", "Movie.avi", "Image.jpg", "Book.pdf", "..", "." }; vector <string> Arg1(Arr1, Arr1 + (sizeof(Arr1) / sizeof(Arr1[0]))); verify_case(0, Arg1, getFiles(Arg0)); }
	void test_case_1() { string Arr0[] = {"Image.jpg", "..", "."}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); string Arr1[] = {"Image.jpg", "..", "." }; vector <string> Arg1(Arr1, Arr1 + (sizeof(Arr1) / sizeof(Arr1[0]))); verify_case(1, Arg1, getFiles(Arg0)); }
	void test_case_2() { string Arr0[] = {"..", ".", "Image.jpg"}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); string Arr1[] = {"Image.jpg", ".", ".." }; vector <string> Arg1(Arr1, Arr1 + (sizeof(Arr1) / sizeof(Arr1[0]))); verify_case(2, Arg1, getFiles(Arg0)); }
	void test_case_3() { string Arr0[] = {"No", "..", "Zaphod", ".", "Just", "very", "very...", "Improbable"}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); string Arr1[] = {"No", "Improbable", "Zaphod", "very...", "Just", "very", ".", ".." }; vector <string> Arg1(Arr1, Arr1 + (sizeof(Arr1) / sizeof(Arr1[0]))); verify_case(3, Arg1, getFiles(Arg0)); }
	void test_case_4() { string Arr0[] = {"www.topcoder.com", "Ever.tried", ".", "Ever.failed", "..", "No", "Matter.", "Try", "Again.", "Fail", "Again..", "Fail.Better"}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); string Arr1[] = {"www.topcoder.com", "Ever.tried", "Fail.Better", "Ever.failed", "Again..", "No", "Matter.", "Try", "Again.", "Fail", "..", "." }; vector <string> Arg1(Arr1, Arr1 + (sizeof(Arr1) / sizeof(Arr1[0]))); verify_case(4, Arg1, getFiles(Arg0)); }
	void test_case_5() { string Arr0[] = {"This", ".", "is", "tricky", "test", ".."}; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); string Arr1[] = {"This", "test", "is", "tricky", "..", "." }; vector <string> Arg1(Arr1, Arr1 + (sizeof(Arr1) / sizeof(Arr1[0]))); verify_case(5, Arg1, getFiles(Arg0)); }

// END CUT HERE

    };

// BEGIN CUT HERE
int main()
    {
    EllysDirectoryListing ___test;
    ___test.run_test(-1);
    }
// END CUT HERE
