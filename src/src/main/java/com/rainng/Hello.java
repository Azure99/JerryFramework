package com.rainng;

import com.rainng.jerry.mouse.HttpServer;
import com.rainng.jerry.mouse.middleware.ErrorMiddleware;
import com.rainng.jerry.mouse.middleware.SessionMiddleware;
import com.rainng.jerry.mouse.middleware.StaticWebMiddleware;

import java.io.IOException;

public class Hello {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello jerryframework");

        HttpServer server = new HttpServer(9615);

        server.addMiddleware(new ErrorMiddleware());
        server.addMiddleware(new SessionMiddleware());
        server.addMiddleware(new StaticWebMiddleware());

        server.start();
    }
}
