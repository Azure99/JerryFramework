package com.rainng.jerry.mouse.http.map;

public class HttpHeaderMap extends BaseHttpMap {

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (String key : keySet()) {
            builder.append(key);
            builder.append(": ");
            builder.append(get(key));
            builder.append("\r\n");
        }

        return builder.toString();
    }
}
