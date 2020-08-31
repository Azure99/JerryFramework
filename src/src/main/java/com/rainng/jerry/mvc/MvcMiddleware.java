package com.rainng.jerry.mvc;

import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.HttpRequest;
import com.rainng.jerry.mouse.http.HttpResponse;
import com.rainng.jerry.mouse.http.constant.HttpContentType;
import com.rainng.jerry.mouse.http.constant.HttpStatusCode;
import com.rainng.jerry.mouse.middleware.BaseMiddleware;
import com.rainng.jerry.mvc.annotation.HttpMethodMapping;
import com.rainng.jerry.mvc.mapping.HttpMethodMask;
import com.rainng.jerry.mvc.mapping.RequestKey;
import com.rainng.jerry.mvc.mapping.RequestTarget;
import com.rainng.jerry.mvc.mapping.RouteParser;
import com.rainng.jerry.mvc.result.ActionContext;
import com.rainng.jerry.mvc.result.JsonResult;
import com.rainng.jerry.mvc.result.Result;
import com.rainng.jerry.util.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MvcMiddleware extends BaseMiddleware {
    private static final String BODY_PLACEHOLDER = "___body___";
    private static final String[] HTTP_METHODS = new String[]{"get", "post", "delete", "put", "patch"};

    private final Map<RequestKey, RequestTarget> requestMap = new HashMap<>();

    public MvcMiddleware(Class<?> appClass) {
        initPathMap(ControllerScanner.scan(appClass));
    }

    @SuppressWarnings("unchecked")
    private void initPathMap(Class<?>[] controllers) {
        for (Class<?> controller : controllers) {
            addController((Class<? extends Controller>) controller);
        }
    }

    private void addController(Class<? extends Controller> controller) {
        String routePath = RouteParser.getControllerChainRoutePath(controller);

        Method[] methods = controller.getDeclaredMethods();
        for (Method method : methods) {
            // public only
            if (((method.getModifiers() & 1) == 1)) {
                boolean enableMethodMapping = controller.getAnnotation(HttpMethodMapping.class) != null;
                addMethod(controller, routePath, method, enableMethodMapping);
            }
        }
    }

    private void addMethod(Class<? extends Controller> controller, String controllerRoutePath
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

    @Override
    public void onExecute(HttpContext context) throws Exception {
        HttpRequest request = context.getRequest();
        HttpResponse response = context.getResponse();

        if (request.getContentType().equals(HttpContentType.JSON)) {
            request.getForm().set(BODY_PLACEHOLDER, request.getBodyString());
        }

        RequestKey requestKey = RouteParser.getRequestKey(request);
        if (!requestMap.containsKey(requestKey)) {
            response.setStatusCode(HttpStatusCode.HTTP_NOT_FOUND);
            next(context);
            return;
        }

        RequestTarget requestTarget = requestMap.get(requestKey);
        Object[] argValues = RouteParser.getArgValues(request, requestTarget);

        Constructor<?> controllerConstructor = requestTarget.getController().getDeclaredConstructor();
        controllerConstructor.setAccessible(true);
        Controller controller = (Controller) controllerConstructor.newInstance();
        controller.setHttpContext(context);
        controller.setActionContext(new ActionContext(context));

        Method method = requestTarget.getMethod();
        method.setAccessible(true);

        Result result = controller.beforeExecute(context, method, argValues);

        if (result == null) {
            if (Result.class.isAssignableFrom(method.getReturnType())) {
                result = (Result) method.invoke(controller, argValues);
            } else {
                result = new JsonResult(method.invoke(controller, argValues));
            }
        }

        Result tempResult = controller.afterExecute(context, method, argValues, result);
        if (tempResult != null) {
            result = tempResult;
        }

        result.executeResult(controller.getActionContext());

        next(context);
    }
}
