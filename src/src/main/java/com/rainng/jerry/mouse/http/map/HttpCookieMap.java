package com.rainng.jerry.mouse.http.map;

import com.rainng.jerry.mouse.http.Cookie;

public class HttpCookieMap extends BaseHttpMap<Cookie> {

    public String toSetCookieHeaderString() {
        StringBuilder builder = new StringBuilder();

        for (Cookie cookie : values()) {
            if (cookie.isFromRequest() && !cookie.isChanged()) {
                continue;
            }

            builder.append("Set-Cookie: ");
            builder.append(cookie.toString());
            builder.append("\r\n");
        }

        if (builder.length() <= 2) {
            return "";
        } else {
            return builder.toString().substring(0, builder.length() - 2);
        }
    }
}
