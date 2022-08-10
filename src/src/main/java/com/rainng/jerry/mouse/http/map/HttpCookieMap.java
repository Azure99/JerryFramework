package com.rainng.jerry.mouse.http.map;

import com.rainng.jerry.mouse.http.Cookie;

public class HttpCookieMap extends BaseHttpMap<Cookie> {

    public void set(Cookie cookie) {
        set(cookie.getName(), cookie);
    }

    public void set(String name, String value) {
        set(name, new Cookie(name, value));
    }
}
