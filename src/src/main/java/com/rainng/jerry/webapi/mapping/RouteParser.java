package com.rainng.jerry.webapi.mapping;

import com.rainng.jerry.mouse.http.HttpRequest;
import com.rainng.jerry.mouse.http.constant.RequestMethod;
import com.rainng.jerry.webapi.Controller;
import com.rainng.jerry.webapi.annotation.Get;
import com.rainng.jerry.webapi.annotation.Post;
import com.rainng.jerry.webapi.annotation.Route;
import com.rainng.jerry.webapi.exception.UnsupportedTypeException;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteParser {
    private static RouteParser instance;
    public static RouteParser getInstance() {
        return instance;
    }

    static {
        instance = new RouteParser();
    }

    private static final String CONTROLLER_SUFFIX = "controller";

    private RouteParser() {

    }

    /**
     * 获取受支持的Http request method
     * @param method 调用的method
     * @return 受支持的Http request method (GET / POST)
     */
    public RequestMethod getSupportedRequestMethod(Method method) {
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
    public String getControllerChainRoutePath(Class<? extends Controller> controller) {
        String routePath = "";

        Class nowController = controller;
        while (!routePath.startsWith("/") &&
                nowController != null &&
                Controller.class.isAssignableFrom(nowController)) {

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
    public String getRoutePath(Class<? extends Controller> controller) {
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
    public String getRoutePath(Method method) {
        Route methodRoute = method.getAnnotation(Route.class);

        if (methodRoute == null || methodRoute.value().equals("")) {
            return method.getName().toLowerCase();
        }

        return methodRoute.value().toLowerCase();
    }

    /**
     * 从Request中解析Request Key
     * @param request Http request
     * @return Request Key
     */
    public RequestKey getRequestKey(HttpRequest request) {
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

    /**
     * 从Request获取调用Target method所需要的参数
     * @param request Http request
     * @param requestTarget Request target
     * @return 参数数组
     * @throws UnsupportedTypeException
     */
    public Object[] getArgValues(HttpRequest request, RequestTarget requestTarget) throws UnsupportedTypeException {
        Method method = requestTarget.getMethod();
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

    /**
     * 将字符串转换为对应类型的值
     * @param parameter 参数
     * @param strValue 字符串值
     * @return 对应类型的值
     * @throws UnsupportedTypeException
     */
    public Object getParameterValue(Parameter parameter, String strValue) throws UnsupportedTypeException {
        if (strValue == null) {
            return null;
        }

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
        } else {
            return null;
        }
    }
}
