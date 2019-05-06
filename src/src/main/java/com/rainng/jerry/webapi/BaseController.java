package com.rainng.jerry.webapi;

import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.webapi.annotation.Route;
import com.rainng.jerry.webapi.result.HtmlResult;
import com.rainng.jerry.webapi.result.IResult;
import com.rainng.jerry.webapi.result.RedirectResult;
import com.rainng.jerry.webapi.result.ValueResult;

@Route("/")
public class BaseController {
    private HttpContext httpContext;

    public BaseController() {
    }

    public HttpContext getHttpContext() {
        return httpContext;
    }

    public void setHttpContext(HttpContext httpContext) {
        this.httpContext = httpContext;
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
}
