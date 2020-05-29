package com.rainng;

import com.rainng.jerry.JerryBuilder;
import com.rainng.jerry.mvc.Controller;
import com.rainng.jerry.mvc.annotation.HttpGet;
import com.rainng.jerry.mvc.annotation.HttpMethodMapping;
import com.rainng.jerry.mvc.annotation.HttpPost;
import com.rainng.jerry.mvc.annotation.Route;
import com.rainng.jerry.mvc.result.IResult;

import java.util.Date;

public class Hello {
    public static void main(String[] args) {
        JerryBuilder.createMvc(Hello.class).start();
    }
}

// Auto mapping: http://localhost:9615/demo
class DemoController extends Controller {
    public IResult hello() {
        putModel("key", "Jerry MVC with thymeleaf");
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

    @HttpGet
    public String get() {
        return "Http GET only";
    }

    @HttpPost
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

// http://localhost/user
@HttpMethodMapping
class UserController extends Controller {
    public String get() {
        return "get";
    }

    public String post(String data) {
        return "post: " + data;
    }

    public String delete() {
        return "delete";
    }

    public String patch(String data) {
        return "patch: " + data;
    }

    // http://localhost/user/list
    public String[] getList() {
        return new String[]{"1", "2", "3"};
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