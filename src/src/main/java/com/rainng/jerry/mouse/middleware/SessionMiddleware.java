package com.rainng.jerry.mouse.middleware;

import com.rainng.jerry.mouse.http.Cookie;
import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.map.HttpCookieMap;
import com.rainng.jerry.mouse.http.map.HttpSessionMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SessionMiddleware extends BaseMiddleware {
    private static final String SESSION_KEY = "_jerrysession";
    private long maxSessionAge;
    private Map<Integer, HttpSessionMap> sessionMap = new HashMap<>();

    public SessionMiddleware() {
        this(3600);
    }

    public SessionMiddleware(int maxSessionAge) {
        setMaxSessionAge(maxSessionAge);
    }

    public long getMaxSessionAge() {
        return maxSessionAge;
    }

    public void setMaxSessionAge(int maxSessionAge) {
        this.maxSessionAge = maxSessionAge * 1000;
    }

    @Override
    public void onExecute(HttpContext context) throws Exception {
        HttpSessionMap session = getSession(context);
        if (session == null) {
            session = createSession(context);
        }

        context.setSession(session);
        session.updateLastRequestTime();

        next(context);
    }

    private HttpSessionMap createSession(HttpContext context) {
        int sessionId = createSessionId();
        HttpSessionMap session = new HttpSessionMap(sessionId);

        HttpCookieMap cookies = context.getCookies();
        cookies.set(SESSION_KEY, new Cookie(SESSION_KEY, Integer.toString(sessionId)));
        sessionMap.put(sessionId, session);

        return session;
    }

    private HttpSessionMap getSession(HttpContext context) {
        HttpCookieMap cookies = context.getCookies();
        if (!cookies.containsKey(SESSION_KEY)) {
            return null;
        }

        Cookie cookie = cookies.get(SESSION_KEY);

        try {
            int sessionId = Integer.valueOf(cookie.getValue());
            if (sessionMap.containsKey(sessionId)) {
                HttpSessionMap session = sessionMap.get(sessionId);
                if (session.getLastRequestTime() + maxSessionAge > System.currentTimeMillis()) {
                    return session;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private int createSessionId() {
        Random random = new Random();
        int sessionId = random.nextInt(Integer.MAX_VALUE);
        while (sessionMap.containsKey(sessionId)) {
            sessionId = random.nextInt(Integer.MAX_VALUE);
        }

        return sessionId;
    }
}
