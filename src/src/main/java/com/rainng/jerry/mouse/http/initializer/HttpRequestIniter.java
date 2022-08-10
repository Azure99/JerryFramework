package com.rainng.jerry.mouse.http.initializer;

import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.HttpRequest;
import com.rainng.jerry.mouse.http.constant.HttpContentType;
import com.rainng.jerry.mouse.http.constant.HttpMethod;
import com.rainng.jerry.mouse.http.map.HttpHeaderMap;
import com.rainng.jerry.mouse.http.map.HttpQueryMap;
import com.rainng.jerry.mouse.util.UrlEncodeUtil;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;

public class HttpRequestIniter {
    private HttpRequestIniter() {

    }

    public static void init(HttpContext context, HttpServerExchange serverExchange, byte[] requestBody) {
        HttpRequest request = context.getRequest();
        initHttpRequestHead(request, serverExchange);
        initHttpRequestBody(request, requestBody);
    }

    private static void initHttpRequestHead(HttpRequest request, HttpServerExchange serverExchange) {
        request.setVersion(serverExchange.getProtocol().toString());
        request.setMethod(HttpMethod.valueOf(serverExchange.getRequestMethod().toString()));
        request.setPath(serverExchange.getRequestURI());
        request.setQueryString(serverExchange.getQueryString());
        request.setQueryArgs(parseQueryArgs(request.getQueryString()));

        HttpHeaderMap headers = new HttpHeaderMap();
        for (HeaderValues headerValues : serverExchange.getRequestHeaders()) {
            headers.put(headerValues.getHeaderName().toString(), String.join("; ", headerValues.toArray()));
        }
        request.setHeaders(headers);
    }

    private static void initHttpRequestBody(HttpRequest request, byte[] requestBody) {
        request.setBody(requestBody);

        if (HttpContentType.FORM_URLENCODED.equals(request.getContentType())) {
            request.setForm(parseQueryArgs(request.getBodyString()));
        }
    }

    private static HttpQueryMap parseQueryArgs(String queryString) {
        HttpQueryMap queryMap = new HttpQueryMap();

        String[] args = queryString.split("&");
        for (String arg : args) {
            String[] kv = arg.split("=");

            if (kv.length == 2) {
                kv[0] = UrlEncodeUtil.decode(kv[0]);
                kv[1] = UrlEncodeUtil.decode(kv[1]);
                queryMap.set(kv[0], kv[1]);
            } else if (kv.length == 1 && arg.contains("=")) {
                kv[0] = UrlEncodeUtil.decode(kv[0]);
                queryMap.set(kv[0], null);
            }
        }

        return queryMap;
    }
}
