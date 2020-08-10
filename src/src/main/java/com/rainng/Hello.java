package com.rainng;

import com.rainng.jerry.JerryBuilder;
import com.rainng.jerry.mouse.http.Cookie;
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

/**
 * Auto mapping: [ControllerName(without postfix)]/[MethodName]
 * http://localhost:9615/demo/hello
 */
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

    public IResult quickJson() {
        return json("id|name", 1, "Azure99");
    }

    public IResult nestJson() {
        return json("id|name|info", 1, "Azure99", jsono(
                "birthday|friends",
                new Date(), new String[]{"A", "B", "C", "D"}));
    }

    @Route("route")
    public String relativeRoute() {
        return "My path is /demo/route";
    }

    @Route("/route2")
    public String absoluteRoute() {
        return "My path is /route2";
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

    public Object cookie() {
        Cookie cookie = getCookie("foo");
        if (cookie == null) {
            cookie = new Cookie("foo", "bar");
            setCookie(cookie);
        }
        return cookie.getValue();
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

/**
 * Auto mapping: [ControllerName(without postfix)]/MethodName(without HTTP verb)
 * GET http://localhost:9615/user
 * POST http://localhost:9615/user
 * ...
 * GET http://localhost:9615/user/list
 */
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