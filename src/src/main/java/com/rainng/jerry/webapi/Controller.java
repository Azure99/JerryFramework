package com.rainng.jerry.webapi;

import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.HttpRequest;
import com.rainng.jerry.mouse.http.HttpResponse;
import com.rainng.jerry.mouse.http.map.HttpCookieMap;
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
