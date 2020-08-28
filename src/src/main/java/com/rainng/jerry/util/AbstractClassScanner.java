package com.rainng.jerry.util;

public abstract class AbstractClassScanner {
    public abstract Class<?>[] scanClasses(Class<?> root);

    public abstract String[] scanClassNames(Class<?> root);
}
