package com.rainng.jerry.mvc.mapping;

import java.util.Arrays;

public class RequestKey {
    private final String path;
    private final HttpMethodMask methodMask;
    private final String[] parameters;

    private int hashCode = 0;
    private boolean hashCodeCached = false;

    public RequestKey(String path, HttpMethodMask methodMask, String[] parameters) {
        this.path = path.toLowerCase();
        this.methodMask = methodMask;
        this.parameters = parameters;
        sortParameters();
    }

    private void sortParameters() {
        Arrays.sort(parameters);

        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = parameters[i].toLowerCase();
        }
    }

    public String getPath() {
        return path;
    }

    public HttpMethodMask getMethodMask() {
        return methodMask;
    }

    public String[] getParameters() {
        return parameters;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof RequestKey)) {
            return false;
        }

        RequestKey other = (RequestKey) obj;
        if (!path.equals(other.path)) {
            return false;
        }

        if (!other.methodMask.containsAll(methodMask)) {
            return false;
        }

        String[] otherParameters = other.parameters;

        for (int i = 0; i < parameters.length; i++) {
            if (!parameters[i].equals(otherParameters[i])) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        if (hashCodeCached) {
            return hashCode;
        }

        return cacheHashCode();
    }

    private synchronized int cacheHashCode() {
        hashCode = Arrays.hashCode(parameters);
        hashCode = hashCode * 31 + path.hashCode();
        hashCodeCached = true;

        return hashCode;
    }
}
