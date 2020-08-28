package com.rainng.jerry.mouse.http.constant;

import java.util.HashMap;
import java.util.Map;

public class HttpStatusCode {

    public static final int HTTP_CONTINUE = 100;
    public static final int HTTP_SWITCHING_PROTOCOLS = 101;
    public static final int HTTP_OK = 200;
    public static final int HTTP_CREATED = 201;
    public static final int HTTP_ACCEPTED = 202;
    public static final int HTTP_NON_AUTHORITATIVE_INFORMATION = 203;
    public static final int HTTP_NO_CONTENT = 204;
    public static final int HTTP_RESET_CONTENT = 205;
    public static final int HTTP_PARTIAL_CONTENT = 206;
    public static final int HTTP_MULTIPLE_CHOICES = 300;
    public static final int HTTP_MOVED_PERMANENTLY = 301;
    public static final int HTTP_FOUND = 302;
    public static final int HTTP_SEE_OTHER = 303;
    public static final int HTTP_NOT_MODIFIED = 304;
    public static final int HTTP_TEMPORARY_REDIRECT = 307;
    public static final int HTTP_PERMANENT_REDIRECT = 308;
    public static final int HTTP_BAD_REQUEST = 400;
    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int HTTP_FORBIDDEN = 403;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_METHOD_NOT_ALLOWED = 405;
    public static final int HTTP_NOT_ACCEPTABLE = 406;
    public static final int HTTP_PROXY_AUTHENTICATION_REQUIRED = 407;
    public static final int HTTP_REQUEST_TIMEOUT = 408;
    public static final int HTTP_CONFLICT = 409;
    public static final int HTTP_GONE = 410;
    public static final int HTTP_LENGTH_REQUIRED = 411;
    public static final int HTTP_PRECONDITION_FAILED = 412;
    public static final int HTTP_PAYLOAD_TOO_LARGE = 413;
    public static final int HTTP_URI_TOO_LONG = 414;
    public static final int HTTP_UNSUPPORTED_MEDIA_TYPE = 415;
    public static final int HTTP_RANGE_NOT_SATISFIABLE = 416;
    public static final int HTTP_EXPECTATION_FAILED = 417;
    public static final int HTTP_UNPROCESSABLE_ENTITY = 422;
    public static final int HTTP_TOO_EARLY = 425;
    public static final int HTTP_UPGRADE_REQUIRED = 426;
    public static final int HTTP_PRECONDITION_REQUIRED = 428;
    public static final int HTTP_TOO_MANY_REQUESTS = 429;
    public static final int HTTP_REQUEST_HEADER_FIELDS_TOO_LARGE = 431;
    public static final int HTTP_UNAVAILABLE_FOR_LEGAL_REASONS = 451;
    public static final int HTTP_INTERNAL_SERVER_ERROR = 500;
    public static final int HTTP_NOT_IMPLEMENTED = 501;
    public static final int HTTP_BAD_GATEWAY = 502;
    public static final int HTTP_SERVICE_UNAVAILABLE = 503;
    public static final int HTTP_GATEWAY_TIMEOUT = 504;
    public static final int HTTP_HTTP_VERSION_NOT_SUPPORTED = 505;
    public static final int HTTP_NETWORK_AUTHENTICATION_REQUIRED = 511;
    private static final Map<Integer, String> statusCodeMap = new HashMap<>();

    static {
        initStatusCodeMap();
    }

    private HttpStatusCode() {

    }

    private static void initStatusCodeMap() {
        statusCodeMap.put(HTTP_CONTINUE, "100 Continue");
        statusCodeMap.put(HTTP_SWITCHING_PROTOCOLS, "101 Switching Protocols");
        statusCodeMap.put(HTTP_OK, "200 OK");
        statusCodeMap.put(HTTP_CREATED, "201 Created");
        statusCodeMap.put(HTTP_ACCEPTED, "202 Accepted");
        statusCodeMap.put(HTTP_NON_AUTHORITATIVE_INFORMATION, "203 Non-Authoritative Information");
        statusCodeMap.put(HTTP_NO_CONTENT, "204 No Content");
        statusCodeMap.put(HTTP_RESET_CONTENT, "205 Reset Content");
        statusCodeMap.put(HTTP_PARTIAL_CONTENT, "206 Partial Content");
        statusCodeMap.put(HTTP_MULTIPLE_CHOICES, "300 Multiple Choices");
        statusCodeMap.put(HTTP_MOVED_PERMANENTLY, "301 Moved Permanently");
        statusCodeMap.put(HTTP_FOUND, "302 Found");
        statusCodeMap.put(HTTP_SEE_OTHER, "303 See Other");
        statusCodeMap.put(HTTP_NOT_MODIFIED, "304 Not Modified");
        statusCodeMap.put(HTTP_TEMPORARY_REDIRECT, "307 Temporary Redirect");
        statusCodeMap.put(HTTP_PERMANENT_REDIRECT, "308 Permanent Redirect");
        statusCodeMap.put(HTTP_BAD_REQUEST, "400 Bad Request");
        statusCodeMap.put(HTTP_UNAUTHORIZED, "401 Unauthorized");
        statusCodeMap.put(HTTP_FORBIDDEN, "403 Forbidden");
        statusCodeMap.put(HTTP_NOT_FOUND, "404 Not Found");
        statusCodeMap.put(HTTP_METHOD_NOT_ALLOWED, "405 Method Not Allowed");
        statusCodeMap.put(HTTP_NOT_ACCEPTABLE, "406 Not Acceptable");
        statusCodeMap.put(HTTP_PROXY_AUTHENTICATION_REQUIRED, "407 Proxy Authentication Required");
        statusCodeMap.put(HTTP_REQUEST_TIMEOUT, "408 Request Timeout");
        statusCodeMap.put(HTTP_CONFLICT, "409 Conflict");
        statusCodeMap.put(HTTP_GONE, "410 Gone");
        statusCodeMap.put(HTTP_LENGTH_REQUIRED, "411 Length Required");
        statusCodeMap.put(HTTP_PRECONDITION_FAILED, "412 Precondition Failed");
        statusCodeMap.put(HTTP_PAYLOAD_TOO_LARGE, "413 Payload Too Large");
        statusCodeMap.put(HTTP_URI_TOO_LONG, "414 URI Too Long");
        statusCodeMap.put(HTTP_UNSUPPORTED_MEDIA_TYPE, "415 Unsupported Media Type");
        statusCodeMap.put(HTTP_RANGE_NOT_SATISFIABLE, "416 Range Not Satisfiable");
        statusCodeMap.put(HTTP_EXPECTATION_FAILED, "417 Expectation Failed");
        statusCodeMap.put(HTTP_UNPROCESSABLE_ENTITY, "422 Unprocessable Entity");
        statusCodeMap.put(HTTP_TOO_EARLY, "425 Too Early");
        statusCodeMap.put(HTTP_UPGRADE_REQUIRED, "426 Upgrade Required");
        statusCodeMap.put(HTTP_PRECONDITION_REQUIRED, "428 Precondition Required");
        statusCodeMap.put(HTTP_TOO_MANY_REQUESTS, "429 Too Many Requests");
        statusCodeMap.put(HTTP_REQUEST_HEADER_FIELDS_TOO_LARGE, "431 Request Header Fields Too Large");
        statusCodeMap.put(HTTP_UNAVAILABLE_FOR_LEGAL_REASONS, "451 Unavailable For Legal Reasons");
        statusCodeMap.put(HTTP_NOT_IMPLEMENTED, "501 Not Implemented");
        statusCodeMap.put(HTTP_BAD_GATEWAY, "502 Bad Gateway");
        statusCodeMap.put(HTTP_SERVICE_UNAVAILABLE, "503 Service Unavailable");
        statusCodeMap.put(HTTP_GATEWAY_TIMEOUT, "504 Gateway Timeout");
        statusCodeMap.put(HTTP_HTTP_VERSION_NOT_SUPPORTED, "505 HTTP Version Not Supported");
        statusCodeMap.put(HTTP_NETWORK_AUTHENTICATION_REQUIRED, "511 Network Authentication Required");
    }

    public static String toString(int code) {
        return statusCodeMap.getOrDefault(code, "500 Internal Server Error");
    }
}
