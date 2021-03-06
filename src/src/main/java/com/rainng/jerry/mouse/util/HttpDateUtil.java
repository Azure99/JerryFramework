package com.rainng.jerry.mouse.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HttpDateUtil {
    private HttpDateUtil() {

    }

    public static String getNowDateString() {
        return getDateString(new Date());
    }

    public static String getDateString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return format.format(date);
    }
}
