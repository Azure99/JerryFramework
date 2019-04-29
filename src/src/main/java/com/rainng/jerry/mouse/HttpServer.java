package com.rainng.jerry.mouse;

import com.rainng.jerry.mouse.middleware.BaseMiddleware;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    private ServerSocket serverSocket;
    private int port;
    private BaseMiddleware middlewareEntry;
    private boolean running;

    public HttpServer(int port) throws IOException {
        this.port = port;
        middlewareEntry = new BaseMiddleware();

        serverSocket = new ServerSocket(port);
        running = false;
    }

    public void start() {
        running = true;
        startAccept();
    }

    public void stop() {
        running = false;
    }

    private void startAccept() {
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                new HttpWorkThread(socket, this).start();

            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
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
