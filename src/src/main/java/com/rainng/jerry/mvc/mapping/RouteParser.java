package com.rainng.jerry.mvc.mapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rainng.jerry.mouse.http.HttpRequest;
import com.rainng.jerry.mouse.http.constant.HttpMethod;
import com.rainng.jerry.mvc.Controller;
import com.rainng.jerry.mvc.annotation.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteParser {
    private static final String JSON_PLACEHOLDER = "___json___";
    private static final String CONTROLLER_SUFFIX = "controller";
    private static final RouteParser instance = new RouteParser();

    private RouteParser() {

    }

    public static RouteParser getInstance() {
        return instance;
    }

    /**
     * 获取受支持的Http request method标志
     *
     * @param method 调用的method
     * @return 受支持的Http request method标志
     */
    public HttpMethodMask scanHttpMethodMask(Method method) {
        boolean getExist = method.getAnnotation(HttpGet.class) != null;
        boolean postExist = method.getAnnotation(HttpPost.class) != null;
        boolean patchExist = method.getAnnotation(HttpPatch.class) != null;
        boolean putExist = method.getAnnotation(HttpPut.class) != null;
        boolean deleteExist = method.getAnnotation(HttpDelete.class) != null;

        if (getExist && postExist && patchExist && putExist && deleteExist) {
            return HttpMethodMask.allMethodMask();
        }

        if (!(getExist || postExist || patchExist || putExist || deleteExist)) {
            return HttpMethodMask.allMethodMask();
        }

        HttpMethodMask mask = new HttpMethodMask();
        if (getExist) {
            mask.add(HttpMethod.GET);
        }
        if (postExist) {
            mask.add(HttpMethod.POST);
        }
        if (patchExist) {
            mask.add(HttpMethod.PATCH);
        }
        if (putExist) {
            mask.add(HttpMethod.PUT);
        }
        if (deleteExist) {
            mask.add(HttpMethod.DELETE);
        }

        return mask;
    }

    /**
     * 获取整个控制器链的路由路径
     *
     * @param controller 控制器
     * @return 此控制器及其所有基控制器的路由路径
     */
    @SuppressWarnings("unchecked")
    public String getControllerChainRoutePath(Class<? extends Controller> controller) {
        String routePath = "";

        Class<?> nowController = controller;
        while (!routePath.startsWith("/") &&
                nowController != null &&
                Controller.class.isAssignableFrom(nowController)) {

            String nowPath = getRoutePath((Class<? extends Controller>) nowController);
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
    public String getRoutePath(Class<? extends Controller> controller) {
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

    /**
     * 获取路由路径
     *
     * @param method 方法
     * @return 获取此方法的路由路径
     */
    public String getRoutePath(Method method) {
        Route methodRoute = method.getAnnotation(Route.class);

        if (methodRoute == null || "".equals(methodRoute.value())) {
            return method.getName().toLowerCase();
        }

        return methodRoute.value().toLowerCase();
    }

    /**
     * 从Request中解析Request Key
     *
     * @param request Http request
     * @return Request Key
     */
    public RequestKey getRequestKey(HttpRequest request) {
        List<String> argList = new ArrayList<>();
        argList.addAll(request.getQueryArgs().keySet());

        argList.addAll(request.getForm().keySet());

        String[] args = new String[argList.size()];
        argList.toArray(args);

        HttpMethodMask methodMask = new HttpMethodMask().add(request.getMethod());

        return new RequestKey(request.getResourcePath(), methodMask, args);
    }

    /**
     * 从Request获取调用Target method所需要的参数
     *
     * @param request       Http request
     * @param requestTarget Request target
     * @return 参数数组
     */
    public Object[] getArgValues(HttpRequest request, RequestTarget requestTarget) {
        Method method = requestTarget.getMethod();
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = getParameterNames(method);
        int argsLen = parameterNames.length;
        Object[] argValues = new Object[argsLen];

        Map<String, String> argsMap = new HashMap<>();
        request.getQueryArgs().forEach((key, value) -> argsMap.put(key.toLowerCase(), value));
        request.getForm().forEach((key, value) -> argsMap.put(key.toLowerCase(), value));

        for (int i = 0; i < argsLen; i++) {
            String value = argsMap.get(parameterNames[i].toLowerCase());
            argValues[i] = getParameterValue(parameters[i], value);
        }

        return argValues;
    }

    public String[] getParameterNames(Method method) {
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getType().isAssignableFrom(JSONObject.class)) {
                parameterNames[i] = JSON_PLACEHOLDER;
                continue;
            }
            parameterNames[i] = parameters[i].getName().toLowerCase();
        }

        return parameterNames;
    }

    /**
     * 将字符串转换为对应类型的值
     *
     * @param parameter 参数
     * @param strValue  字符串值
     * @return 对应类型的值
     */
    public Object getParameterValue(Parameter parameter, String strValue) {
        if (strValue == null) {
            return null;
        }

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
            return strValue.charAt(0);
        } else if (type.equals(Boolean.class)) {
            return Boolean.valueOf(strValue);
        } else if (type.equals(Short.class)) {
            return Short.valueOf(strValue);
        } else if (type.equals(JSONObject.class)) {
            return JSON.parse(strValue);
        } else {
            return strValue;
        }
    }
}
