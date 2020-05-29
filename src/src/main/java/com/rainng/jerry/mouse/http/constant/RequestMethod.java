package com.rainng.jerry.mouse.http.constant;

public enum RequestMethod {
    GET,
    POST,
    PUT,
    DELETE,
    PATCH,
    HEAD,
    OPTIONS,
    ANY;

    public static RequestMethod parse(String name) {
        switch (name) {
            case "get":
                return RequestMethod.GET;
            case "post":
                return RequestMethod.POST;
            case "put":
                return RequestMethod.PUT;
            case "delete":
                return RequestMethod.DELETE;
            case "patch":
                return RequestMethod.PATCH;
            case "head":
                return RequestMethod.HEAD;
            case "options":
                return RequestMethod.OPTIONS;
            default:
                return RequestMethod.ANY;
        }
    }
}
