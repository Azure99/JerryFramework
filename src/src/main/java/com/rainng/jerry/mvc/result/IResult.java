package com.rainng.jerry.mvc.result;

public interface IResult {
    void executeResult(ActionContext context) throws Exception;

    Object getResultObject();

    String getResultString();
}
