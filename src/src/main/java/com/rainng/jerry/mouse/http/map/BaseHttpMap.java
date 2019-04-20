package com.rainng.jerry.mouse.http.map;

import java.util.HashMap;

public class BaseHttpMap extends HashMap<String, String> {
    public String get(String key, String defaultValue) {
        return containsKey(key) ? get(key) : defaultValue;
    }

    public void set(String key, String value) {
        put(key, value);
    }
}
