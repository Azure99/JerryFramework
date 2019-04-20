package com.rainng.jerry.mouse.http.map;

public class HttpCookieMap extends BaseHttpMap {

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (String key : keySet()) {
            builder.append(key);
            builder.append("=");
            builder.append(get(key));
            builder.append("; ");
        }

        int length = builder.length();
        if (length > 2) {
            builder.delete(length - 2, length);
        }

        return builder.toString();
    }

    public static HttpCookieMap parse(String cookiesStr) {
        HttpCookieMap cookieMap = new HttpCookieMap();

        String[] cookies = cookiesStr.split(";\\s");
        for (String cookie : cookies) {
            String[] kv = cookie.split("=");
            cookieMap.put(kv[0], kv[1]);
        }

        return cookieMap;
    }
}
