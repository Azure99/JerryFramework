package com.rainng.jerry.webapi.result;

import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.HttpResponse;
import com.rainng.jerry.mouse.http.constant.HttpContentType;

import java.io.UnsupportedEncodingException;

public class ValueResult extends BaseResult {
    private Object value;

    public ValueResult(Object value) {
        this.value = value;
    }

    @Override
    public void executeResult(ActionContext context) throws Exception {
        super.executeResult(context);

        HttpContext httpContext = context.getHttpContext();
        HttpResponse response = httpContext.getResponse();

        try {
            response.setContentType(HttpContentType.TEXT_PLAIN);
            byte[] data = value.toString().getBytes("UTF-8");
            response.getBody().write(data);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }
}
