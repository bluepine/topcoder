package com.nostacktrace.topcoder.srm521;

/**
 * SRM 521 MissingParentheses
 *
 * http://community.topcoder.com/stat?c=problem_statement&pm=10943
 *
 * Given a sequence of parantheses, how many parentheses need to be inserted
 * to balance all parentheses.  The simple solution here iterates through the
 * string keeping track of the number of unclosed parentheses.  If a close
 * paren is met with no previous open paren, we know we would have needed a closing
 * paren at some point prior.  When the end of the string is reached, if there are
 * any unclosed parens, we know we need to insert a paren for each open paren
 */
public class MissingParentheses {

    public int countCorrections(String par) {
        int parenLevel  = 0;
        int corrections = 0;

        for(int i=0; i<par.length(); i++) {
            char current = par.charAt(i);

            if (current == '(') {
                // open paren is always good
                parenLevel++;
            } else if (current == ')') {
                if (parenLevel>0) {
                    // balanced close, decrement paren level
                    parenLevel--;
                } else {
                    // unbalanced close - add a correction
                    // no change to paren level
                    corrections++;
                }
            }

        }

        // if parenLevel is non-zero, we need parenLevel more corrections.
        corrections += parenLevel;

        return corrections;
    }
}
