package com.rainng.jerry.mvc.mapping;

import com.rainng.jerry.mouse.http.constant.HttpMethod;

public class HttpMethodMask {
    private int value;

    public HttpMethodMask add(HttpMethod httpMethod) {
        int maskValue = MaskValue.valueOf(httpMethod.name()).value;
        value = value | maskValue;
        return this;
    }

    public HttpMethodMask remove(HttpMethod httpMethod) {
        int maskValue = MaskValue.valueOf(httpMethod.name()).value;
        if (contains(httpMethod)) {
            value = value ^ maskValue;
        }
        return this;
    }

    public boolean contains(HttpMethod httpMethod) {
        int maskValue = MaskValue.valueOf(httpMethod.name()).value;
        return (value & maskValue) == maskValue;
    }

    public boolean containsAll(HttpMethodMask methodMask) {
        return (value & methodMask.value) == methodMask.value;
    }

    public int getValue() {
        return value;
    }

    public static HttpMethodMask fromString(String method) {
        return new HttpMethodMask().add(HttpMethod.valueOf(method.toUpperCase()));
    }

    public static HttpMethodMask allMethodMask() {
        HttpMethodMask mask = new HttpMethodMask();
        mask.value = MaskValue.ALL.value;
        return mask;
    }

    private enum MaskValue {
        GET(1),
        POST(2),
        PUT(4),
        DELETE(8),
        PATCH(16),
        HEAD(32),
        OPTIONS(64),
        ALL(127);
        private final int value;

        MaskValue(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
