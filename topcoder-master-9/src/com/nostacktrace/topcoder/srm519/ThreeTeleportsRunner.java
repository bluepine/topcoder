package com.nostacktrace.topcoder.srm519;

public class ThreeTeleportsRunner {
    public static void run(String name, long expected, long actual) {
        String status = (expected == actual) ? "OK" : "FAIL";
        System.out.println(name + "[" + status + "] " + expected + " : " + actual);
    }

    public static void main(String[] args) {
        ThreeTeleports problem = new ThreeTeleports();

        run("ex 0", 3, problem.shortestDistance(3, 3, 4, 5,
                new String[] {"1000 1001 1000 1002", "1000 1003 1000 1004", "1000 1005 1000 1006"}));

        run("ex 1", 14, problem.shortestDistance(0,0,20,20,
                new String[] {"1 1 18 20", "1000 1003 1000 1004", "1000 1005 1000 1006"}));

        run("ex 2", 14, problem.shortestDistance(0, 0, 20, 20,
                new String[]{"1000 1003 1000 1004", "18 20 1 1", "1000 1005 1000 1006"}));

        run("ex 3", 30, problem.shortestDistance(10, 10, 10000, 20000,
                new String[]{"1000 1003 1000 1004", "3 3 10004 20002", "1000 1005 1000 1006"}));

        run("ex 4", 117, problem.shortestDistance(3, 7, 10000, 30000,
                new String[]{"3 10 5200 4900", "12212 8699 9999 30011", "12200 8701 5203 4845"}));

        run("ex 5", 36, problem.shortestDistance(0, 0, 1000000000, 1000000000,
                new String[]{"0 1 0 999999999", "1 1000000000 999999999 0", "1000000000 1 1000000000 999999999"}));

    }

}
