package com.rainng.jerry.webapi.result;

import com.rainng.jerry.mouse.http.HttpResponse;
import com.rainng.jerry.mouse.http.constant.HttpHeaderKey;
import com.rainng.jerry.mouse.http.constant.HttpStatusCode;

public class RedirectResult extends BaseResult {
    private String url;

    public RedirectResult(String url) {
        this.url = url;
    }

    @Override
    public void executeResult(ActionContext context) throws Exception {
        super.executeResult(context);

        HttpResponse response = context.getHttpContext().getResponse();
        response.setStatusCode(HttpStatusCode.HTTP_MOVED_TEMPORARILY);
        response.getHeaders().set(HttpHeaderKey.LOCATION, url);
    }
}
