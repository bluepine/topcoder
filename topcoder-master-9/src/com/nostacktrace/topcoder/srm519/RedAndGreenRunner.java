package com.nostacktrace.topcoder.srm519;

public class RedAndGreenRunner {
    public static void run(String name, int expected, int actual) {
        String status = (expected == actual) ? "OK" : "FAIL";
        System.out.println(name + "[" + status + "] " + expected + " : " + actual);
    }

    public static void main(String[] args) {
        RedAndGreen problem = new RedAndGreen();

        run("ex 0", 2, problem.minPaints("RGRGR"));
        run("ex 1", 0, problem.minPaints("RRRGGGGG"));
        run("ex 2", 3, problem.minPaints("GGGGRRR"));
        run("ex 3", 8, problem.minPaints("RGRGRGRGRGRGRGRGR"));
        run("ex 4", 9, problem.minPaints("RRRGGGRGGGRGGRRRGGRRRGR"));
    }
}
