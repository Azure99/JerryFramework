package com.rainng.jerry.webapi.result;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.HttpResponse;
import com.rainng.jerry.mouse.http.constant.HttpContentType;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class JsonResult extends BaseResult {
    private Object object;
    private String jsonString;

    public JsonResult(Object object) {
        this.object = object;
        int feature = SerializerFeature.WriteMapNullValue.getMask() | SerializerFeature.QuoteFieldNames.getMask();
        this.jsonString = JSON.toJSONString(object, feature);
    }

    @Override
    public void executeResult(ActionContext context) throws Exception {
        super.executeResult(context);

        HttpContext httpContext = context.getHttpContext();
        HttpResponse response = httpContext.getResponse();

        try {
            response.setContentType(HttpContentType.JSON);
            byte[] data = jsonString.getBytes(StandardCharsets.UTF_8);
            response.getBody().write(data);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
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
