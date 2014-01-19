package com.nostacktrace.topcoder.srm518;


/**
 * SRM 518 Twice String
 *
 * http://community.topcoder.com/stat?c=problem_statement&pm=11480&rd=14543
 *
 * A simple problem to compute the shortest string in which the source string appears twice.
 * This solution just iterates all possible extensions of the source string that could
 * result in the source string being at the end of the new string.  Nothing elegant or
 * pretty - just brute force.
 */
public class TwiceString {

    public String getShortest(String s) {
        for (int i=s.length()-1; i>=0; i--) {
           String test = s + s.substring(i);

           if (test.lastIndexOf(s)>0) {
               return test;
           }
        }

        // won't get here.
        return s + s;
    }
}
