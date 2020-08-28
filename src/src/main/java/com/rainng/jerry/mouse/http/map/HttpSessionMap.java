package com.rainng.jerry.mouse.http.map;

import java.util.concurrent.ConcurrentHashMap;

public class HttpSessionMap extends ConcurrentHashMap<String, Object> {
    private final int sessionId;
    private long lastRequestTime = System.currentTimeMillis();

    public HttpSessionMap(int sessionId) {
        this.sessionId = sessionId;
    }

    public long getLastRequestTime() {
        return lastRequestTime;
    }

    public void updateLastRequestTime() {
        lastRequestTime = System.currentTimeMillis();
    }

    public int getSessionId() {
        return sessionId;
    }
}
