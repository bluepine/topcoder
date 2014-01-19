package com.nostacktrace.topcoder.srm520;

/**
 * SRM 520 DIV 1 250 points
 *
 *
 * This problem asks us to compute highest possible submission score
 * for a 3 problem set by distributing luck points to the various problems.
 * Luck points reduce the time spent on a problem, increasing that problems score
 * and possibly making an unsubmittable problem submittable.
 *
 * Given the small problem space, a brute force search of all possible
 * luck distributions is sufficient.
 */

public class SRMCodingPhase {
    public static final int SRM_TIME_LIMIT = 75;

    //  Evaluate score for a given luck distribution, as per statement:
    //       (points[0] - 2t) points for the first problem, or
    //       (points[1] - 4t) points for the second problem, or
    //       (points[2] - 8t) points for the third problem.

    // a luck value of -1 indicates that no solution is submitted
    public int calculateScore(int[] points, int[] skills, int[] luck) {
        int time0 = (luck[0] == -1) ? 0 : skills[0] - luck[0];
        int time1 = (luck[1] == -1) ? 0 : skills[1] - luck[1];
        int time2 = (luck[2] == -1) ? 0 : skills[2] - luck[2];

        if (time0 + time1 + time2 > SRM_TIME_LIMIT) {
            return 0;
        }

        int score0 = (time0 == 0) ? 0 : points[0] - 2 * time0;
        int score1 = (time1 == 0) ? 0 : points[1] - 4 * time1;
        int score2 = (time2 == 0) ? 0 : points[2] - 8 * time2;

        return score0 + score1 + score2;
    }

    // calculate how many luck points are still available for
    // distribution considering a current luck distribution
    int remainingLuck(int maxLuck, int[] lucks, int skill) {
        // subtract all the used luck from maxLuck
        for (int luck: lucks) {
            if (luck > 0) {
                maxLuck -= luck;
            }
        }

        // the remaining usable luck for a problem
        // is the minimum of the available luck
        // and the maximum available to use on a problem of given skill
        return Math.min(maxLuck, skill-1);

    }


    // Constraints
    // points will contain exactly 3 elements.
    // points[0] will be between 250 and 300, inclusive.
    // points[1] will be between 450 and 600, inclusive.
    // points[2] will be between 900 and 1100, inclusive.
    // skills will contain exactly 3 elements.
    // Each element of skills will be between 1 and 100, inclusive.
    // luck will be between 0 and 100, inclusive.
    public int countScore(int[] points, int[] skills, int luck) {
        int[] lucks = {0,0,0};
        int bestScore = 0;

        // iterate
        int max0 = remainingLuck(luck, lucks, skills[0]);
        for (lucks[0] = -1; lucks[0]<=max0; lucks[0]++) {

            lucks[1] = 0; lucks[2] = 0;
            int max1 = remainingLuck(luck, lucks, skills[1]);

            for (lucks[1]=-1; lucks[1]<=max1; lucks[1]++) {
                lucks[2] = 0;

                int max2 = remainingLuck(luck, lucks, skills[2]);
                for (lucks[2]=-1; lucks[2]<=max2; lucks[2]++) {

                    int score = calculateScore(points, skills, lucks);
                    if (score > bestScore) {
                        bestScore = score;
                    }
                }
            }
        }

        return bestScore;
    }

}
