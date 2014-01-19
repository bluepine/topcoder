package com.nostacktrace.topcoder.srm520;


public class SRMRoomAssignmentPhaseRunner {
    public static void run(String name, int expected, int actual) {
        String status = (expected == actual) ? "OK" : "FAIL";
        System.out.println(name + "[" + status + "] " + expected + " : " + actual);
    }

    public static void main(String[] args) {
        SRMRoomAssignmentPhase problem = new SRMRoomAssignmentPhase();

        run("ex 0",
                2,
                problem.countCompetitors(new int[]{491, 981, 1199, 763, 994, 879, 888}, 3));

        run("ex 1",
                0,
                problem.countCompetitors(new int[]{1024, 1000, 600}, 1));

        run("ex2",
                1,
                problem.countCompetitors(new int[]{505, 679, 900, 1022}, 2));

        run("ex3",
                1,
                problem.countCompetitors(new int[]{716, 58, 1000, 1004, 912, 822, 453, 1100, 558}, 3));

        run("ex4",
                3,
                problem.countCompetitors(new int[]{422, 623, 1023, 941, 882, 776, 852, 495, 803, 622, 618, 532, 751, 500}, 4));

        run("ex5",
                2,
                problem.countCompetitors(new int[]{1197, 1198, 1196, 1195, 1199}, 1));


    }
}
