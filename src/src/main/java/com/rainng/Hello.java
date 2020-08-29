package com.rainng;

import com.rainng.jerry.JerryBuilder;
import com.rainng.jerry.mvc.Controller;
import com.rainng.jerry.mvc.annotation.*;
import com.rainng.jerry.mvc.result.Result;

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
    public Result hello() {
        putModel("key", "Jerry MVC with thymeleaf");
        return view("index.html");
    }

    public Double add(Integer a, Double b) {
        return a + b;
    }

    public Student json() {
        return new Student();
    }

    public Result quickJson() {
        return json("id|name", 1, "Azure99");
    }

    public Result nestJson() {
        return json("id|name|info", 1, "Azure99", jsono(
                "birthday|friends",
                new Date(), new String[]{"A", "B", "C", "D"}));
    }

    public Result requestBody(String message, @RequestBody Student student) {
        return json("message|student", message, student);
    }

    @Route("route")
    public String relativeRoute() {
        return "My path is /demo/route";
    }

    @Route("/route2")
    public String absoluteRoute() {
        return "My path is /route2";
    }

    @HttpGet
    public String get() {
        return "Http GET only";
    }

    @HttpPost
    public String post(String arg) {
        return "Http POST only";
    }

    @HttpPut
    @HttpPatch
    public String putOrPatch(String arg) {
        return "Http PUT or PATCH";
    }

    public Result redirect() {
        return redirect("https://www.baidu.com");
    }

    public Result html() {
        return html("<h1>Html</h1>");
    }

    public Object cookie() {
        return getCookie("foo").getValue();
    }

    public Object session() {
        return getSession("datetime");
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
    public Integer id = 1;
    public String name = "Azure99";
}