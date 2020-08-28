package com.rainng.jerry.mouse.middleware;

import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.HttpRequest;
import com.rainng.jerry.mouse.http.HttpResponse;
import com.rainng.jerry.mouse.http.constant.HttpContentType;
import com.rainng.jerry.mouse.http.constant.HttpStatusCode;

import java.io.File;
import java.nio.file.Files;

public class StaticWebMiddleware extends BaseMiddleware {
    private final String rootDirectory;
    private final String defaultFile;

    public StaticWebMiddleware() {
        this("wwwroot");
    }

    public StaticWebMiddleware(String rootDirectory) {
        this(rootDirectory, "index.html");
    }

    public StaticWebMiddleware(String rootDirectory, String defaultFile) {
        this.rootDirectory = rootDirectory;
        this.defaultFile = defaultFile;
    }

    @Override
    public void onExecute(HttpContext context) throws Exception {
        HttpRequest request = context.getRequest();
        HttpResponse response = context.getResponse();

        String resourcePath = rootDirectory + request.getResourcePath();
        if (resourcePath.endsWith("/")) {
            resourcePath += defaultFile;
        }

        File file = new File(resourcePath);
        if (!file.exists()) {
            response.setStatusCode(HttpStatusCode.HTTP_NOT_FOUND);
            next(context);

        } else {
            response.setStatusCode(HttpStatusCode.HTTP_OK);
            response.setContentType(HttpContentType.getContentTypeByFileName(file));
            byte[] content = Files.readAllBytes(file.toPath());

            response.getBody().write(content);
        }
    }
}
