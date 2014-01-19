package com.nostacktrace.topcoder.srm518;

/**
 *
 */
public class CoinReversingRunner{
    public static boolean compare(double first, double second) {
        return Math.abs(first-second) <= 1e-9;
    }

    public static void run(String name, double expected, double actual) {
        String status = (compare(expected,actual)) ? "OK" : "FAIL";
        System.out.println(name + "[" + status + "] " + expected + " : " + actual);
    }

    public static void main(String[] args) {
        CoinReversing problem = new CoinReversing();

        run("ex 0", 1.666666666666667, problem.expectedHeads(3, new int[] {2, 2}));
        run("ex 1", 0, problem.expectedHeads(10, new int[] {10,10,10}));
        run("ex 2", 4.792639999999999, problem.expectedHeads(10, new int[] {2,7,1,8,2,8}));
        run("ex 2", 498.1980774932278, problem.expectedHeads(1000, new int[] {916,153,357,729,183,848,61,672,295,936}));
    }
}
