package com.rainng.jerry.mouse.http;

import com.rainng.jerry.mouse.http.constant.HttpHeaderKey;
import com.rainng.jerry.mouse.http.constant.RequestMethod;
import com.rainng.jerry.mouse.http.map.HttpHeaderMap;
import com.rainng.jerry.mouse.http.map.HttpQueryMap;

import java.io.InputStream;

public class HttpRequest {
    private HttpContext httpContext;
    private RequestMethod method;
    private String path;
    private String resourcePath;
    private String queryString;
    private HttpQueryMap queryArgs;
    private String version;
    private HttpHeaderMap headers;
    private InputStream body;
    private HttpQueryMap form;

    public HttpRequest() {
        method = RequestMethod.GET;
        path = "/";
        resourcePath = "/";
        queryString = "";
        queryArgs = new HttpQueryMap();
        version = "HTTP/1.1";
        headers = new HttpHeaderMap();
        form = new HttpQueryMap();
    }

    public HttpContext getHttpContext() {
        return httpContext;
    }

    public void setHttpContext(HttpContext httpContext) {
        this.httpContext = httpContext;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public HttpQueryMap getQueryArgs() {
        return queryArgs;
    }

    public void setQueryArgs(HttpQueryMap queryArgs) {
        this.queryArgs = queryArgs;
    }

    public HttpHeaderMap getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaderMap headers) {
        this.headers = headers;
    }

    public String getHost() {
        return headers.get(HttpHeaderKey.HOST, "");
    }

    public void setHost(String host) {
        headers.set(HttpHeaderKey.HOST, host);
    }

    public String getContentType() {
        return headers.get(HttpHeaderKey.CONTENT_TYPE, "");
    }

    public void setContentType(String contentType) {
        headers.set(HttpHeaderKey.CONTENT_TYPE, contentType);
    }

    public long getContentLength() {
        String lengthStr = headers.get(HttpHeaderKey.CONTENT_LENGTH, "0");
        return Integer.valueOf(lengthStr);
    }

    public void setContentLength(long contentLength) {
        headers.set(HttpHeaderKey.CONTENT_LENGTH, Long.toString(contentLength));
    }

    public InputStream getBody() {
        return body;
    }

    public void setBody(InputStream body) {
        this.body = body;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public HttpQueryMap getForm() {
        return form;
    }

    public void setForm(HttpQueryMap form) {
        this.form = form;
    }
}
