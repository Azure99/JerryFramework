package com.rainng.jerry.mouse.http.initializer;

import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.constant.HttpContentType;
import com.rainng.jerry.mouse.http.constant.HttpHeaderKey;
import com.rainng.jerry.mouse.http.map.HttpHeaderMap;
import com.rainng.jerry.mouse.util.HttpDateUtil;

public class HttpResponseIniter {

    private HttpResponseIniter() {

    }

    public static void init(HttpContext context) {
        HttpHeaderMap headers = context.getResponse().getHeaders();

        headers.set(HttpHeaderKey.CONTENT_LENGTH, "0");
        headers.set(HttpHeaderKey.CONTENT_TYPE, HttpContentType.TEXT_PLAIN);
        headers.set(HttpHeaderKey.DATE, HttpDateUtil.getNowDateString());
        headers.set(HttpHeaderKey.SERVER, "Rainng-Jerrymouse/0.1");
    }
}
