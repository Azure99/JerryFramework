package com.rainng.jerry.mvc.mapping;

import com.rainng.jerry.mvc.Controller;
import com.rainng.jerry.mvc.annotation.HttpMethodMapping;
import com.rainng.jerry.util.Logger;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RequestMapResolver {
    private static final String[] HTTP_METHODS = new String[]{"get", "post", "delete", "put", "patch"};

    private RequestMapResolver() {

    }

    @SuppressWarnings("unchecked")
    public static Map<RequestKey, RequestTarget> resolve(Class<?> appClass) {
        Map<RequestKey, RequestTarget> requestMap = new HashMap<>();

        Class<?>[] controllers = ControllerScanner.scan(appClass);
        for (Class<?> controller : controllers) {
            addController(requestMap, (Class<? extends Controller>) controller);
        }
        return requestMap;
    }

    private static void addController(Map<RequestKey, RequestTarget> requestMap, Class<? extends Controller> controller) {
        String routePath = RouteParser.getControllerChainRoutePath(controller);

        Method[] methods = controller.getDeclaredMethods();
        for (Method method : methods) {
            // public only
            if (((method.getModifiers() & 1) == 1)) {
                boolean enableMethodMapping = controller.getAnnotation(HttpMethodMapping.class) != null;
                addMethod(requestMap, controller, routePath, method, enableMethodMapping);
            }
        }
    }

    private static void addMethod(Map<RequestKey, RequestTarget> requestMap, Class<? extends Controller> controller, String controllerRoutePath
            , Method method, boolean enableMethodMapping) {
        HttpMethodMask methodMask = RouteParser.scanHttpMethodMask(method);
        String routePath = RouteParser.getRoutePath(method);

        if (enableMethodMapping) {
            for (String httpMethod : HTTP_METHODS) {
                if (routePath.startsWith(httpMethod)) {
                    routePath = routePath.substring(httpMethod.length());
                    methodMask = HttpMethodMask.fromString(httpMethod);
                    break;
                }
            }
        }

        if ("".equals(routePath) && controllerRoutePath.endsWith("/")) {
            routePath = controllerRoutePath.substring(0, controllerRoutePath.length() - 1);
        }

        if (!routePath.startsWith("/")) {
            routePath = controllerRoutePath + routePath;
        }

        String[] parameterNames = RouteParser.getParameterNames(method);
        Arrays.sort(parameterNames);

        RequestKey requestKey = new RequestKey(routePath, methodMask, parameterNames);
        RequestTarget requestTarget = new RequestTarget(controller, method);

        if (requestMap.containsKey(requestKey)) {
            RequestTarget existTarget = requestMap.get(requestKey);
            String existInfo = existTarget.getController().getName() + "#" + existTarget.getMethod().getName();
            String newInfo = requestTarget.getController().getName() + "#" + requestTarget.getMethod().getName();
            Logger.warn("Duplicate request key between: " + existInfo + " and " + newInfo);
        }

        requestMap.put(requestKey, requestTarget);
    }
}
