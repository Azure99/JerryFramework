# JerryFramework

Code is your mean - 强调代码自身表现力的Java Web框架

采用管道+中间件的请求处理模型，拥有良好的组件扩展性。实现了自托管Web服务器以及多个组件：错误拦截、静态文件服务、Session、MVC等（拥有强大的基于约定优先的请求映射）

### 特性

- 非Servlet栈的自托管Web服务器
- 静态Web服务
- 全局错误处理
- Session/Cookie
- MVC（模板引擎为Thymeleaf，也支持序列化为Json）
- 约定优先的请求映射，支持请求参数/表单字段映射
- 支持HTTP动词映射，Body自动反序列化与映射
- 使用@Route注解手动指定映射路径，使用@HttpXXX注解限定请求方法
- 请求/响应拦截器
- URL、表单自动编解码
- 支持中间件扩展

### 理念

约定优先，框架绑定，牺牲一定灵活度以大幅提升代码与框架结合能力

#### 一、侵入式编程模型

Spring曾极力推崇非侵入式编程模型，然而，基于注解的声明依旧含有侵入性，真正意义上的非侵入却需要引入海量的、难以维护的XML配置。同时，SpringMVC在Web层不要求与框架强绑定（指无需继承框架的基类），使得我们在使用上下文信息时，必须增加额外代码来注入。

JerryFramework面向小应用，MVC模块主张**侵入式**的编程模型（继承Controller），可减少了不必要的注解声明，同时能更方便的获取上下文信息。

#### 二、约定优先的请求映射

- 命名

很多时候，我们的Web层都使用如下命名：**HomController -> hello()**。SpringMVC需要编写大量的@RequestMapping("/home/hello")，而JerryMVC倡导**约定优先**，不需要编写任何注解，它会自动根据Name[Controller]/method()来映射请求。

当然，如果认为此功能多余，可以使用@Route("/home/hello")注解来映射请求，也可以使用@HttpGet等注解来限定请求方法

值得一提的是，JerryMVC的@Route是支持继承的，可以使用相对路径与绝对路径两种形式

```java
public class HomeController extends Controller {
    public String hello() {
        return "say hello";
    }
}
```

- 参数/Body映射

JerryMVC将**请求参数、表单参数**简化聚合为**参数列表**，根据参数名在调用方法时自动传入。同时，可以使用@RequestBody注解来指定一个参数来源为请求的Body，它支持自动反序列化JSON为参数类型，这点与SpringMVC一致。

另外，代码中还演示了快速构建Json响应的特性，它含有message和student两个字段

```java
public Result test(String message, @RequestBody Student student) {
    return json("message|student", message, student);
}
```

- HTTP动词映射

对于一类实体，我们通常会进行CRUD操作，可以通过相似的路径、不同的HTTP方法来区分

只需要方法以**HTTP动词**开头，JerryMVC即可实现映射

```java
@HttpMethodMapping
class UserController extends Controller {
    // GET/POST/DELETE/PATCH /user
    public String get() { return "get"; }
    public String post(String data) { return "post: " + data; }
    public String delete() { return "delete"; }
    public String patch(String data) { return "patch: " + data; }

    // GET /user/list
    public String[] getList() { return new String[]{"1", "2", "3"}; }
}
```



#### 三·、与众不同的响应包装

在SpringMVC中，一个方法返回String可能有多种含义：View？重定向指令？或者只是字符串？这种不确定的设计导致我们必须使用额外的、业务无关的代码来明确我们的需求，例如@ResponseBody注解

Jerry MVC控制器的所有方法执行结果都将是**Result**的子类，执行结果支持View、Json、Html、Redirect等等，这种设计结合侵入式编程模型可以非常简洁且明确的表达需求。

注：非Result的结果会自动包装成**JsonResult**。

```Java
public class HomeController extends Controller {
    public Result mvc() {
        putModel("key", "Jerry MVC with thymeleaf");
        return view("index.html");
    }

    public Student json() {
        return new Student(1, "Azure99");
    }

    public Result redirect() {
        return redirect("https://github.com");
    }

    public Result html() {
        return html("<h1>Html</h1>");
    }
}
```

#### 四、管道+中间件的请求处理模型

你可以简单的理解为类似Servlet中Filter的模型，FilterChain≈管道、Filter≈中间件、doFilter≈next。这些"Filter"通过**特定的组合顺序**并**控制"doFilter"的调用时机**来实现具体功能，例如：

- 错误处理中间件最早执行，在try块中调用下一个中间件以便拦截异常
- Session中间件应当在MVC中间件执行前执行，以便实现session功能
- 静态文件中间件在找到对应文件后可以不调用下一个中间件，进而打破管道
- MVC中间件放在最后

缺点是中间件的组合必须为特定顺序，因此框架采用构造者模式提供了一个简单的JerryBuilder类

### 简化开发

```java
/**
 * Auto mapping: /demo/hello、/demo/add ...
 */
class DemoController extends Controller {
    // 返回一个视图, 可通过putModel来传递数据
    public Result hello() {
        putModel("key", "Jerry MVC with thymeleaf");
        return view("index.html");
    }

    // 请求参数映射
    public Double add(Integer a, Double b) {
        return a + b;
    }

    // 自动将返回的Student实例序列化为Json
    public Student json() {
        return new Student();
    }

    // 快速构建Json的API, 字段使用|分隔, 后面接n个参数为n字段赋值
    public Result quickJson() {
        return json("id|name", 1, "Azure99");
    }

    // 快速构建复杂Json的API, 使用jsono方法来嵌套一个Json对象
    public Result nestJson() {
        return json("id|name|info", 1, "Azure99", jsono(
                "birthday|friends",
                new Date(), new String[]{"A", "B", "C", "D"}));
    }

    // 根据类型自动反序列化Body并映射到student参数
    public Result requestBody(String message, @RequestBody Student student) {
        return json("message|student", message, student);
    }

    // 相对路径的路由指定, 会继承父亲的路径
    @Route("route")
    public String relativeRoute() {
        return "My path is /demo/route";
    }

    // 绝对路径的路由指定, 不会继承父亲的路径
    @Route("/route2")
    public String absoluteRoute() {
        return "My path is /route2";
    }

    // 限定请求方法为GET
    @HttpGet
    public String get() {
        return "Http GET only";
    }

    // 返回302重定向
    public Result redirect() {
        return redirect("https://www.baidu.com");
    }

    // 返回一段Html
    public Result html() {
        return html("<h1>Html</h1>");
    }

    // 使用Cookie, 可通过setCookie设置
    public Object cookie() {
        return getCookie("foo").getValue();
    }

    // 使用Session, 可通过setSession设置
    public Object session() {
        return getSession("datetime");
    }
}

/**
 * Auto mapping:
 * GET/POST/DELETE/PATCH /user
 * GET /user/list
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

    public String[] getList() {
        return new String[]{"1", "2", "3"};
    }
}
```
