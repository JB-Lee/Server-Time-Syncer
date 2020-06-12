package org.cnsl.software.finalproject.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimeFormatter {

    private final static int SEC = 60;
    private final static int MIN = 60;
    private final static int HOUR = 24;

    public static String formatTime(long epochSecond) {
        String formattedTime = "";

        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(epochSecond * 1000);

        SimpleDateFormat sdf;

        long now = (System.currentTimeMillis() / 1000);
        long diffTime = now - epochSecond;

        int diffDay = (int) (diffTime / (SEC * MIN * HOUR));

        if (diffDay < 3) {
            if (diffDay == 0)
                formattedTime += "오늘 ";
            else if (diffDay == 1)
                formattedTime += "어제 ";
            else if (diffDay == 2)
                formattedTime += "그저께 ";


            if (date.get(Calendar.HOUR_OF_DAY) < 13)
                formattedTime += "오전 ";
            else
                formattedTime += "오후 ";

            sdf = new SimpleDateFormat("hh:mm", Locale.US);
        } else {
            sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.US);
        }

        formattedTime += sdf.format(date.getTime());

        return formattedTime;
    }
}
