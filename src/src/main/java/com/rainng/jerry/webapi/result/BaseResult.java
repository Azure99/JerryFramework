package com.rainng.jerry.webapi.result;

import com.rainng.jerry.mouse.http.HttpResponse;
import com.rainng.jerry.mouse.http.constant.HttpStatusCode;

public class BaseResult implements IResult {
    @Override
    public void executeResult(ActionContext context) throws Exception {
        HttpResponse response = context.getHttpContext().getResponse();
        if (response.getStatusCode() == HttpStatusCode.HTTP_NOT_FOUND) {
            response.setStatusCode(HttpStatusCode.HTTP_OK);
        }
    }
}
