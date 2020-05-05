package com.rainng.jerry.mouse;

import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.HttpResponse;
import com.rainng.jerry.mouse.http.constant.HttpStatusCode;
import com.rainng.jerry.mouse.http.initializer.HttpCookieIniter;
import com.rainng.jerry.mouse.http.initializer.HttpRequestIniter;
import com.rainng.jerry.mouse.http.initializer.HttpResponseIniter;
import com.rainng.jerry.mouse.http.map.HttpHeaderMap;

import java.io.*;
import java.net.Socket;

public class HttpWorkThread extends Thread {
    private Socket socket;
    private HttpServer httpServer;

    public HttpWorkThread(Socket socket, HttpServer server) {
        this.socket = socket;
        this.httpServer = server;
    }

    @Override
    public void run() {
        try {
            onAccept();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void onAccept() throws Exception {
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        HttpContext httpContext = new HttpContext();

        try {
            HttpRequestIniter.init(httpContext, inputStream);
            HttpCookieIniter.init(httpContext);
            HttpResponseIniter.init(httpContext);

            httpServer.getMiddlewareEntry().onExecute(httpContext);

            afterExecute(httpContext);
            writeResponse(httpContext, outputStream);
        } catch (Exception ex) {
            ex.printStackTrace();
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
            if (headerValue.equals("")) {
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

        //写入Response Header
        outputStream.write(head.toString().getBytes());

        // 写Rresponse Body
        byte[] bodyData = ((ByteArrayOutputStream) response.getBody()).toByteArray();
        outputStream.write(bodyData);
    }

    private void tryClose(Closeable closeable) {
        try {
            closeable.close();
        } catch (Exception ex) {

        }
    }
}
