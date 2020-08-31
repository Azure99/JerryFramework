package com.rainng.jerry.mvc.mapping;

import com.rainng.jerry.mvc.Controller;
import com.rainng.jerry.mvc.annotation.Route;

import java.lang.reflect.Method;

public class RouteResolver {
    private static final String CONTROLLER_SUFFIX = "controller";

    private RouteResolver() {

    }

    @SuppressWarnings("unchecked")
    public static String getFullControllerPath(Class<? extends Controller> controller) {
        String routePath = "";

        Class<?> nowController = controller;
        while (!routePath.startsWith("/") && nowController != null &&
                Controller.class.isAssignableFrom(nowController)) {

            String nowPath = getControllerPath((Class<? extends Controller>) nowController);
            if (!nowPath.endsWith("/")) {
                nowPath += '/';
            }

            routePath = nowPath + routePath;
            nowController = nowController.getSuperclass();
        }

        return routePath;
    }

    public static String getControllerPath(Class<? extends Controller> controller) {
        Route controllerRoute = controller.getAnnotation(Route.class);

        if (controllerRoute == null || "".equals(controllerRoute.value())) {
            String controllerPath = controller.getSimpleName().toLowerCase();

            int lastPos = controllerPath.lastIndexOf(CONTROLLER_SUFFIX);
            if (lastPos != -1 && lastPos + CONTROLLER_SUFFIX.length() == controllerPath.length()) {
                controllerPath = controllerPath.substring(0, lastPos);
            }

            return controllerPath;
        }

        return controllerRoute.value().toLowerCase();
    }

    public static String getMethodPath(Method method) {
        Route methodRoute = method.getAnnotation(Route.class);

        if (methodRoute == null || "".equals(methodRoute.value())) {
            return method.getName().toLowerCase();
        }

        return methodRoute.value().toLowerCase();
    }
}
