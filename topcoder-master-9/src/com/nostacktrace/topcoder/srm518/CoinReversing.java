package com.nostacktrace.topcoder.srm518;


/**
 * SRM 518 CoinReversing
 *
 * http://community.topcoder.com/stat?c=problem_statement&pm=11473&rd=14543
 *
 * The solution here was not immediately obvious. I thought it required some complicated
 * recursion tracking all the possible choices, but working it out it become clear that
 * all the coins should have the same chance of being heads or tails.  This solution tracks the
 * chance of any one coin being heads after performing the flips in requested.  The expectation
 * is that probablity * N.  The logic of the calculation is in the code.
 */

public class CoinReversing {
    public double expectedHeads(int N, int[] a) {
        double headsPercent = 1.0;

        for (int numToFlip: a) {
            double chanceThisRound =  (1.0*numToFlip)/N;
            double tailsPercent = 1.0 - headsPercent;

            // if there was p% chance the coin is heads and c% chance it will flip
            // we have 4 cases:
            //   c * p         heads -> tails
            //   c * (1-p)     heads -> heads
            //   (1-c) * p     tails -> heads
            //   (1-c) * (1-p) tails -> tails
            //
            // so the chance of being heads is  c * (1-p) + (1-c) * p

            headsPercent = chanceThisRound * tailsPercent +
                          (1-chanceThisRound) * headsPercent;
        }

        return headsPercent * N;
    }

}
