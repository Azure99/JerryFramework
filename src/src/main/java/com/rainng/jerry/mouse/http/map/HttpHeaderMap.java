package com.rainng.jerry.mouse.http.map;

public class HttpHeaderMap extends BaseHttpMap<String> {

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        forEach((key, value) -> builder.append(key).append(": ").append(value).append("\r\n"));

        return builder.toString();
    }
}
