package com.rainng;

import com.rainng.jerry.mouse.HttpServer;
import com.rainng.jerry.mouse.middleware.ErrorMiddleware;
import com.rainng.jerry.mouse.middleware.SessionMiddleware;
import com.rainng.jerry.mouse.middleware.StaticWebMiddleware;
import com.rainng.jerry.webapi.BaseController;
import com.rainng.jerry.webapi.WebApiMiddleware;
import com.rainng.jerry.webapi.annotation.Route;
import com.rainng.jerry.webapi.result.IResult;

import java.io.IOException;

public class Hello {
    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer(9615);

        server.addMiddleware(new ErrorMiddleware());
        server.addMiddleware(new SessionMiddleware());
        server.addMiddleware(new StaticWebMiddleware());
        server.addMiddleware(new WebApiMiddleware(new Class[]{ApiController.class}));

        server.start();
    }
}

class ApiController extends BaseController {
    @Route("hello")
    public IResult test() {
        return value("Hello jerry framework");
    }
}
