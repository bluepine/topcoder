package com.nostacktrace.topcoder.srm519;

public class WhichDayRunner {
    public static void run(String name, String expected, String actual) {
        String status = (expected.equals(actual)) ? "OK" : "FAIL";
        System.out.println(name + "[" + status + "] " + expected + " : " + actual);
    }

    public static void main(String[] args) {
        WhichDay problem = new WhichDay();

        run("ex 0", "Saturday",
                problem.getDay(new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"}));
        run("ex 1", "Saturday",
                problem.getDay(new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Friday", "Thursday"}));
        run("ex 2", "Wednesday",
                problem.getDay(new String[]{"Sunday", "Monday", "Tuesday", "Thursday", "Friday", "Saturday"}));
        run("ex 3", "Thursday",
                problem.getDay(new String[]{"Sunday", "Friday", "Tuesday", "Wednesday", "Monday", "Saturday"}));
    }

}
