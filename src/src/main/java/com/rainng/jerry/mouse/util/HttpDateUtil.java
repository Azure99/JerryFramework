package com.rainng.jerry.mouse.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HttpDateUtil {
    private static final ThreadLocal<SimpleDateFormat> threadLocalFormat = ThreadLocal.withInitial(() -> {
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return format;
    });

    private static volatile String nowDateString = getDateString(new Date());

    private HttpDateUtil() {
    }

    static {
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(() -> {
            nowDateString = getDateString(new Date());
        }, 1000, 1000, TimeUnit.MILLISECONDS);
    }

    public static String getNowDateString() {
        return nowDateString;
    }

    public static String getDateString(Date date) {
        return threadLocalFormat.get().format(date);
    }
}
