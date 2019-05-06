package com.rainng.jerry.webapi.result;

import com.rainng.jerry.mouse.http.HttpContext;

public class ActionContext {
    private HttpContext httpContext;

    public ActionContext(HttpContext httpContext) {
        this.httpContext = httpContext;
    }

    public HttpContext getHttpContext() {
        return httpContext;
    }

    public void setHttpContext(HttpContext httpContext) {
        this.httpContext = httpContext;
    }
}
