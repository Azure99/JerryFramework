package com.rainng.jerry.mvc.result;

import com.rainng.jerry.mouse.http.HttpContext;
import com.rainng.jerry.mouse.http.HttpResponse;
import com.rainng.jerry.mouse.http.constant.HttpContentType;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ViewResult extends BaseResult {
    private static TemplateEngine templateEngine = new TemplateEngine();
    private String viewPath;
    private Map<String, Object> variables;

    public ViewResult(String viewPath) {
        this.viewPath = viewPath;
    }

    @Override
    public void executeResult(ActionContext context) throws Exception {
        super.executeResult(context);

        HttpContext httpContext = context.getHttpContext();
        HttpResponse response = httpContext.getResponse();

        variables = context.getModelMap();

        try {
            response.setContentType(HttpContentType.TEXT_HTML);
            byte[] data = generateHtml().getBytes(StandardCharsets.UTF_8);
            response.getBody().write(data);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }

    private String generateHtml() {
        Context templateContext = new Context();
        templateContext.setVariables(variables);
        try {

            return templateEngine.process(loadTemplate(), templateContext);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return "";
    }

    private String loadTemplate() throws IOException {
        InputStream stream = this.getClass().getResourceAsStream("/static/" + viewPath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line).append("\r\n");
        }

        return builder.toString();
    }
}
