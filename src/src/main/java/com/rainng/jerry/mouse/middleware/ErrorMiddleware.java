package com.rainng.jerry.mouse.middleware;

import com.rainng.jerry.mouse.exception.HttpException;
import com.rainng.jerry.mouse.exception.NotFoundException;
import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.HttpResponse;
import com.rainng.jerry.mouse.http.constant.HttpStatusCode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class ErrorMiddleware extends BaseMiddleware {
    @Override
    public void onExecute(HttpContext context) throws Exception {
        try {
            next(context);

        }
        catch (Exception ex) {
            ex.printStackTrace();
            onException(context, ex);
        }
    }

    private void onException(HttpContext context, Exception ex) {
        HttpResponse response = context.getResponse();

        try {
            response.getBody().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.setBody(new ByteArrayOutputStream());

        if(ex instanceof HttpException) {
            httpException(response, ex);
        }else {
            exception(response, ex);
        }
    }

    private void httpException(HttpResponse response, Exception exception) {
        PrintWriter writer = new PrintWriter(response.getBody());
        int statusCode = HttpStatusCode.HTTP_INTERNAL_SERVER_ERROR;

        if(exception instanceof NotFoundException) {
            statusCode = HttpStatusCode.HTTP_NOT_FOUND;
            writer.write("404 NotFound");
        }

        response.setStatusCode(statusCode);
        writer.flush();
    }

    private void exception(HttpResponse response, Exception ex) {
        response.setStatusCode(HttpStatusCode.HTTP_INTERNAL_SERVER_ERROR);

        PrintWriter writer = new PrintWriter(response.getBody());
        writer.write("500 Internal server error");
        writer.flush();
    }
}
