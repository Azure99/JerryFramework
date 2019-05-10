package com.rainng.jerry.mouse.http.map;

import java.util.HashMap;

public class BaseHttpMap<T> extends HashMap<String, T> {
    public T get(String key, T defaultValue) {
        return containsKey(key) ? get(key) : defaultValue;
    }

    public void set(String key, T value) {
        put(key, value);
    }
}
