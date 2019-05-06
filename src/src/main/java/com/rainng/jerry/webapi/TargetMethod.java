package com.rainng.jerry.webapi;

import java.lang.reflect.Method;

public class TargetMethod {
    private Class<? extends BaseController> controller;
    private Method method;

    public TargetMethod(Class<? extends BaseController> controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public Class<? extends BaseController> getController() {
        return controller;
    }

    public Method getMethod() {
        return method;
    }
}
