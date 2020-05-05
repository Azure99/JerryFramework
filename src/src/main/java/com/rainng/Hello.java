package com.rainng;

import com.rainng.jerry.JerryBuilder;
import com.rainng.jerry.webapi.Controller;
import com.rainng.jerry.webapi.annotation.Get;
import com.rainng.jerry.webapi.annotation.Post;
import com.rainng.jerry.webapi.annotation.Route;
import com.rainng.jerry.webapi.result.IResult;

import java.io.IOException;
import java.util.Date;

public class Hello {
    public static void main(String[] args) throws IOException {
        JerryBuilder.createWebApi(DemoController.class).start();
    }
}

// Auto mapping: http://localhost:9615/demo
class DemoController extends Controller {
    public IResult hello(Integer a, Integer b) {
        putModel("key", "Jerry MVC with thymeleaf");
        putModel("sum", a + b);

        return view("index.html");
    }

    public Double add(Integer a, Double b) {
        return a + b;
    }

    public Student json() {
        return new Student(1, "Azure99");
    }

    public IResult json2() {
        return json("id|name", 1, "Azure99");
    }

    public IResult json3() {
        return json("id|name|info", 1, "Azure99", jsono(
                "birthday|friends",
                new Date(), new String[]{"A", "B", "C", "D"}));
    }

    @Route("route")
    public String route() {
        return "/demo/route";
    }

    @Route("/route2")
    public String route2() {
        return "/route2";
    }

    public IResult redirect() {
        return redirect("https://www.baidu.com");
    }

    @Get
    public String get() {
        return "Http GET only";
    }

    @Post
    public String post(String arg) {
        return "Http POST only";
    }

    public IResult html() {
        return html("<h1>Html</h1>");
    }

    public Object session() {
        Object session = getSession("time");
        if (session == null) {
            session = new Date().toString();
            setSession("time", session);
        }
        return session;
    }
}

class Student {
    private Integer id;
    private String name;

    public Student(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}