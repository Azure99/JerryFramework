package com.rainng.jerry.mouse.http.initializer;

import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.HttpRequest;
import com.rainng.jerry.mouse.http.constant.HttpContentType;
import com.rainng.jerry.mouse.http.constant.RequestMethod;
import com.rainng.jerry.mouse.http.map.HttpHeaderMap;
import com.rainng.jerry.mouse.http.map.HttpQueryMap;
import com.rainng.jerry.mouse.util.UrlEncoding;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class HttpRequestIniter {

    public static void init(HttpContext context, InputStream inputStream) throws IOException {
        HttpRequest request = context.getRequest();

        byte[] headData = readHttpRequestHeadData(inputStream);
        initHttpRequestHead(request, headData);

        byte[] bodyData = readHttpRequestBodyData(inputStream, (int) request.getContentLength());
        initHttpRequestBody(request, bodyData);
    }

    private static byte[] readHttpRequestHeadData(InputStream inputStream) throws IOException {
        ByteArrayOutputStream tempHeadStream = new ByteArrayOutputStream();

        int[] endFlagArray = new int[]{13, 10, 13, 10};
        int[] endFlagBuffer = new int[4];
        boolean endFlagFound = false;

        int b = 0;
        while (b != -1 && !endFlagFound) {
            b = inputStream.read();
            tempHeadStream.write(b);

            for (int i = 0; i < 3; i++) {
                endFlagBuffer[i] = endFlagBuffer[i + 1];
            }
            endFlagBuffer[3] = b;

            endFlagFound = true;
            for (int i = 0; i < 4; i++) {
                if (endFlagBuffer[i] != endFlagArray[i]) {
                    endFlagFound = false;
                    break;
                }
            }
        }

        byte[] headData = tempHeadStream.toByteArray();
        tempHeadStream.close();

        return headData;
    }

    private static void initHttpRequestHead(HttpRequest request, byte[] headData) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(headData));

        String method = scanner.next();
        String path = scanner.next();
        String version = scanner.next();
        scanner.nextLine();

        HttpHeaderMap headers = new HttpHeaderMap();
        while (true) {
            String line = scanner.nextLine();
            if (line.length() == 0) {
                break;
            }

            String[] kv = line.split(":\\s");
            String key = convertHeaderKey(kv[0]);
            String value = kv[1];
            headers.set(key, value);
        }

        request.setMethod(parseRequestMethod(method));
        request.setPath(path);
        request.setResourcePath(parseResourcePath(path));
        request.setQueryString(parseQueryString(path));
        request.setQueryArgs(parseQueryArgs(request.getQueryString()));
        request.setVersion(version);
        request.setHeaders(headers);
    }

    private static byte[] readHttpRequestBodyData(InputStream inputStream, int contentLength) throws IOException {
        ByteArrayOutputStream bodyOutputStream = new ByteArrayOutputStream(contentLength);

        if (contentLength > 0) {
            byte[] buffer = new byte[1024];
            int readLength = 0;

            while (readLength < contentLength) {
                int len = Math.min(buffer.length, contentLength - readLength);
                inputStream.read(buffer, 0, len);
                bodyOutputStream.write(buffer, 0, len);
                readLength += len;
            }
        }

        byte[] bodyData = bodyOutputStream.toByteArray();
        bodyOutputStream.close();

        return bodyData;
    }

    private static void initHttpRequestBody(HttpRequest request, byte[] bodyData) {
        request.setBody(new ByteArrayInputStream(bodyData));

        switch (request.getContentType()) {
            case HttpContentType.TEXT_PLAIN:
            case HttpContentType.FORM_URLENCODED:
            case HttpContentType.JSON:
            case HttpContentType.TEXT_HTML:
                request.setBodyString(new String(bodyData));
                break;
            default:
        }

        if (!request.getContentType().equals(HttpContentType.FORM_URLENCODED)) {
            return;
        }

        request.setForm(parseQueryArgs(new String(bodyData)));
    }

    private static String convertHeaderKey(String key) {
        String[] split = key.split("-");

        for (int i = 0; i < split.length; i++) {
            if (Character.isLowerCase(split[i].charAt(0))) {
                char[] chars = split[i].toCharArray();
                chars[0] -= 32;
                split[i] = String.valueOf(chars);
            }
        }

        return String.join("-", split);
    }

    private static RequestMethod parseRequestMethod(String method) {
        switch (method) {
            case "GET":
                return RequestMethod.GET;
            case "POST":
                return RequestMethod.POST;
            case "OPTIONS":
                return RequestMethod.OPTIONS;
            default:
                return RequestMethod.GET;
        }
    }

    private static String parseResourcePath(String path) {
        int pos = path.indexOf("?");
        if (pos == -1) {
            return path;
        } else {
            return path.substring(0, pos);
        }
    }

    private static String parseQueryString(String path) {
        int pos = path.indexOf("?");
        if (pos == -1) {
            return "";
        } else {
            return path.substring(pos + 1);
        }
    }

    private static HttpQueryMap parseQueryArgs(String queryString) {
        HttpQueryMap queryMap = new HttpQueryMap();

        String[] args = queryString.split("&");
        for (String arg : args) {
            String[] kv = arg.split("=");

            if (kv.length == 2) {
                kv[0] = UrlEncoding.decode(kv[0]);
                kv[1] = UrlEncoding.decode(kv[1]);
                queryMap.set(kv[0], kv[1]);
            } else if (kv.length == 1 && arg.contains("=")) {
                kv[0] = UrlEncoding.decode(kv[0]);
                queryMap.set(kv[0], null);
            }
        }

        return queryMap;
    }
}
