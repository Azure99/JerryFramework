package com.rainng.jerry.webapi;

import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.HttpRequest;
import com.rainng.jerry.mouse.http.HttpResponse;
import com.rainng.jerry.mouse.http.constant.HttpStatusCode;
import com.rainng.jerry.mouse.http.constant.RequestMethod;
import com.rainng.jerry.mouse.middleware.BaseMiddleware;
import com.rainng.jerry.webapi.mapping.RequestKey;
import com.rainng.jerry.webapi.mapping.RequestTarget;
import com.rainng.jerry.webapi.mapping.RouteParser;
import com.rainng.jerry.webapi.result.ActionContext;
import com.rainng.jerry.webapi.result.IResult;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WebApiMiddleware extends BaseMiddleware {
    private RouteParser parser = RouteParser.getInstance();
    private Map<RequestKey, RequestTarget> requestMap = new HashMap<>();

    public WebApiMiddleware(Class<? extends Controller>[] controllers) {
        initPathMap(controllers);
    }

    private void initPathMap(Class<? extends Controller>[] controllers) {
        for (Class controller : controllers) {
            addController(controller);
        }
    }

    private void addController(Class<? extends Controller> controller) {
        String routePath = parser.getControllerChainRoutePath(controller);

        Method[] methods = controller.getMethods();
        for (Method method : methods) {
            if (IResult.class.isAssignableFrom(method.getReturnType())) {
                addMethod(controller, routePath, method);
            }
        }
    }

    private void addMethod(Class<? extends Controller> controller, String controllerRoutePath, Method method) {
        String routePath = parser.getRoutePath(method);
        if (!routePath.startsWith("/")) {
            routePath = controllerRoutePath + routePath;
        }

        Parameter[] parameters = method.getParameters();
        String[] parameterNames = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            parameterNames[i] = parameters[i].getName().toLowerCase();
        }
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

        Method method = requestTarget.getMethod();
        method.setAccessible(true);

        IResult result = (IResult) method.invoke(controller, argValues);
        result.executeResult(new ActionContext(context));

        next(context);
    }
}
