package com.nostacktrace.topcoder.srm520;

/**
 * SRM 520 - SRMRoomAssignmentPhase
 * <p/>
 * http://community.topcoder.com/stat?c=problem_statement&pm=11380&rd=14545
 */

public class SRMRoomAssignmentPhase {
    public int countCompetitors(int[] ratings, int K) {
        int my_rating = ratings[0];
        int num_higher = 0;

        for (int rating: ratings) {
            if (rating > my_rating) {
                num_higher++;
            }

        }

        return num_higher / K;
    }
}
