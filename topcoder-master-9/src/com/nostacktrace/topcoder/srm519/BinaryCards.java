package com.nostacktrace.topcoder.srm519;


/*
 * SRM 519 - Binary Cards
 *
 * http://community.topcoder.com/stat?c=problem_statement&pm=11552&rd=14544
 *
 * This problems asks us to compute the highest number
 * reached as we flip binary bits from A .. B given that
 * we visit every number once and that only 1 bit is changed
 * at a time, starting with the higher bits.
 *
 * With some thought, it's clear that the maximum bumber will come
 * on a transition from 01+ to 10+ for whatever the first differing
 * binary digit is.
 *
 * ex:  00100010   -->  001 0 010
 *      00110000   -->  001 1 000
 *
 *      The largest tran bit transaction will be flipping the 4th bit in the
 *      transition from 001 0 111 to 001 1 000, yielding 001 1 111.
 *
 *  So the algorithm here is to take all the leading common bits and at the first
 *  differing bit, fill with 1s.
 */
public class BinaryCards {
    public long largestNumber(long A, long B) {
        long result = 0;

        for (int i=63; i>=0; i--) {
            long bitmask = 1L << i;

            boolean bitASet = (A & bitmask) > 0;
            boolean bitBSet = (B & bitmask) > 0;

            if (bitASet != bitBSet) {
                // bit differs, fill set all remaing bits
                result |= ((bitmask << 1) - 1);
                break;
            }

            if (bitASet) {
                result |= bitmask;
            }
        }

        return result;
    }
}
