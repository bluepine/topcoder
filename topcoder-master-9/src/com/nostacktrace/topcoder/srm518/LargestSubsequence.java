package com.nostacktrace.topcoder.srm518;

/**
 * SRM 518 LargestSubsequence
 *
 * http://community.topcoder.com/stat?c=problem_statement&pm=11471&rd=14543
 *
 * Given a String s, return the lexicographically largest subsequence of s.
 * My first instinct was to start at the beginning of the string and select the
 * first instance of the largest character remaining and then recurse. But then
 * I realized that we could actually perform the computation from the rear and
 * construct the correct solution up.  Algorithm performance wasn't a key concern
 * here, but the solution was straightforward.
 */
public class LargestSubsequence {
   public String getLargest(String s) {
       StringBuffer result = new StringBuffer(s.length());
       char prev = 'a';

       for (int i=s.length()-1; i>=0; i--) {
           char current = s.charAt(i);
           if (current >= prev) {
               prev = current;
               result.append(current);
           }
       }

       // we added the characters to the string buffer in reverse order, so we
       // need to reverse the result
       return result.reverse().toString();
   }

}
