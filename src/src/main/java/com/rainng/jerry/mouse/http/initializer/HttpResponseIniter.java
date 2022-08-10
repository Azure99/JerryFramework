package com.rainng.jerry.mouse.http.initializer;

import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.constant.HttpContentType;
import com.rainng.jerry.mouse.http.constant.HttpHeaderKey;
import com.rainng.jerry.mouse.http.map.HttpHeaderMap;

public class HttpResponseIniter {

    private HttpResponseIniter() {

    }

    public static void init(HttpContext context) {
        HttpHeaderMap headers = context.getResponse().getHeaders();
        headers.set(HttpHeaderKey.CONTENT_TYPE, HttpContentType.TEXT_PLAIN);
    }
}
