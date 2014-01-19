package com.nostacktrace.topcoder.srm519;


/**
 *  SRM 519 WhichDay
 *  http://community.topcoder.com/stat?c=problem_statement&pm=11553&rd=14544
 *
 *  No need to get fancy here
 */
public class WhichDay {
    static final String[] ALL_DAYS = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

    public String getDay(String[] notOnThisDay)  {
        for (String day: ALL_DAYS) {
            if (!in(day, notOnThisDay)) {
                return day;
            }
        }

        return null;
    }

    private boolean in(String target, String[] notOnThisDay) {
        for (String notDay: notOnThisDay) {
            if (notDay.equals(target)) {
                return true;
            }
        }

        return false;
    }
}
