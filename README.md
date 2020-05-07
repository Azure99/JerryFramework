# JerryFramework

小巧、精致的非Servlet Web框架，采用管道+中间件的请求处理模型，拥有良好的组件扩展性。实现了自托管Web服务器以及多个组件：错误拦截、静态文件服务、Session、MVC（支持约定优先的请求映射，模板引擎支持Thymeleaf，并支持过滤器、执行结果序列化为Json等特性）

### 特性

- 实现了非Servlet的自托管Web服务器
- 静态文件
- 错误拦截
- Session/Cookie
- MVC（模板引擎为Thymeleaf、也支持序列化为Json）
- 约定优先的请求映射，支持参数映射
- 使用@Route注解指定请求映射，使用@HttpXXX注释限定请求方法
- 请求/响应拦截器
- URL、表单自动编解码
- 支持中间件扩展

### 理念

跳出传统思维，尝试在效率与灵活之间寻找平衡，提供敏捷的小型应用开发方式。

#### 一、侵入式编程模型

Spring曾极力推崇非侵入式编程模型，付出的代价是海量的XML配置，如今Spring也推荐使用注解来简化配置。JerryFramework面向小应用，MVC模块主张**侵入式**的编程模型（**继承Controller**），这可以更方便的获取上下文信息。

#### 二、约定优先的请求映射

很多时候，我们的Controller层都使用如下命名：**HomController -> hello()**。SpringMVC需要编写大量的@RequestMapping("/home/hello")，而Jerry MVC倡导**约定优先**，不需要编写任何注解，它会自动根据Name[Controller]/method()来映射请求。

当然，如果认为此功能多余，可以使用@Route("/home/hello")注解来映射请求，也可以使用@HttpGet等注解来限定请求方法

```java
public class HomeController extends Controller {
    public String hello() {
        return "say hello";
    }
}
```

#### 三·、与众不同的响应包装

Jerry MVC控制器的所有方法执行结果都将是**IResult**的子类，执行结果支持View、Json、Html、Redirect等等，注：非IResult的结果会自动包装成**JsonResult**。这样做的好处是：结合侵入式编程模型可以非常简便的生成各种响应。

```Java
public class HomeController extends Controller {
    public IResult mvc() {
        putModel("key", "Jerry MVC with thymeleaf");
        return view("index.html");
    }

    public Student json() {
        return new Student(1, "Azure99");
    }

    public IResult redirect() {
        return redirect("https://github.com");
    }

    public IResult html() {
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

Spring MVC

```java
@Controller
@RequestMapping("/demo")
class DemoController {
    @RequestMapping("/mvc")
    public String mvc(Model model, Integer a, Integer b) {
        model.addAttribute("key", "Spring MVC with thymeleaf");
        model.addAttribute("sum", a + b);
        return "index";
    }

    @ResponseBody
    @RequestMapping("/json")
    public Student json() {
        return new Student(1, "Azure99");
    }

    @ResponseBody
    @RequestMapping("/redirect")
    public void redirect(HttpServletResponse resp) {
        try {
            resp.sendRedirect("https://www.baidu.com");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @ResponseBody
    @GetMapping("/get")
    public String get() {
        return "Http GET only";
    }

    @ResponseBody
    @RequestMapping(value = "/html", produces = "text/html")
    public String html() {
        return "<h1>Html</h1>";
    }
}
```

Jerry MVC

```java
class DemoController extends Controller {
    public IResult mvc(Integer a, Integer b) {
        putModel("key", "Jerry MVC with thymeleaf");
        putModel("sum", a + b);
        return view("index.html");
    }

    public Student json() {
        return new Student(1, "Azure99");
    }

    public IResult redirect() {
        return redirect("https://www.baidu.com");
    }

    @HttpGet
    public String get() {
        return "Http GET only";
    }

    public IResult html() {
        return html("<h1>Html</h1>");
    }
}
```
