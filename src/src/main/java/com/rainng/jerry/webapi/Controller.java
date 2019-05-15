package com.rainng.jerry.webapi;

import com.rainng.jerry.mouse.http.Cookie;
import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.HttpRequest;
import com.rainng.jerry.mouse.http.HttpResponse;
import com.rainng.jerry.mouse.http.map.HttpCookieMap;
import com.rainng.jerry.mouse.http.map.HttpSessionMap;
import com.rainng.jerry.webapi.annotation.Route;
import com.rainng.jerry.webapi.result.*;

@Route("/")
public class Controller {
    private HttpContext httpContext;

    public Controller() {
    }

    public HttpContext getHttpContext() {
        return httpContext;
    }

    public void setHttpContext(HttpContext httpContext) {
        this.httpContext = httpContext;
    }

    protected HttpRequest getRequest() {
        return httpContext.getRequest();
    }

    protected HttpResponse getResponse() {
        return httpContext.getResponse();
    }

    protected HttpCookieMap getCookies() {
        return getHttpContext().getCookies();
    }

    protected Cookie getCookie(String name) {
        return getCookies().get(name);
    }

    protected String getCookieValue(String name) {
        HttpCookieMap cookies = getCookies();
        if (!cookies.containsKey(name)) {
            return null;
        }

        Cookie cookie = getCookies().get(name);
        return cookie.getValue();
    }

    protected void setCookie(String name, Cookie cookie) {
        getCookies().set(name, cookie);
    }

    protected void setCookieValue(String name, String value) {
        getCookies().set(name, new Cookie(name, value));
    }

    protected boolean containsCookie(String name) {
        return getCookies().containsKey(name);
    }

    protected void removeCookie(String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        getCookies().set(name, cookie);
    }

    protected HttpSessionMap getSessions() {
        return getHttpContext().getSession();
    }

    protected Object getSession(String name) {
        return getSessions().get(name);
    }

    protected void setSession(String name, Object value) {
        getSessions().set(name, value);
    }

    protected boolean containsSession(String name) {
        return getSessions().containsKey(name);
    }

    protected void removeSession(String name) {
        if (containsSession(name)) {
            getSessions().remove(name);
        }
    }

    protected IResult value(Object obj) {
        return new ValueResult(obj);
    }

    protected IResult redirect(String url) {
        return new RedirectResult(url);
    }

    protected IResult html(String html) {
        return new HtmlResult(html);
    }

    protected IResult json(Object object) {
        return new JsonResult(object);
    }
}
