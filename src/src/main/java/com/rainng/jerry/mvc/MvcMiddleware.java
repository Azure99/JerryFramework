package com.rainng.jerry.mvc;

import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.HttpRequest;
import com.rainng.jerry.mouse.http.HttpResponse;
import com.rainng.jerry.mouse.http.constant.HttpContentType;
import com.rainng.jerry.mouse.http.constant.HttpStatusCode;
import com.rainng.jerry.mouse.http.constant.RequestMethod;
import com.rainng.jerry.mouse.middleware.BaseMiddleware;
import com.rainng.jerry.mvc.mapping.RequestKey;
import com.rainng.jerry.mvc.mapping.RequestTarget;
import com.rainng.jerry.mvc.mapping.RouteParser;
import com.rainng.jerry.mvc.result.ActionContext;
import com.rainng.jerry.mvc.result.IResult;
import com.rainng.jerry.mvc.result.JsonResult;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MvcMiddleware extends BaseMiddleware {
    private RouteParser parser = RouteParser.getInstance();
    private Map<RequestKey, RequestTarget> requestMap = new HashMap<>();

    public MvcMiddleware(Class<?> appClass) {
        initPathMap(ControllerScanner.scan(appClass));
    }

    private void initPathMap(Class<?>[] controllers) {
        for (Class<?> controller : controllers) {
            addController((Class<? extends Controller>) controller);
        }
    }

    private void addController(Class<? extends Controller> controller) {
        String routePath = parser.getControllerChainRoutePath(controller);

        Method[] methods = controller.getDeclaredMethods();
        for (Method method : methods) {
            // public
            if (((method.getModifiers() & 1) == 1)) {
                addMethod(controller, routePath, method);
            }
        }
    }

    private void addMethod(Class<? extends Controller> controller, String controllerRoutePath, Method method) {
        String routePath = parser.getRoutePath(method);
        if (!routePath.startsWith("/")) {
            routePath = controllerRoutePath + routePath;
        }

        String[] parameterNames = parser.getParameterNames(method);
        Arrays.sort(parameterNames);

        RequestMethod requestMethod = parser.getSupportedRequestMethod(method);

        RequestKey requestKey = new RequestKey(routePath, requestMethod, parameterNames);
        RequestTarget requestTarget = new RequestTarget(controller, method);

        requestMap.put(requestKey, requestTarget);
    }

    @Override
    public void onExecute(HttpContext context) throws Exception {
        HttpRequest request = context.getRequest();
        HttpResponse response = context.getResponse();

        if (request.getContentType().equals(HttpContentType.JSON)) {
            request.getForm().set("__json__", request.getBodyString());
        }

        RequestKey requestKey = parser.getRequestKey(request);
        if (!requestMap.containsKey(requestKey)) {
            response.setStatusCode(HttpStatusCode.HTTP_NOT_FOUND);
            next(context);
            return;
        }

        RequestTarget requestTarget = requestMap.get(requestKey);
        Object[] argValues = parser.getArgValues(request, requestTarget);

        Constructor<?> controllerConstructor = requestTarget.getController().getDeclaredConstructor();
        controllerConstructor.setAccessible(true);
        Controller controller = (Controller) controllerConstructor.newInstance();
        controller.setHttpContext(context);
        controller.setActionContext(new ActionContext(context));

        Method method = requestTarget.getMethod();
        method.setAccessible(true);

        IResult result = controller.beforeExecute(context, method, argValues);

        if (result == null) {
            if (IResult.class.isAssignableFrom(method.getReturnType())) {
                result = (IResult) method.invoke(controller, argValues);
            } else {
                result = new JsonResult(method.invoke(controller, argValues));
            }
        }

        IResult tempResult = controller.afterExecute(context, method, argValues, result);
        if (tempResult != null) {
            result = tempResult;
        }

        result.executeResult(controller.getActionContext());

        next(context);
    }
}
