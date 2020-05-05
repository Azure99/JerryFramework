package com.rainng.jerry.mouse.middleware;

import com.rainng.jerry.mouse.http.Cookie;
import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.map.HttpCookieMap;
import com.rainng.jerry.mouse.http.map.HttpSessionMap;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SessionMiddleware extends BaseMiddleware {
    private static final String SESSION_KEY = "_jerrysession";
    private long maxSessionAge;
    private Map<Integer, HttpSessionMap> sessionMap = new ConcurrentHashMap<>();

    public SessionMiddleware() {
        this(3600);
    }

    public SessionMiddleware(int maxSessionAge) {
        setMaxSessionAge(maxSessionAge);
        startSessionLifeMonitor();
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
            int sessionId = Integer.parseInt(cookie.getValue());
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

    private void startSessionLifeMonitor() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();

                List<Integer> expiredSessionKeys = new ArrayList<>();
                for (Integer key : sessionMap.keySet()) {
                    HttpSessionMap session = sessionMap.get(key);
                    if (session.getLastRequestTime() + maxSessionAge < currentTime) {
                        expiredSessionKeys.add(key);
                    }
                }

                for (Integer key : expiredSessionKeys) {
                    sessionMap.remove(key);
                }
            }
        }, 0, 60000);
    }
}
