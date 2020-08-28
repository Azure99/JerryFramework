package com.rainng.jerry.mouse.http;

import com.rainng.jerry.mouse.http.map.HttpCookieMap;
import com.rainng.jerry.mouse.http.map.HttpSessionMap;

public class HttpContext {
    private final HttpRequest request;
    private final HttpResponse response;
    private final HttpCookieMap cookies;
    private HttpSessionMap session;

    public HttpContext() {
        request = new HttpRequest();
        response = new HttpResponse();
        cookies = new HttpCookieMap();

        request.setHttpContext(this);
        response.setHttpContext(this);
    }

    public HttpRequest getRequest() {
        return request;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public HttpCookieMap getCookies() {
        return cookies;
    }

    public HttpSessionMap getSession() {
        return session;
    }

    public void setSession(HttpSessionMap session) {
        this.session = session;
    }
}
