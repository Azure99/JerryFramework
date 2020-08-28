package com.rainng.jerry.mvc.mapping;

import com.rainng.jerry.mouse.http.constant.RequestMethod;

import java.util.Arrays;

public class RequestKey {
    private final String path;
    private final RequestMethod requestMethod;
    private final String[] parameters;

    private int hashCode = 0;
    private boolean hashCodeCached = false;

    public RequestKey(String path, RequestMethod requestMethod, String[] parameters) {
        this.path = path.toLowerCase();
        this.requestMethod = requestMethod;
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

    public RequestMethod getRequestMethod() {
        return requestMethod;
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

        if (requestMethod != other.requestMethod && other.requestMethod != RequestMethod.ANY) {
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
