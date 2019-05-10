package com.rainng.jerry.mouse.http;

import com.rainng.jerry.mouse.http.map.HttpCookieMap;

public class HttpContext {
    private HttpRequest request;
    private HttpResponse response;
    private HttpCookieMap cookies;
    private Session session;

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

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
