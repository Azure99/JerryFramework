package com.rainng.jerry.mouse;

import com.rainng.jerry.mouse.middleware.BaseMiddleware;
import com.rainng.jerry.util.Logger;
import io.undertow.Undertow;

import java.io.IOException;
import java.lang.management.ManagementFactory;

public class HttpServer {
    private final int port;
    private final String host;
    private final BaseMiddleware middlewareEntry;

    public HttpServer(int port, String host) throws IOException {
        this.port = port;
        this.host = host;
        middlewareEntry = new BaseMiddleware();
    }

    public void start() {
        Undertow undertowServer = Undertow.builder()
                .addHttpListener(port, host)
                .setHandler(new HttpHandler(this))
                .build();

        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        Logger.log("Jerry mouse web server initialized in " + uptime + "ms");

        undertowServer.start();
    }

    public HttpServer addMiddleware(BaseMiddleware middleware) {
        BaseMiddleware lastMiddleware = middlewareEntry;
        BaseMiddleware nextMiddleware = middlewareEntry.getNextMiddleware();
        while (nextMiddleware != null) {
            lastMiddleware = nextMiddleware;
            nextMiddleware = lastMiddleware.getNextMiddleware();
        }

        lastMiddleware.setNextMiddleware(middleware);

        return this;
    }

    public int getPort() {
        return port;
    }

    public BaseMiddleware getMiddlewareEntry() {
        return middlewareEntry;
    }
}
