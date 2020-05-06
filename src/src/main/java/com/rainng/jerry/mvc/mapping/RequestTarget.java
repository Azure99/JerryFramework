package com.rainng.jerry.mvc.mapping;

import com.rainng.jerry.mvc.Controller;

import java.lang.reflect.Method;

public class RequestTarget {
    private Class<? extends Controller> controller;
    private Method method;

    public RequestTarget(Class<? extends Controller> controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public Class<? extends Controller> getController() {
        return controller;
    }

    public Method getMethod() {
        return method;
    }
}
