package com.rainng.jerry.mouse;

import com.rainng.jerry.mouse.http.Cookie;
import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.HttpResponse;
import com.rainng.jerry.mouse.http.constant.HttpHeaderKey;
import com.rainng.jerry.mouse.http.initializer.HttpCookieIniter;
import com.rainng.jerry.mouse.http.initializer.HttpRequestIniter;
import com.rainng.jerry.mouse.http.initializer.HttpResponseIniter;
import com.rainng.jerry.util.Logger;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.HttpString;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Map;

public class HttpHandler implements io.undertow.server.HttpHandler {
    private final HttpServer httpServer;

    public HttpHandler(HttpServer server) {
        this.httpServer = server;
    }

    @Override
    public void handleRequest(HttpServerExchange serverExchange) throws Exception {
        serverExchange.getRequestReceiver().receiveFullBytes(this::handleRequest);
    }


    private void handleRequest(HttpServerExchange serverExchange, byte[] requestBody) {
        HttpContext httpContext = new HttpContext();
        try {
            HttpRequestIniter.init(httpContext, serverExchange, requestBody);
            HttpCookieIniter.init(httpContext);
            HttpResponseIniter.init(httpContext);

            httpServer.getMiddlewareEntry().onExecute(httpContext);
            afterExecute(httpContext);

            writeResponse(httpContext, serverExchange);
        } catch (Exception ex) {
            Logger.ex("Unknown server error", ex);
        }
    }

    private void afterExecute(HttpContext context) {
        HttpResponse response = context.getResponse();
        if (response.getContentType().startsWith("text/") && !response.getContentType().contains("charset=")) {
            String contentType = response.getContentType() + "; charset=utf-8";
            response.setContentType(contentType);
        }
    }

    private void writeResponse(HttpContext context, HttpServerExchange serverExchange) {
        HttpResponse response = context.getResponse();

        HeaderMap serverHeaderMap = serverExchange.getResponseHeaders();
        for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
            if ("".equals(header.getValue())) {
                continue;
            }
            serverHeaderMap.put(new HttpString(header.getKey()), header.getValue());
        }
        for (Cookie cookie : context.getCookies().values()) {
            if (cookie.isFromRequest() && cookie.isChanged()) {
                return;
            }
            serverHeaderMap.add(new HttpString(HttpHeaderKey.SET_COOKIE), cookie.toString());
        }

        serverExchange.setStatusCode(response.getStatusCode());
        ByteBuffer bodyBuffer = ByteBuffer.wrap(((ByteArrayOutputStream) response.getBody()).toByteArray());
        serverExchange.getResponseSender().send(bodyBuffer);
    }
}
