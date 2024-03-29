package com.rainng.jerry.mvc.mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainng.jerry.mouse.http.HttpRequest;
import com.rainng.jerry.mouse.http.constant.HttpMethod;
import com.rainng.jerry.mvc.Controller;
import com.rainng.jerry.mvc.annotation.*;
import com.rainng.jerry.util.Logger;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class RequestMapResolver {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String REQUEST_BODY = "___body___";
    private static final String[] HTTP_METHODS = new String[]{"get", "post", "delete", "put", "patch"};

    private RequestMapResolver() {

    }

    @SuppressWarnings("unchecked")
    public static Map<RequestKey, RequestTarget> resolveMap(Class<?> appClass) {
        Map<RequestKey, RequestTarget> requestMap = new HashMap<>();

        Class<?>[] controllers = ControllerScanner.scan(appClass);
        for (Class<?> controller : controllers) {
            addController(requestMap, (Class<? extends Controller>) controller);
        }

        return requestMap;
    }

    private static void addController(Map<RequestKey, RequestTarget> requestMap, Class<? extends Controller> controller) {
        String controllerPath = RouteResolver.resolveFullControllerPath(controller);

        Method[] methods = controller.getDeclaredMethods();
        for (Method method : methods) {
            // public only
            if (((method.getModifiers() & 1) == 1)) {
                boolean enableMethodMapping = controller.getAnnotation(HttpMethodMapping.class) != null;
                addMethod(requestMap, controller, controllerPath, method, enableMethodMapping);
            }
        }
    }

    private static void addMethod(Map<RequestKey, RequestTarget> requestMap, Class<? extends Controller> controller,
                                  String controllerPath, Method method, boolean enableMethodMapping) {

        HttpMethodMask methodMask = scanHttpMethodMask(method);
        String routePath = RouteResolver.resolveMethodPath(method);

        if (enableMethodMapping) {
            for (String httpMethod : HTTP_METHODS) {
                if (routePath.startsWith(httpMethod)) {
                    routePath = routePath.substring(httpMethod.length());
                    methodMask = HttpMethodMask.fromString(httpMethod);
                    break;
                }
            }
        }

        if ("".equals(routePath) && controllerPath.endsWith("/")) {
            routePath = controllerPath.substring(0, controllerPath.length() - 1);
        }

        if (!routePath.startsWith("/")) {
            routePath = controllerPath + routePath;
        }

        String[] parameterNames = getParameterNamesForKey(method);
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

    public static HttpMethodMask scanHttpMethodMask(Method method) {
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

    public static RequestKey resolveRequestKey(HttpRequest request) {
        List<String> argList = new ArrayList<>();
        argList.addAll(request.getQueryArgs().keySet());
        argList.addAll(request.getForm().keySet());

        String[] args = new String[argList.size()];
        argList.toArray(args);

        HttpMethodMask methodMask = new HttpMethodMask().add(request.getMethod());
        return new RequestKey(request.getPath(), methodMask, args);
    }

    public static Object[] getArgValues(HttpRequest request, RequestTarget requestTarget) {
        Method method = requestTarget.getMethod();
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = getParameterNamesForCall(method);
        int argsLen = parameterNames.length;
        Object[] argValues = new Object[argsLen];

        Map<String, String> argsMap = new HashMap<>();
        request.getQueryArgs().forEach((key, value) -> argsMap.put(key.toLowerCase(), value));
        request.getForm().forEach((key, value) -> argsMap.put(key.toLowerCase(), value));

        for (int i = 0; i < argsLen; i++) {
            if (REQUEST_BODY.equals(parameterNames[i])) {
                argValues[i] = parseBodyValue(parameters[i], request);
            } else {
                String strValue = argsMap.get(parameterNames[i].toLowerCase());
                argValues[i] = parseParameterValue(parameters[i], strValue);
            }
        }

        return argValues;
    }

    public static String[] getParameterNamesForKey(Method method) {
        Parameter[] parameters = method.getParameters();

        List<String> parameterNames = new ArrayList<>(parameters.length);
        for (Parameter parameter : parameters) {
            if (parameter.getAnnotation(RequestBody.class) != null) {
                continue;
            }
            parameterNames.add(parameter.getName().toLowerCase());
        }

        return parameterNames.toArray(new String[0]);
    }

    public static String[] getParameterNamesForCall(Method method) {
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getAnnotation(RequestBody.class) != null) {
                parameterNames[i] = REQUEST_BODY;
                continue;
            }
            parameterNames[i] = parameters[i].getName().toLowerCase();
        }

        return parameterNames;
    }

    public static Object parseParameterValue(Parameter parameter, String strValue) {
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
        } else {
            return strValue;
        }
    }

    public static Object parseBodyValue(Parameter parameter, HttpRequest request) {
        if (parameter.getType().equals(byte[].class)) {
            return request.getBody();
        }

        try {
            return objectMapper.readValue(request.getBodyString(), parameter.getType());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
