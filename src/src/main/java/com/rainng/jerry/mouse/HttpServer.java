package com.rainng.jerry.mouse;

import com.rainng.jerry.mouse.middleware.BaseMiddleware;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

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
        ExecutorService executorService = createThreadPool();

        while (running) {
            try {
                Socket socket = serverSocket.accept();
                try {
                    executorService.submit(new HttpWorkThread(socket, this));
                } catch (RejectedExecutionException ex) {
                    System.err.println(ex.getMessage());
                    socket.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private ExecutorService createThreadPool() {
        int processorCount = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = new ThreadPoolExecutor(
                processorCount * 4,
                Math.max(processorCount * 256, 4096),
                60,
                TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );

        return executorService;
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
