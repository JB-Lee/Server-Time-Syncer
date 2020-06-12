package org.cnsl.software.finalproject.models;

import android.graphics.Color;

public class SignUpModel {
    private final static String re1 = "^(?=.*[0-9]).+$";
    private final static String re2 = "^(?=.*[a-z]).+$";
    private final static String re3 = "^(?=.*[A-Z]).+$";
    private final static String re4 = "^(?=.*[!@#$%^&*]).+$";

    public int getPasswordRisk(String pw) {
        int score = 0;
        score += (pw.matches(re1) ? 10 : 0);
        score += (pw.matches(re2) ? 26 : 0);
        score += (pw.matches(re3) ? 26 : 0);
        score += (pw.matches(re4) ? 8 : 0);
        return (int)(Math.pow(score, pw.length())/40000000000L);
    }

    public boolean passwordValid(int risk) {
        return risk / (60 * 60) > 1;
    }

    public boolean doSignUp(String id, String pw) {
        //계정 생성
        return true;
    }

    public boolean chkId(String id) {
        //id 중복 검사
        return true;
    }

    public boolean chkEmail(String email) {
        return true;
    }

    public String getCrackTime(int risk) {
        int sec = risk;
        int min = sec / 60;
        int hour = min / 60;
        int day = hour / 24;
        int month = day / 30;
        int year = day / 365;

        String crackTime = "";

        if (year > 0)
            crackTime += String.valueOf(year) + "년";
        else if (month > 0)
            crackTime += String.valueOf(month) + "개월";
        else if (day > 0)
            crackTime += String.valueOf(day) + "일";
        else if (hour > 0)
            crackTime += String.valueOf(hour) + "시간";
        else if (min > 0)
            crackTime += String.valueOf(min) + "분";
        else if (sec > 0)
            crackTime += String.valueOf(sec) + "초";
        else
            crackTime += "즉시";

        return crackTime;
    }

    public int getWarningColor(int risk) {
        int sec = risk;
        int min = sec / 60;
        int hour = min / 60;
        int day = hour / 24;
        int month = day / 30;
        int year = day / 365;

        int color;

        if (year > 2) {
            color = Color.parseColor("#00ff00");
        }
        else if (month > 2) {
            color = Color.parseColor("#88ff00");
        }
        else if (hour > 12) {
            color = Color.parseColor("#ffff00");
        }
        else if (hour > 1) {
            color = Color.parseColor("#ff8800");
        }
        else {
            color = Color.parseColor("#ff0000");
        }

        return color;
    }
}