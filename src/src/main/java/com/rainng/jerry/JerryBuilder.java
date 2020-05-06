package com.rainng.jerry;

import com.rainng.jerry.mouse.HttpServer;
import com.rainng.jerry.mouse.middleware.ErrorMiddleware;
import com.rainng.jerry.mouse.middleware.SessionMiddleware;
import com.rainng.jerry.mouse.middleware.StaticWebMiddleware;
import com.rainng.jerry.mvc.MvcMiddleware;

public class JerryBuilder {
    private boolean useError;
    private boolean useSession;
    private boolean useStaticWeb;
    private boolean useMvc;

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

    public static JerryBuilder createMvc(int port, Class<?> appClass) {
        return createMvc(appClass).setPort(port);
    }

    public static JerryBuilder createMvc(Class<?> appClass) {
        return new JerryBuilder()
                .useError()
                .useSession()
                .useStaticWeb()
                .useMvc(appClass);
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

    public JerryBuilder useMvc(Class<?> appClass) {
        useMvc = true;
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
            if (useMvc) {
                server.addMiddleware(new MvcMiddleware(appClass));
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
