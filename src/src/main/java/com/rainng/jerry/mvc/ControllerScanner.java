package com.rainng.jerry.mvc;

import com.rainng.jerry.util.FileClassScanner;

import java.util.ArrayList;

public class ControllerScanner {
    public static Class<?>[] scan(Class<?> app) {
        Class<?>[] classes = new FileClassScanner().scan(app);
        ArrayList<Class<?>> controllerList = new ArrayList<>();
        for (Class<?> clazz : classes) {
            if (Controller.class.equals(clazz.getSuperclass())) {
                controllerList.add(clazz);
            }
        }

        return controllerList.toArray(new Class[0]);
    }
}
