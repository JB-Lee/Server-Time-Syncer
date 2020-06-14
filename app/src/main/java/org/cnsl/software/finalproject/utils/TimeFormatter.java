package org.cnsl.software.finalproject.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimeFormatter {
    public static String formatTime(long epochSecond) {
        String formattedTime = "";

        Calendar date = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        date.setTimeInMillis(epochSecond * 1000);

        SimpleDateFormat sdf;

        int diffDay = today.get(Calendar.DAY_OF_YEAR) - date.get(Calendar.DAY_OF_YEAR);

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
