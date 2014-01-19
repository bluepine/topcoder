package com.nostacktrace.topcoder.srm518;


public class LargestSubsequenceRunner {
    public static void run(String name, String expected, String actual) {
        String status = (expected.equals(actual)) ? "OK" : "FAIL";
        System.out.println(name + "[" + status + "] " + expected + " : " + actual);
    }

    public static void main(String[] args) {
        LargestSubsequence problem = new LargestSubsequence();

        run("ex 0", "tt", problem.getLargest("test"));
        run("ex 1", "a", problem.getLargest("a"));
        run("ex 2", "xple", problem.getLargest("example"));
        run("ex 3", "zyog", problem.getLargest("aquickbrownfoxjumpsoverthelazydog"));

    }
}
