package com.rainng.jerry.mouse.http.constant;

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

    public static String toString(int code) {
        switch (code) {
            case HTTP_CONTINUE:
                return "100 Continue";
            case HTTP_SWITCHING_PROTOCOLS:
                return "101 Switching Protocols";
            case HTTP_OK:
                return "200 OK";
            case HTTP_CREATED:
                return "201 Created";
            case HTTP_ACCEPTED:
                return "202 Accepted";
            case HTTP_NON_AUTHORITATIVE_INFORMATION:
                return "203 Non-Authoritative Information";
            case HTTP_NO_CONTENT:
                return "204 No Content";
            case HTTP_RESET_CONTENT:
                return "205 Reset Content";
            case HTTP_PARTIAL_CONTENT:
                return "206 Partial Content";
            case HTTP_MULTIPLE_CHOICES:
                return "300 Multiple Choices";
            case HTTP_MOVED_PERMANENTLY:
                return "301 Moved Permanently";
            case HTTP_FOUND:
                return "302 Found";
            case HTTP_SEE_OTHER:
                return "303 See Other";
            case HTTP_NOT_MODIFIED:
                return "304 Not Modified";
            case HTTP_TEMPORARY_REDIRECT:
                return "307 Temporary Redirect";
            case HTTP_PERMANENT_REDIRECT:
                return "308 Permanent Redirect";
            case HTTP_BAD_REQUEST:
                return "400 Bad Request";
            case HTTP_UNAUTHORIZED:
                return "401 Unauthorized";
            case HTTP_FORBIDDEN:
                return "403 Forbidden";
            case HTTP_NOT_FOUND:
                return "404 Not Found";
            case HTTP_METHOD_NOT_ALLOWED:
                return "405 Method Not Allowed";
            case HTTP_NOT_ACCEPTABLE:
                return "406 Not Acceptable";
            case HTTP_PROXY_AUTHENTICATION_REQUIRED:
                return "407 Proxy Authentication Required";
            case HTTP_REQUEST_TIMEOUT:
                return "408 Request Timeout";
            case HTTP_CONFLICT:
                return "409 Conflict";
            case HTTP_GONE:
                return "410 Gone";
            case HTTP_LENGTH_REQUIRED:
                return "411 Length Required";
            case HTTP_PRECONDITION_FAILED:
                return "412 Precondition Failed";
            case HTTP_PAYLOAD_TOO_LARGE:
                return "413 Payload Too Large";
            case HTTP_URI_TOO_LONG:
                return "414 URI Too Long";
            case HTTP_UNSUPPORTED_MEDIA_TYPE:
                return "415 Unsupported Media Type";
            case HTTP_RANGE_NOT_SATISFIABLE:
                return "416 Range Not Satisfiable";
            case HTTP_EXPECTATION_FAILED:
                return "417 Expectation Failed";
            case HTTP_UNPROCESSABLE_ENTITY:
                return "422 Unprocessable Entity";
            case HTTP_TOO_EARLY:
                return "425 Too Early";
            case HTTP_UPGRADE_REQUIRED:
                return "426 Upgrade Required";
            case HTTP_PRECONDITION_REQUIRED:
                return "428 Precondition Required";
            case HTTP_TOO_MANY_REQUESTS:
                return "429 Too Many Requests";
            case HTTP_REQUEST_HEADER_FIELDS_TOO_LARGE:
                return "431 Request Header Fields Too Large";
            case HTTP_UNAVAILABLE_FOR_LEGAL_REASONS:
                return "451 Unavailable For Legal Reasons";
            case HTTP_INTERNAL_SERVER_ERROR:
                return "500 Internal Server Error";
            case HTTP_NOT_IMPLEMENTED:
                return "501 Not Implemented";
            case HTTP_BAD_GATEWAY:
                return "502 Bad Gateway";
            case HTTP_SERVICE_UNAVAILABLE:
                return "503 Service Unavailable";
            case HTTP_GATEWAY_TIMEOUT:
                return "504 Gateway Timeout";
            case HTTP_HTTP_VERSION_NOT_SUPPORTED:
                return "505 HTTP Version Not Supported";
            case HTTP_NETWORK_AUTHENTICATION_REQUIRED:
                return "511 Network Authentication Required";
            default:
                return "500 Internal Server Error";
        }
    }
}
