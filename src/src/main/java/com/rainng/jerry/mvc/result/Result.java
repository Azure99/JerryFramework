package com.rainng.jerry.mvc.result;

public interface Result {
    void executeResult(ActionContext context) throws Exception;

    Object getResultObject();

    String getResultString();
}
