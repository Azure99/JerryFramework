package com.rainng.jerry.mouse.http.constant;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    PATCH("PATCH"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS");
    private final String value;

    HttpMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}