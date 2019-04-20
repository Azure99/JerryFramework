package com.rainng.jerry.mouse.http.initializer;

import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.HttpRequest;
import com.rainng.jerry.mouse.http.HttpResponse;
import com.rainng.jerry.mouse.http.constant.HttpHeaderKey;

public class HttpResponseIniter {
    public static void init(HttpContext context) {
        HttpRequest request = context.getRequest();
        HttpResponse response = context.getResponse();

        if (request.getHeaders().containsKey(HttpHeaderKey.COOKIE)) {
            response.setCookies(request.getCookies());
        }
    }
}
