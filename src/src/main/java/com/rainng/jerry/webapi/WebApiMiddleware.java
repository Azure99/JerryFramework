package com.rainng.jerry.webapi;

import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.HttpRequest;
import com.rainng.jerry.mouse.http.HttpResponse;
import com.rainng.jerry.mouse.http.constant.HttpStatusCode;
import com.rainng.jerry.mouse.http.constant.RequestMethod;
import com.rainng.jerry.mouse.middleware.BaseMiddleware;
import com.rainng.jerry.webapi.annotation.Get;
import com.rainng.jerry.webapi.annotation.Post;
import com.rainng.jerry.webapi.annotation.Route;
import com.rainng.jerry.webapi.exception.UnsupportedTypeException;
import com.rainng.jerry.webapi.result.ActionContext;
import com.rainng.jerry.webapi.result.IResult;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class WebApiMiddleware extends BaseMiddleware {
    private static final String CONTROLLER_SUFFIX = "controller";
    private Map<RequestKey, TargetMethod> requestMap = new HashMap<>();

    public WebApiMiddleware(Class<? extends BaseController>[] controllers) {
        initPathMap(controllers);
    }

    private void initPathMap(Class<? extends BaseController>[] controllers) {
        for (Class controller : controllers) {
            addController(controller);
        }
    }

    private void addController(Class<? extends BaseController> controller) {
        String routePath = getControllerChainRoutePath(controller);

        Method[] methods = controller.getMethods();
        for (Method method : methods) {
            if (IResult.class.isAssignableFrom(method.getReturnType())) {
                addMethod(controller, routePath, method);
            }
        }
    }

    private void addMethod(Class<? extends BaseController> controller, String controllerRoutePath, Method method) {
        String routePath = getRoutePath(method);
        if (!routePath.startsWith("/")) {
            routePath = controllerRoutePath + routePath;
        }

        Parameter[] parameters = method.getParameters();
        String[] parameterNames = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            parameterNames[i] = parameters[i].getName().toLowerCase();
        }
        Arrays.sort(parameterNames);

        RequestMethod requestMethod = getSupportedRequestMethod(method);

        RequestKey requestKey = new RequestKey(routePath, requestMethod, parameterNames);
        TargetMethod targetMethod = new TargetMethod(controller, method);

        requestMap.put(requestKey, targetMethod);
    }

    private RequestMethod getSupportedRequestMethod(Method method) {
        RequestMethod requestMethod = RequestMethod.ANY;

        boolean getExist = method.getAnnotation(Get.class) != null;
        boolean postExist = method.getAnnotation(Post.class) != null;

        if ((getExist && postExist) || (!getExist && !postExist)) {
            return RequestMethod.ANY;
        }

        return getExist ? RequestMethod.GET : RequestMethod.POST;
    }

    /**
     * 获取整个控制器链的路由路径
     *
     * @param controller 控制器
     * @return 此控制器及其所有基控制器的路由路径
     */
    private String getControllerChainRoutePath(Class<? extends BaseController> controller) {
        String routePath = "";

        Class nowController = controller;
        while (!routePath.startsWith("/") &&
                nowController != null &&
                BaseController.class.isAssignableFrom(nowController)) {

            String nowPath = getRoutePath(nowController);
            if (!nowPath.endsWith("/")) {
                nowPath += '/';
            }

            routePath = nowPath + routePath;
            nowController = nowController.getSuperclass();
        }

        return routePath;
    }

    /**
     * 获取仅限定于控制器的路由路径
     *
     * @param controller 控制器
     * @return 路由路径
     */
    private String getRoutePath(Class<? extends BaseController> controller) {
        Route controllerRoute = controller.getAnnotation(Route.class);

        if (controllerRoute == null || controllerRoute.value().equals("")) {
            String controllerPath = controller.getSimpleName().toLowerCase();

            int lastPos = controllerPath.lastIndexOf(CONTROLLER_SUFFIX);
            if (lastPos != -1 && lastPos + CONTROLLER_SUFFIX.length() == controllerPath.length()) {
                controllerPath = controllerPath.substring(0, lastPos);
            }

            return controllerPath;

        }

        return controllerRoute.value().toLowerCase();
    }

    /**
     * 获取路由路径
     *
     * @param method 方法
     * @return 获取此方法的路由路径
     */
    private String getRoutePath(Method method) {
        Route methodRoute = method.getAnnotation(Route.class);

        if (methodRoute == null || methodRoute.value().equals("")) {
            return method.getName().toLowerCase();
        }

        return methodRoute.value().toLowerCase();
    }

    @Override
    public void onExecute(HttpContext context) throws Exception {
        HttpRequest request = context.getRequest();
        HttpResponse response = context.getResponse();

        RequestKey requestKey = getRequestKey(request);
        if (!requestMap.containsKey(requestKey)) {
            response.setStatusCode(HttpStatusCode.HTTP_NOT_FOUND);
            next(context);
            return;
        }

        TargetMethod targetMethod = requestMap.get(requestKey);
        Object[] argValues = getArgValues(request, targetMethod);

        Constructor<?> controllerConstructor = targetMethod.getController().getDeclaredConstructor();
        controllerConstructor.setAccessible(true);
        BaseController controller = (BaseController) controllerConstructor.newInstance();
        controller.setHttpContext(context);

        Method method = targetMethod.getMethod();
        method.setAccessible(true);
        IResult result = (IResult) method.invoke(controller, argValues);
        result.executeResult(new ActionContext(context));
    }


    private RequestKey getRequestKey(HttpRequest request) {
        List<String> argList = new ArrayList<>();
        for (String arg : request.getQueryArgs().keySet()) {
            argList.add(arg);
        }

        for (String arg : request.getForm().keySet()) {
            argList.add(arg);
        }

        String[] args = new String[argList.size()];
        argList.toArray(args);

        return new RequestKey(request.getResourcePath(), request.getMethod(), args);
    }

    private Object[] getArgValues(HttpRequest request, TargetMethod targetMethod) throws UnsupportedTypeException {
        Method method = targetMethod.getMethod();
        Parameter[] parameters = method.getParameters();
        int argsLen = method.getParameters().length;
        Object[] argValues = new Object[argsLen];

        Map<String, String> argsMap = new HashMap<>();
        Map<String, String> queryMap = request.getQueryArgs();
        Map<String, String> formMap = request.getForm();
        for (String key : queryMap.keySet()) {
            argsMap.put(key.toLowerCase(), queryMap.get(key));
        }
        for (String key : formMap.keySet()) {
            argsMap.put(key.toLowerCase(), formMap.get(key));
        }

        for (int i = 0; i < argsLen; i++) {
            String value = argsMap.get(parameters[i].getName().toLowerCase());
            argValues[i] = getParameterValue(parameters[i], value);
        }

        return argValues;
    }

    private Object getParameterValue(Parameter parameter, String strValue) throws UnsupportedTypeException {
        Object value = strValue;

        Class<?> type = parameter.getType();
        if (type.equals(String.class)) {
            return strValue;
        } else if (type.equals(Integer.class)) {
            return Integer.valueOf(strValue);
        } else if (type.equals(Long.class)) {
            return Long.valueOf(strValue);
        } else if (type.equals(Float.class)) {
            return Float.valueOf(strValue);
        } else if (type.equals(Double.class)) {
            return Double.valueOf(strValue);
        } else if (type.equals(Byte.class)) {
            return Byte.valueOf(strValue);
        } else if (type.equals(Character.class)) {
            return Character.valueOf(strValue.charAt(0));
        } else if (type.equals(Boolean.class)) {
            return Boolean.valueOf(strValue);
        } else if (type.equals(Short.class)) {
            return Short.valueOf(strValue);
        }

        throw new UnsupportedTypeException(type.getName());
    }
}
