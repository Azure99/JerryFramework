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
        server.addMiddleware(new StaticWebMiddleware());
        server.addMiddleware(new WebApiMiddleware(new Class[]{ApiController.class}));

        server.start();
    }
}

class ApiController extends BaseController {
    public IResult value() {
        return value("Hello JerryFramework");
    }

    @Route("/r1")
    public IResult route1() {
        return value("Route example 1");
    }

    @Route("r2")
    public IResult route2() {
        return value("Route example 2");
    }

    public IResult add(Integer a, Integer b) {
        return value(a + b);
    }

    public IResult redirect() {
        return redirect("http://www.rainng.com");
    }

    public IResult html() {
        return html("<h1>hello</h1>");
    }
}