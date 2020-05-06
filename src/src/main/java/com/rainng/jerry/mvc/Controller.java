package com.rainng.jerry.mvc;

import com.alibaba.fastjson.JSONObject;
import com.rainng.jerry.mouse.http.Cookie;
import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.HttpRequest;
import com.rainng.jerry.mouse.http.HttpResponse;
import com.rainng.jerry.mouse.http.map.HttpCookieMap;
import com.rainng.jerry.mouse.http.map.HttpSessionMap;
import com.rainng.jerry.mvc.annotation.Route;
import com.rainng.jerry.mvc.result.*;

import java.lang.reflect.Method;
import java.util.Map;

@Route("/")
public class Controller {
    private HttpContext httpContext;
    private ActionContext actionContext;

    public HttpContext getHttpContext() {
        return httpContext;
    }

    public void setHttpContext(HttpContext httpContext) {
        this.httpContext = httpContext;
    }

    protected HttpRequest getRequest() {
        return httpContext.getRequest();
    }

    protected HttpResponse getResponse() {
        return httpContext.getResponse();
    }

    protected HttpCookieMap getCookies() {
        return getHttpContext().getCookies();
    }

    protected Cookie getCookie(String name) {
        return getCookies().get(name);
    }

    protected String getCookieValue(String name) {
        HttpCookieMap cookies = getCookies();
        if (!cookies.containsKey(name)) {
            return null;
        }

        Cookie cookie = getCookies().get(name);
        return cookie.getValue();
    }

    protected void setCookie(String name, Cookie cookie) {
        getCookies().set(name, cookie);
    }

    protected void setCookieValue(String name, String value) {
        getCookies().set(name, new Cookie(name, value));
    }

    protected boolean containsCookie(String name) {
        return getCookies().containsKey(name);
    }

    protected void removeCookie(String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        getCookies().set(name, cookie);
    }

    protected HttpSessionMap getSessions() {
        return getHttpContext().getSession();
    }

    protected Object getSession(String name) {
        return getSessions().get(name);
    }

    protected void setSession(String name, Object value) {
        getSessions().put(name, value);
    }

    protected boolean containsSession(String name) {
        return getSessions().containsKey(name);
    }

    protected void removeSession(String name) {
        if (containsSession(name)) {
            getSessions().remove(name);
        }
    }

    protected ValueResult value(Object obj) {
        return new ValueResult(obj);
    }

    protected RedirectResult redirect(String url) {
        return new RedirectResult(url);
    }

    protected HtmlResult html(String html) {
        return new HtmlResult(html);
    }

    protected JsonResult json(Object object) {
        return new JsonResult(object);
    }

    protected JsonResult json(String format, Object... args) {
        return json(jsono(format, args));
    }

    protected ViewResult view(String view) {
        return new ViewResult(view);
    }

    protected JSONObject jsono(String format, Object... args) {
        JSONObject jsonObject = new JSONObject();
        String[] names = format.split("\\|");

        for (int i = 0; i < names.length; i++) {
            if (i < args.length) {
                jsonObject.put(names[i], args[i]);
            } else {
                jsonObject.put(names[i], null);
            }
        }

        return jsonObject;
    }

    public ActionContext getActionContext() {
        return actionContext;
    }

    public void setActionContext(ActionContext actionContext) {
        this.actionContext = actionContext;
    }

    public Map<String, Object> getModelMap() {
        return actionContext.getModelMap();
    }

    protected Object putModel(String key, Object model) {
        return actionContext.getModelMap().put(key, model);
    }

    protected IResult beforeExecute(HttpContext context, Method method, Object[] argValues) {
        return null;
    }

    protected IResult afterExecute(HttpContext context, Method method, Object[] argValues, IResult result) {
        return null;
    }
}
