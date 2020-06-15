package org.cnsl.software.finalproject.utils;

import android.graphics.Color;

public class PasswordHelper {
    private final static String re1 = "^(?=.*[0-9]).+$";
    private final static String re2 = "^(?=.*[a-z]).+$";
    private final static String re3 = "^(?=.*[A-Z]).+$";
    private final static String re4 = "^(?=.*[!@#$%^&*]).+$";

    public static int getPasswordRisk(String pw) {
        int score = 0;
        score += (pw.matches(re1) ? 10 : 0);
        score += (pw.matches(re2) ? 26 : 0);
        score += (pw.matches(re3) ? 26 : 0);
        score += (pw.matches(re4) ? 8 : 0);
        return (int) (Math.pow(score, pw.length()) / 40000000000L);
    }

    public static String getCrackTime(int risk) {
        int min = risk / 60;
        int hour = min / 60;
        int day = hour / 24;
        int month = day / 30;
        int year = day / 365;

        String crackTime = "";

        if (year > 0)
            crackTime += year + "년";
        else if (month > 0)
            crackTime += month + "개월";
        else if (day > 0)
            crackTime += day + "일";
        else if (hour > 0)
            crackTime += hour + "시간";
        else if (min > 0)
            crackTime += min + "분";
        else if (risk > 0)
            crackTime += risk + "초";
        else
            crackTime += "즉시";

        return crackTime;
    }

    public static int getWarningColor(int risk) {
        int min = risk / 60;
        int hour = min / 60;
        int day = hour / 24;
        int month = day / 30;
        int year = day / 365;

        int color;

        if (year > 2) {
            color = Color.parseColor("#00ff00");
        } else if (month > 2) {
            color = Color.parseColor("#88ff00");
        } else if (hour > 12) {
            color = Color.parseColor("#ffff00");
        } else if (hour > 1) {
            color = Color.parseColor("#ff8800");
        } else {
            color = Color.parseColor("#ff0000");
        }

        return color;
    }
}
