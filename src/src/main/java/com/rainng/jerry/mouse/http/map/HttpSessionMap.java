package com.rainng.jerry.mouse.http.map;

public class HttpSessionMap extends BaseHttpMap<Object> {
    private long lastRequestTime = System.currentTimeMillis();
    private int sessionId;

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
