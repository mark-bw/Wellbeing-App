package com.sociallaboursupply.sls_wellbeing_app.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static Date databaseStringToDate(String string) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(string);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String dateToDatabaseString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = dateFormat.format(date);
        return dateTime;
    }
}
