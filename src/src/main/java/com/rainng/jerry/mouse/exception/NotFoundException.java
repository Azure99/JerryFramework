package com.rainng.jerry.mouse.exception;

public class NotFoundException extends HttpException {
    public NotFoundException(String message) {
        super(message);
    }
}
