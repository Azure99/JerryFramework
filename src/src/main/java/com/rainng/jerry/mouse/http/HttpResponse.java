package com.rainng.jerry.mouse.http;

import com.rainng.jerry.mouse.http.constant.HttpHeaderKey;
import com.rainng.jerry.mouse.http.constant.HttpStatusCode;
import com.rainng.jerry.mouse.http.map.HttpHeaderMap;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class HttpResponse {
    private HttpContext httpContext;
    private int statusCode;
    private HttpHeaderMap headers;
    private OutputStream body;

    public HttpResponse() {
        statusCode = HttpStatusCode.HTTP_OK;
        headers = new HttpHeaderMap();
        body = new ByteArrayOutputStream();
    }

    public HttpContext getHttpContext() {
        return httpContext;
    }

    public void setHttpContext(HttpContext httpContext) {
        this.httpContext = httpContext;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public HttpHeaderMap getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaderMap headers) {
        this.headers = headers;
    }

    public OutputStream getBody() {
        return body;
    }

    public void setBody(OutputStream body) {
        this.body = body;
    }

    public long getContentLength() {
        String lengthStr = headers.get(HttpHeaderKey.CONTENT_LENGTH, "0");
        return Integer.valueOf(lengthStr);
    }

    public void setContentLength(long contentLength) {
        headers.set(HttpHeaderKey.CONTENT_LENGTH, Long.toString(contentLength));
    }

    public String getContentType() {
        return headers.get(HttpHeaderKey.CONTENT_TYPE, "");
    }

    public void setContentType(String contentType) {
        headers.set(HttpHeaderKey.CONTENT_TYPE, contentType);
    }
}