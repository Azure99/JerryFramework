package com.rainng.jerry.mvc.result;

import com.alibaba.fastjson.JSON;
import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.HttpResponse;
import com.rainng.jerry.mouse.http.constant.HttpContentType;

import java.nio.charset.StandardCharsets;

public class JsonResult extends BaseResult {
    private final String jsonString;
    private Object object;

    public JsonResult(Object object) {
        this.jsonString = JSON.toJSONString(object);
    }

    @Override
    public void executeResult(ActionContext context) throws Exception {
        super.executeResult(context);

        HttpContext httpContext = context.getHttpContext();
        HttpResponse response = httpContext.getResponse();

        response.setContentType(HttpContentType.JSON);
        byte[] data = jsonString.getBytes(StandardCharsets.UTF_8);
        response.getBody().write(data);
    }

    @Override
    public Object getResultObject() {
        return object;
    }

    @Override
    public String getResultString() {
        return jsonString;
    }
}
