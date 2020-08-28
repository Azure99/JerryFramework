package com.rainng.jerry;

import com.rainng.jerry.mouse.HttpServer;
import com.rainng.jerry.mouse.middleware.ErrorMiddleware;
import com.rainng.jerry.mouse.middleware.SessionMiddleware;
import com.rainng.jerry.mouse.middleware.StaticWebMiddleware;
import com.rainng.jerry.mvc.MvcMiddleware;
import com.rainng.jerry.util.Logger;

public class JerryBuilder {
    private boolean useError;
    private boolean useSession;
    private boolean useStaticWeb;
    private boolean useMvc;

    private int port = 9615;
    private String rootDirectory = "wwwroot";
    private int maxSessionAge = 3600;
    private Class<?> appClass;

    public JerryBuilder(Class<?> appClass) {
        this.appClass = appClass;
    }

    public JerryBuilder(Class<?> appClass, int port) {
        this.appClass = appClass;
        this.port = port;
    }

    public static JerryBuilder createStaticWeb(Class<?> appClass, int port) {
        return createStaticWeb(appClass).setPort(port);
    }

    public static JerryBuilder createStaticWeb(Class<?> appClass, int port, String rootDirectory) {
        return createStaticWeb(appClass, rootDirectory).setPort(port);
    }

    public static JerryBuilder createStaticWeb(Class<?> appClass, String rootDirectory) {
        return createStaticWeb(appClass).setRootDirectory(rootDirectory);
    }

    public static JerryBuilder createStaticWeb(Class<?> appClass) {
        return new JerryBuilder(appClass)
                .useError()
                .useStaticWeb();
    }

    public static JerryBuilder createMvc(Class<?> appClass, int port) {
        return createMvc(appClass).setPort(port);
    }

    public static JerryBuilder createMvc(Class<?> appClass) {
        return new JerryBuilder(appClass)
                .useError()
                .useSession()
                .useStaticWeb()
                .useMvc();
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

    public JerryBuilder useMvc() {
        useMvc = true;
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
            if (useMvc) {
                server.addMiddleware(new MvcMiddleware(appClass));
            }

        } catch (Exception ex) {
            Logger.ex("Error when build middleware pipeline", ex);
        }

        return server;
    }

    public void start() {
        this.build().start();
    }
}
