package com.nostacktrace.topcoder.srm520;

public class SRMCodingPhaseRunner {

    public static void run(String name, int expected, int actual) {
        String status = (expected == actual) ? "OK" : "FAIL";
        System.out.println( name + "["  + status + "] " + expected + " : " + actual);

    }

    public static void main(String[] args) {
        SRMCodingPhase problem = new SRMCodingPhase();


        run("ex 0",
            1310,
            problem.countScore(new int[]{250, 500, 1000},
                    new int[]{10, 25, 40},
                    0));


        run("ex 1",
            680,
            problem.countScore(new int[]{300, 600, 900},
                    new int[]{30, 65, 90},
                    25));


        run("ex 2",
            1736,
            problem.countScore(new int[]{250, 550, 950},
                    new int[]{10, 25, 40},
                    75));

        run("ex 3",
            1216,
            problem.countScore(new int[]{256, 512, 1024},
                    new int[]{35, 30, 25},
                    0));

        run("ex 4",
            0,
            problem.countScore(new int[]{300, 600, 1100},
                    new int[]{80, 90, 100},
                    4));
    }
}