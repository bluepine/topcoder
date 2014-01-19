package com.nostacktrace.topcoder.srm518;


public class TwiceStringRunner {

    public static void run(String name, String expected, String actual) {
        String status = (expected.equals(actual)) ? "OK" : "FAIL";
        System.out.println(name + "[" + status + "] " + expected + " : " + actual);
    }

    public static void main(String[] args) {
        TwiceString problem = new TwiceString();

        run("ex 0", "ababa", problem.getShortest("aba"));
        run("ex 1", "xxxxxx", problem.getShortest("xxxxx"));
        run("ex 2", "topcodertopcoder", problem.getShortest("topcoder"));
        run("ex 3", "abracadabracadabra", problem.getShortest("abracadabra"));

    }

}