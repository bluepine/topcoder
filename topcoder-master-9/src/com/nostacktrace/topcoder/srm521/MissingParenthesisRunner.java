package com.nostacktrace.topcoder.srm521;


public class MissingParenthesisRunner {
    public static void run(String name, int expected, int actual) {
        String status = (expected == actual) ? "OK" : "FAIL";
        System.out.println(name + "[" + status + "] " + expected + " : " + actual);
    }

    public static void main(String[] args) {
        MissingParentheses problem = new MissingParentheses();

        run("ex 0", 2,
                problem.countCorrections("(()(()"));
        run("ex 1", 1,
                problem.countCorrections("()()(()"));
        run("ex 2", 0,
                problem.countCorrections("(())(()())"));
        run("ex 3", 7,
                problem.countCorrections("())(())((()))))()((())))()())())())()()()"));
    }
}

