package com.rainng.jerry.mouse.http.constant;

public class HttpStatusCode {
    public static final int HTTP_OK = 200;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_INTERNAL_SERVER_ERROR = 500;

    public static String toString(int code) {
        switch (code) {
            case HttpStatusCode.HTTP_OK:
                return "200 OK";
            case HttpStatusCode.HTTP_NOT_FOUND:
                return "404 Not Found";
            case HttpStatusCode.HTTP_INTERNAL_SERVER_ERROR:
                return "500 Internal Server Error";
            default:
                return "";
        }
    }
}
