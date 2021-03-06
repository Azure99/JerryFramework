package com.rainng.jerry.mvc.result;

import com.rainng.jerry.mouse.http.HttpContext;

import java.util.LinkedHashMap;
import java.util.Map;

public class ActionContext {
    private final Map<String, Object> modelMap = new LinkedHashMap<>();
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

    public Map<String, Object> getModelMap() {
        return modelMap;
    }
}
