package com.rainng.jerry.mouse;

import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.HttpResponse;
import com.rainng.jerry.mouse.http.constant.HttpStatusCode;
import com.rainng.jerry.mouse.http.initializer.HttpCookieIniter;
import com.rainng.jerry.mouse.http.initializer.HttpRequestIniter;
import com.rainng.jerry.mouse.http.initializer.HttpResponseIniter;
import com.rainng.jerry.mouse.http.map.HttpHeaderMap;
import com.rainng.jerry.util.Logger;

import java.io.*;
import java.net.Socket;

public class HttpWorkThread extends Thread {
    private static final String KEEP_ALIVE = "keep-alive";

    private final Socket socket;
    private final HttpServer httpServer;

    public HttpWorkThread(Socket socket, HttpServer server) {
        this.socket = socket;
        this.httpServer = server;
    }

    @Override
    public void run() {
        try {
            onAccept();
        } catch (Exception ex) {
            Logger.ex("Internal server error", ex);
        }
    }

    private void onAccept() throws Exception {
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        HttpContext httpContext = new HttpContext();

        try {
            if (!HttpRequestIniter.init(httpContext, inputStream)) {
                return;
            }
            HttpCookieIniter.init(httpContext);
            HttpResponseIniter.init(httpContext);

            httpServer.getMiddlewareEntry().onExecute(httpContext);

            afterExecute(httpContext);
            writeResponse(httpContext, outputStream);

            if (KEEP_ALIVE.equals(httpContext.getRequest().getConnection())) {
                onAccept();
            }
        } finally {
            tryClose(httpContext.getRequest().getBody());
            tryClose(httpContext.getResponse().getBody());
            tryClose(inputStream);
            tryClose(outputStream);
        }
    }

    private void afterExecute(HttpContext context) {
        HttpResponse response = context.getResponse();

        long contentLength = ((ByteArrayOutputStream) response.getBody()).size();
        response.setContentLength(contentLength);

        if (response.getContentType().startsWith("text/")) {
            String contentType = response.getContentType() + "; charset=utf-8";
            response.setContentType(contentType);
        }

        if (KEEP_ALIVE.equals(context.getRequest().getConnection())) {
            response.setConnection(KEEP_ALIVE);
        }
    }

    private void writeResponse(HttpContext context, OutputStream outputStream) throws IOException {
        HttpResponse response = context.getResponse();

        StringBuilder head = new StringBuilder();

        // 构建首行
        head.append("HTTP/1.1 ");
        head.append(HttpStatusCode.toString(response.getStatusCode()));
        head.append("\r\n");

        // 写入Headers(Set-Cookie除外)
        HttpHeaderMap headers = response.getHeaders();
        for (String headerKey : headers.keySet()) {
            String headerValue = headers.get(headerKey, "");
            if ("".equals(headerValue)) {
                continue;
            }

            head.append(headerKey);
            head.append(": ");
            head.append(headerValue);
            head.append("\r\n");
        }

        // 写入Set-Cookie Headers
        String setCookieHeaderString = context.getCookies().toSetCookieHeaderString();
        head.append(setCookieHeaderString);
        if (setCookieHeaderString.length() > 0) {
            head.append("\r\n");
        }

        // 空行 标识HTTP头结束
        head.append("\r\n");

        // 写入Response Header
        outputStream.write(head.toString().getBytes());

        // 写入Response Body
        byte[] bodyData = ((ByteArrayOutputStream) response.getBody()).toByteArray();
        outputStream.write(bodyData);
    }

    private void tryClose(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception ex) {
            Logger.ex("Can not release some resources", ex);
        }
    }
}
