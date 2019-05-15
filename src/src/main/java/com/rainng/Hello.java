package com.rainng;

import com.rainng.jerry.mouse.HttpServer;
import com.rainng.jerry.mouse.middleware.ErrorMiddleware;
import com.rainng.jerry.mouse.middleware.SessionMiddleware;
import com.rainng.jerry.mouse.middleware.StaticWebMiddleware;
import com.rainng.jerry.webapi.Controller;
import com.rainng.jerry.webapi.WebApiMiddleware;
import com.rainng.jerry.webapi.annotation.Route;
import com.rainng.jerry.webapi.result.IResult;

import java.io.IOException;
import java.util.Date;

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

class ApiController extends Controller {
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

    public IResult add(Double a, Integer b) {
        return value(a + b);
    }

    public IResult redirect() {
        return redirect("http://www.rainng.com");
    }

    public IResult html() {
        return html("<h1>hello</h1>");
    }

    public IResult json() {
        return json(new Student("张三", 123));
    }

    public IResult session() {
        if (!containsSession("hello")) {
            System.out.println("Create session");
            setSession("hello", new Date().toString());
        }

        return value(getSession("hello"));
    }
}

class Student {
    private String name;
    private int id;

    public Student(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}