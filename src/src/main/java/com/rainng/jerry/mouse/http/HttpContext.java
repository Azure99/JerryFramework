package com.rainng.jerry.mouse.http;

public class HttpContext {
    private HttpRequest request;
    private HttpResponse response;
    private Session session;

    public HttpContext() {
        request = new HttpRequest();
        response = new HttpResponse();

        request.setHttpContext(this);
        response.setHttpContext(this);
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
