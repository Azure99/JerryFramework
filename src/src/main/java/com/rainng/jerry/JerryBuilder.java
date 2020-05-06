package com.rainng.jerry;

import com.rainng.jerry.mouse.HttpServer;
import com.rainng.jerry.mouse.middleware.ErrorMiddleware;
import com.rainng.jerry.mouse.middleware.SessionMiddleware;
import com.rainng.jerry.mouse.middleware.StaticWebMiddleware;
import com.rainng.jerry.webapi.WebApiMiddleware;

public class JerryBuilder {
    private boolean useError;
    private boolean useSession;
    private boolean useStaticWeb;
    private boolean useWebApi;

    private int port = 9615;
    private String rootDirectory = "wwwroot";
    private int maxSessionAge = 3600;
    private Class<?> appClass = JerryBuilder.class;

    public JerryBuilder() {

    }

    public JerryBuilder(int port) {
        this.port = port;
    }

    public static JerryBuilder createStaticWeb(int port) {
        return createStaticWeb().setPort(port);
    }

    public static JerryBuilder createStaticWeb(int port, String rootDirectory) {
        return createStaticWeb(rootDirectory).setPort(port);
    }

    public static JerryBuilder createStaticWeb(String rootDirectory) {
        return createStaticWeb().setRootDirectory(rootDirectory);
    }

    public static JerryBuilder createStaticWeb() {
        return new JerryBuilder()
                .useError()
                .useStaticWeb();
    }

    public static JerryBuilder createWebApi(int port, Class<?> appClass) {
        return createWebApi(appClass).setPort(port);
    }

    public static JerryBuilder createWebApi(Class<?> appClass) {
        return new JerryBuilder()
                .useError()
                .useSession()
                .useStaticWeb()
                .useWebApi(appClass);
    }

    public JerryBuilder useError() {
        useError = true;
        return this;
    }

    public JerryBuilder useSession() {
        useSession = true;
        return this;
    }

    public JerryBuilder useSession(int maxSessionAge) {
        useSession = true;
        this.maxSessionAge = maxSessionAge;
        return this;
    }

    public JerryBuilder useStaticWeb() {
        useStaticWeb = true;
        return this;
    }

    public JerryBuilder useStaticWeb(String rootDirectory) {
        useStaticWeb = true;
        this.rootDirectory = rootDirectory;
        return this;
    }

    public JerryBuilder useWebApi() {
        useWebApi = true;
        return this;
    }

    public JerryBuilder useWebApi(Class<?> appClass) {
        useWebApi = true;
        this.appClass = appClass;
        return this;
    }

    public JerryBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public JerryBuilder setRootDirectory(String rootDirectory) {
        this.rootDirectory = rootDirectory;
        return this;
    }

    public JerryBuilder setMaxSessionAge(int maxSessionAge) {
        this.maxSessionAge = maxSessionAge;
        return this;
    }

    public JerryBuilder setAppClass(Class<?> appClass) {
        this.appClass = appClass;
        return this;
    }

    public HttpServer build() {
        HttpServer server = null;
        try {
            server = new HttpServer(port);

            if (useError) {
                server.addMiddleware(new ErrorMiddleware());
            }
            if (useSession) {
                server.addMiddleware(new SessionMiddleware(maxSessionAge));
            }
            if (useStaticWeb) {
                server.addMiddleware(new StaticWebMiddleware(rootDirectory));
            }
            if (useWebApi) {
                server.addMiddleware(new WebApiMiddleware(appClass));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return server;
    }

    public void start() {
        this.build().start();
    }
}
