package com.rainng.jerry.mouse;

import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.HttpResponse;
import com.rainng.jerry.mouse.http.constant.HttpStatusCode;
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
        HttpRequestIniter.init(httpContext, inputStream);
        HttpResponseIniter.init(httpContext);

        httpServer.getMiddlewareEntry().onExecute(httpContext);
        afterExecute(httpContext);

        writeResponse(httpContext, outputStream);

        tryClose(httpContext.getRequest().getBody());
        tryClose(httpContext.getResponse().getBody());
        tryClose(inputStream);
        tryClose(outputStream);
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
        head.append("HTTP/1.1 ");
        head.append(HttpStatusCode.toString(response.getStatusCode()));
        head.append("\r\n");

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
        head.append("\r\n");

        outputStream.write(head.toString().getBytes());

        byte[] bodyData = ((ByteArrayOutputStream) response.getBody()).toByteArray();
        outputStream.write(bodyData);
    }

    private void tryClose(Closeable closeable) {
        try {
            closeable.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
