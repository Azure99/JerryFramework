package com.rainng.jerry.mouse.http.initializer;

import com.rainng.jerry.mouse.http.Cookie;
import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.HttpRequest;
import com.rainng.jerry.mouse.http.constant.HttpHeaderKey;
import com.rainng.jerry.mouse.http.map.HttpCookieMap;

public class HttpCookieIniter {
    private HttpCookieIniter() {

    }

    public static void init(HttpContext context) {
        HttpRequest request = context.getRequest();
        if (!request.getHeaders().containsKey(HttpHeaderKey.COOKIE)) {
            return;
        }

        HttpCookieMap cookieMap = context.getCookies();
        String cookiesStr = request.getHeaders().get(HttpHeaderKey.COOKIE, "");

        String[] cookies = cookiesStr.split(";\\s");
        for (String cookie : cookies) {
            String[] kv = cookie.split("=");
            String key = kv[0];
            String value = kv.length == 2 ? kv[1] : "";
            cookieMap.put(kv[0], new Cookie(key, value, true));
        }
    }
}
