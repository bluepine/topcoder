package com.nostacktrace.topcoder.srm519;

public class BinaryCardsRunner {
    public static void run(String name, long expected, long actual) {
        String status = (expected == actual) ? "OK" : "FAIL";
        System.out.println(name + "[" + status + "] " + expected + " : " + actual);
    }

    public static void main(String[] args) {
        BinaryCards problem = new BinaryCards();

        run("ex 0", 6, problem.largestNumber(6, 6));
        run("ex 1", 7, problem.largestNumber(6,7));
        run("ex 2", 15, problem.largestNumber(6,8));
        run("ex 3", 15, problem.largestNumber(1,11));
        run("ex 4", 39, problem.largestNumber(35,38));
        run("ex 5",
            1125899906842639L, problem.largestNumber(1125899906842630L,1125899906842632L));
    }
}
