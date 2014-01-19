package com.nostacktrace.topcoder.srm519;


import java.util.HashMap;

/**
 * SRM 521 RedAndGreen
 *
 * http://community.topcoder.com/stat?c=problem_statement&pm=11567&rd=14546
 *
 * This question asks us to find the minimum number of toggles of a string of
 * R and G characters such that all the Rs appear before all of the Gs.
 *
 * Judging from the simpler solutions I see out there, my approach was overkill.  The
 * simpler solution occurred to me, but I couldn't convince myself that it was right, so
 * I opted for a recursive solution where:
 *
 * If the string starts with an R or ends with a G, strip that character and solve the simpler problem
 * otherwise:
 *    we will have to either repaint the first or last character.
 *    the cost will be 1 plus the minimum cost of:
 *        repainting all the characters except the first or
 *        repainting all the characters but the last
 *
 * Since we are recursing n-1, I added a simple cache to memoize the intermediate results.
 * There are only two values for each position, so we can expect a lot of common subproblems.
 * Since the input is limited to 50, this should be more than enough optimization.
 */
public class RedAndGreen {
    public int minPaints(String row) {
        return minPaints(row, new HashMap<String, Integer>());
    }

    private int minPaints(String row, HashMap<String,Integer> cache) {
        int length = row.length();

        if (length == 0) {
            return 0;

        } else if (cache.containsKey(row)) {
            // cache hit
            return cache.get(row);

        } else if (row.charAt(0) == 'R') {
            // strip any leading Rs
            int costRest = minPaints(row.substring(1), cache);
            cache.put(row, costRest);
            return costRest;

        } else if (row.charAt(length-1) == 'G') {
            // string any trailing Gs
            int costRest = minPaints(row.substring(0, length - 1), cache);
            cache.put(row, costRest);
            return costRest;

        } else {
            // branch, considering the strings with the first and the last character removed
            // our minimal cost is 1 more than the minimum of both possibilities
            int costRight = minPaints(row.substring(1), cache);
            int costLeft  = minPaints(row.substring(0, length-1), cache);
            int cost      = 1 + Math.min(costLeft, costRight);
            cache.put(row, cost);
            return cost;
        }

    }
}
