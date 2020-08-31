package com.rainng.jerry.mvc;

import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.HttpRequest;
import com.rainng.jerry.mouse.http.HttpResponse;
import com.rainng.jerry.mouse.http.constant.HttpContentType;
import com.rainng.jerry.mouse.http.constant.HttpStatusCode;
import com.rainng.jerry.mouse.middleware.BaseMiddleware;
import com.rainng.jerry.mvc.mapping.*;
import com.rainng.jerry.mvc.result.ActionContext;
import com.rainng.jerry.mvc.result.JsonResult;
import com.rainng.jerry.mvc.result.Result;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

public class MvcMiddleware extends BaseMiddleware {
    private static final String BODY_PLACEHOLDER = "___body___";
    private final Map<RequestKey, RequestTarget> requestMap;

    public MvcMiddleware(Class<?> appClass) {
        requestMap = RequestMapResolver.resolve(appClass);
    }

    @Override
    public void onExecute(HttpContext context) throws Exception {
        HttpRequest request = context.getRequest();
        HttpResponse response = context.getResponse();

        if (request.getContentType().equals(HttpContentType.JSON)) {
            request.getForm().set(BODY_PLACEHOLDER, request.getBodyString());
        }

        RequestKey requestKey = RequestMapResolver.getRequestKey(request);
        if (!requestMap.containsKey(requestKey)) {
            response.setStatusCode(HttpStatusCode.HTTP_NOT_FOUND);
            next(context);
            return;
        }

        RequestTarget requestTarget = requestMap.get(requestKey);
        Object[] argValues = RequestMapResolver.getArgValues(request, requestTarget);

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
