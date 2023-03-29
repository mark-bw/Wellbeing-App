package com.sociallaboursupply.sls_wellbeing_app.Utils;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DateUtilsTest {

    Date date;
    String dateString;

    @Before
    public void setUp() {
        date = new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime();
        dateString = "2020-01-01 00:00:00";
    }

    @Test
    public void stringToDate() {
        assertEquals(DateUtils.databaseStringToDate(dateString), date);
    }

    @Test
    public void dateToString() {
        // Get
        assertEquals(DateUtils.dateToDatabaseString(date), dateString);
        System.err.println(DateUtils.dateToDatabaseString(date));
    }

    @Test
    public void invalidStringToDate() {
        assertNull(DateUtils.databaseStringToDate("invalidString"));
    }
}