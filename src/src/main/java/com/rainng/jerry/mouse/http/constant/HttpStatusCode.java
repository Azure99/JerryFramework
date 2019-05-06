package com.rainng.jerry.mouse.http.constant;

public class HttpStatusCode {
    public static final int HTTP_OK = 200;
    public static final int HTTP_MOVED_PERMANENTLY = 301;
    public static final int HTTP_MOVED_TEMPORARILY  = 302;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_INTERNAL_SERVER_ERROR = 500;

    public static String toString(int code) {
        switch (code) {
            case HTTP_OK:
                return "200 OK";
            case HTTP_MOVED_PERMANENTLY:
                return "301 Moved Permanently";
            case HTTP_MOVED_TEMPORARILY:
                return "302 Moved Temporarily";
            case HTTP_NOT_FOUND:
                return "404 Not Found";
            case HTTP_INTERNAL_SERVER_ERROR:
                return "500 Internal Server Error";
            default:
                return "200 OK";
        }
    }
}
