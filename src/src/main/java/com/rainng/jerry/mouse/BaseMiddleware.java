package com.rainng.jerry.mouse;

import com.rainng.jerry.mouse.http.HttpContext;

public class BaseMiddleware {
    private BaseMiddleware nextMiddleware ;

    public BaseMiddleware getNextMiddleware() {
        return nextMiddleware;
    }

    public void setNextMiddleware(BaseMiddleware nextMiddleware) {
        this.nextMiddleware = nextMiddleware;
    }

    public void onExecute(HttpContext context) throws Exception {
        next(context);
    }

    public void next(HttpContext context) throws Exception {
        if(nextMiddleware != null) {
            nextMiddleware.onExecute(context);
        }
    }
}
