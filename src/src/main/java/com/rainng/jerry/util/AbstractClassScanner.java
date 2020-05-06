package com.rainng.jerry.util;

public abstract class AbstractClassScanner {
    public abstract Class<?>[] scan(Class<?> root);
    public abstract String[] scanName(Class<?> root);
}
