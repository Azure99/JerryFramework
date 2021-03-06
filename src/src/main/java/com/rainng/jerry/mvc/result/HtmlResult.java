package com.rainng.jerry.mvc.result;

import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.HttpResponse;
import com.rainng.jerry.mouse.http.constant.HttpContentType;

import java.nio.charset.StandardCharsets;

public class HtmlResult extends BaseResult {
    private final String html;

    public HtmlResult(String html) {
        this.html = html;
    }

    @Override
    public void executeResult(ActionContext context) throws Exception {
        super.executeResult(context);

        HttpContext httpContext = context.getHttpContext();
        HttpResponse response = httpContext.getResponse();

        response.setContentType(HttpContentType.TEXT_HTML);
        byte[] data = html.getBytes(StandardCharsets.UTF_8);
        response.getBody().write(data);
    }

    @Override
    public Object getResultObject() {
        return html;
    }

    @Override
    public String getResultString() {
        return html;
    }
}